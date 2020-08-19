package com.practice.countdown;

import android.content.Context;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DataManager {

    final String TIMERS_TAG = "timers";
    final String TIMER_TAG = "timer";
    final String ID_TAG = "id";
    final String NAME_TAG = "name";
    final String END_DATE_TAG = "endDate";

    private Context context;
    private ArrayList<CountdownData> dataList;

    //region Accessors
    public ArrayList<CountdownData> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<CountdownData> dataList) {
        this.dataList = dataList;
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
                        getDataList().add(data);
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    super.characters(ch, start, length);
                    currentValue = currentValue +  new String(ch, start, length);
                }
            };

            InputStream inputStream = context.getAssets().open("save_data.xml");
            parser.parse(inputStream, handler);

            Toast.makeText(context, "Size: " + dataList.size(), Toast.LENGTH_SHORT).show();
        }
        catch (SAXException | ParserConfigurationException | IOException e) {
            Toast.makeText(context, e.getCause().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void Save() {

    }
}
