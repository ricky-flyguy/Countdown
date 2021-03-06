package com.practice.countdown;

import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DataManager {

    public final static String TIMERS_TAG = "timers";
    public final static String TIMER_TAG = "timer";
    public final static String ID_TAG = "id";
    public final static String NAME_TAG = "name";
    public final static String END_DATE_TAG = "endDate";

    private final String SAVE_FILE_NAME = "save_data.xml";

    private Context context;
    private ArrayList<CountdownData> dataList;

    //region Accessors
    public ArrayList<CountdownData> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<CountdownData> dataList) {
        this.dataList = dataList;
    }

    public void addData(CountdownData data) {
        this.dataList.add(data);
    }
    //endregion

    public DataManager(Context context) {
        this.context = context;
        setDataList(new ArrayList<CountdownData>());
    }

    public void Load() {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

                CountdownData data;
                HashMap<String, String> map = new HashMap<>();
                String currentValue = "";
                boolean currentElement = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    super.startElement(uri,localName, qName, attributes);
                    currentElement = true;
                    currentValue = "";
                    if(localName.equals(TIMER_TAG)){
                        map = new HashMap<>();
                    }
                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    currentElement = false;
                    if (localName.equalsIgnoreCase(ID_TAG))
                        map.put(ID_TAG, currentValue);
                    else if (localName.equalsIgnoreCase(NAME_TAG))
                        map.put(NAME_TAG, currentValue);
                    else if (localName.equalsIgnoreCase(END_DATE_TAG))
                        map.put(END_DATE_TAG, currentValue);
                    else if (localName.equalsIgnoreCase(TIMER_TAG)) {
                        data = new CountdownData(map.get(ID_TAG), map.get(NAME_TAG), Long.parseLong(map.get(END_DATE_TAG)));
                        dataList.add(data);
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    super.characters(ch, start, length);
                    currentValue = currentValue +  new String(ch, start, length);
                }
            };

            // TODO: Load file from app internal storage

            String filePath = context.getFilesDir() + "/" + SAVE_FILE_NAME;
            File saveFile = new File(filePath);

            if (saveFile.exists() == false) {
                Toast.makeText(context, "no save file found.", Toast.LENGTH_LONG).show();
                return;
            }

            FileInputStream fInputStream = new FileInputStream(filePath);
            parser.parse(fInputStream, handler);

            //InputStream inputStream = context.openFileInput(filePath);
            //parser.parse(inputStream, handler);

            Toast.makeText(context, "Size: " + dataList.size(), Toast.LENGTH_SHORT).show();
        }
        catch (SAXException | ParserConfigurationException | IOException e) {
            Toast.makeText(context, e.getCause().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void Save() {
        // TODO: Save to app internal storage

        String str = CreateXML();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(str.getBytes("UTF-16"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String CreateXML() {
        String xmlString = "";
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(false);
        documentBuilderFactory.setValidating(false);
        DocumentBuilder builder = null;
        Document document = null;

        try {
            builder = documentBuilderFactory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        if (document != null) {
            // Create base element <timers>
            Element timers = document.createElement(TIMERS_TAG);
            CountdownData data;

            for (int i = 0; i < dataList.size(); i++) {
                data = dataList.get(i);

                // Create timer element <timer>
                Element t = document.createElement(TIMER_TAG);

                Element idEl = document.createElement(ID_TAG);
                Text text = document.createTextNode(data.getId());
                idEl.appendChild(text);
                t.appendChild(idEl);

                Element nameEl = document.createElement(NAME_TAG);
                text = document.createTextNode(data.getName());
                nameEl.appendChild(text);
                t.appendChild(nameEl);

                Element endDateEl = document.createElement(END_DATE_TAG);
                text = document.createTextNode(Long.toString(data.getEndTime()));
                endDateEl.appendChild(text);
                t.appendChild(endDateEl);

                timers.appendChild(t);
            }

            // Add <timer> element to root <timers>
            document.appendChild(timers);

            // Writing the XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;

            try {
                StringWriter sw = new StringWriter();
                transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-16");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
                transformer.transform(new DOMSource(document), new StreamResult(sw));
                xmlString = sw.toString();

                Toast.makeText(context, "data: " + xmlString, Toast.LENGTH_LONG).show();

            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }

        return xmlString;
    }
}
