package com.example.smartpost;
// https://demonuts.com/android-spinner-searchable/
// Touko Tikkanen 1.4.2020
// updated 22.3.2021

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    TextView info;
    EditText search;
    ListView listView;
    Spinner spinner;
    Context context;

    String name = "";
    Integer spinnernro;

    // Creation of singleton
    Finnkino list = Finnkino.getInstance();

    ArrayList<String> movies = null;
    ArrayList<String> theaters = new ArrayList<String>();
    ArrayList<String> filterlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        info = (TextView) findViewById(R.id.info);
        search = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);

        // permissions
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // read Finnkino information from website when opening app
        readXML();

        // add this information to an arraylist
        //options = list.getAll(); // this would add theater details to listview
        theaters = list.getNames(); // names used for spinner

        //--------------------------------------------------------------------------------

        // adapter for spinner
        // https://stackoverflow.com/questions/19820803/android-spinner-using-arrayliststring
        // https://www.codeproject.com/Tips/894233/Using-Spinner-Control-for-Filtering-ListView-Andro

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, theaters);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = parent.getItemAtPosition(position).toString();

                showSelected();

                // add movies in the theater to listview
                readmovieXML(list.returnTeatteriID(name));
                System.out.println("Movies added!");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //--------------------------------------------------------------------------------

        // filtering: https://stackoverflow.com/questions/36169944/adding-filter-for-spinner-and-filter-the-data-on-spinner-itself
        search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    // if something is written in the search, filter:
                    filterlist = new ArrayList<String>();
                    filterlist = getFilter(s.toString());

                    /*
                    //start using filtered list in spinner
                    ArrayAdapter<String> modAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, filterlist);
                    listView.setAdapter(modAdapter);
                    modAdapter.notifyDataSetChanged();

                     */

                } else {
                    // search is empty -> use full option list
                    //listView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                }

            }
        });
    }

    //--------------------------------------------------------------------------------------------

    public ArrayList<String> getFilter(CharSequence charSequence) {
        ArrayList<String> filterResultsData = new ArrayList<String>();;
        if(charSequence == null || charSequence.length() == 0) {
            return null;
        } else {
            for(String data : movies) {
                // options contains a String list of all entity information, one entity per cell
                // add in anything that matches the string written in search

                if(data.contains(charSequence)) {
                    filterResultsData.add(data);
                }
            }
            return filterResultsData;
        }
    }


    public void showSelected() {
        //  kirjoitetaan tiedot vastaavasta spinnerin oliosta
        //info.setText(list.findTeatteri(name));
    }

    public void readXML() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String url1 = "https://www.finnkino.fi/xml/TheatreAreas/";

            Document doc1 = builder.parse(url1);

            System.out.println(doc1.getDocumentElement().getNodeName());

            NodeList nList = doc1.getDocumentElement().getElementsByTagName("TheatreArea");
            parseNode(nList);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void readmovieXML(String theaterID) {
        try {
            DocumentBuilder builder2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // get current date for url
            String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

            String url = "https://www.finnkino.fi/xml/Schedule/?area=" + theaterID + "&dt=" + date;
            System.out.println(url);

            Document doc = builder2.parse(url);

            System.out.println(doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

            parseNodeMovie(nList, theaterID, date);
            System.out.println("Movies retrieved. Adding...");

            // add parsed movies to listview
            movies = list.getMovieInfo(theaterID);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movies);
            listView.setAdapter(adapter);

        }catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void parseNode(NodeList nList) {
        for (int i=0; i<nList.getLength(); i++) {
            Node node = nList.item(i);
            //System.out.println(node.getNodeName());

            if (node.getNodeType() == node.ELEMENT_NODE) {
                // tehdään elementistä olio
                Element element = (Element) node;

                list.addTeatteri(element);
            }
        }
    }

    private void parseNodeMovie(NodeList nList, String x, String y) {
        for (int i=0; i<nList.getLength(); i++) {
            Node node = nList.item(i);
            //System.out.println(node.getNodeName());

            if (node.getNodeType() == node.ELEMENT_NODE) {
                // tehdään elementistä olio
                Element element = (Element) node;

                System.out.println("Adding movies...");
                list.addMovies(element, x, y);
            }
        }
    }




}
