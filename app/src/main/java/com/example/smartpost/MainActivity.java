package com.example.smartpost;
// https://demonuts.com/android-spinner-searchable/
// Touko Tikkanen 1.4.2020
// updated 22.3.2021

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    EditText search;
    ListView listView;
    Spinner spinner;
    Context context;
    Button alku, loppu, pva;
    TextView alkuText, loppuText, pvaText;

    String currentTheater, name = "";
    int day, month, year, hour, minute;
    int myDay, myMonth, myYear, myHour1, myMinute1, myHour2, myMinute2;

    // Creation of singleton
    Finnkino list = Finnkino.getInstance();

    ArrayList<String> movies = null, theaters = new ArrayList<String>(), filterlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        search = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);
        pva = (Button) findViewById(R.id.btnPick);
        alku = (Button) findViewById(R.id.btnPick1);
        loppu = (Button) findViewById(R.id.btnPick2);
        pvaText = (TextView) findViewById(R.id.showDate);
        alkuText = (TextView) findViewById(R.id.showTime1);
        loppuText = (TextView) findViewById(R.id.showTime2);

        // permissions
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // read Finnkino information from website when opening app
        readXML();

        // add this information to an arraylist
        //options = list.getAll(); // this would add theater details to listview
        theaters = list.getNames(); // names used for spinner

        //--------------------------------------------------------------------------------

        setTime();
        chooseTime();

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

                // add movies in the theater to listview
                currentTheater = list.returnTeatteriID(name);
                readmovieXML(currentTheater);
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

    //--------------------------------------------------------------------------------------------

    public void setTime() {
        Calendar c = Calendar.getInstance();
        myHour1 = c.get(Calendar.HOUR);
        myMinute1 = c.get(Calendar.MINUTE);
        myYear = c.get(Calendar.YEAR);
        myMonth = c.get(Calendar.MONTH);
        myDay = c.get(Calendar.DAY_OF_MONTH);

        myHour2 = 23;
        myMinute2 = 59;
    }


    //https://www.tutorialspoint.com/how-to-use-date-time-picker-in-android
    public void chooseTime() {
        pva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                System.out.println(year + "." + month + "." + day);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myYear = year;
                        myDay = dayOfMonth;
                        myMonth = month;
                        System.out.println("Date picked: " + myYear + "." + myMonth + "." + myDay);

                        readmovieXML(currentTheater);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        alku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myHour1 = hourOfDay;
                        myMinute1 = minute;

                        readmovieXML(currentTheater);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        loppu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myHour2 = hourOfDay;
                        myMinute2 = minute;

                        readmovieXML(currentTheater);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }

    //--------------------------------------------------------------------------------------------

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

            String url, date;

            if (myDay == 0) {
                // get current date for url
                date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
            } else {
                // use picked date
                date = Integer.toString(myDay) + "." + Integer.toString(myMonth + 1) + "." + Integer.toString(myYear);
            }

            url = "https://www.finnkino.fi/xml/Schedule/?area=" + theaterID + "&dt=" + date;
            System.out.println(url);
            // show date to user
            pvaText.setText(date);
            // show time to user
            loppuText.setText(Integer.toString(myHour2) + ":" + Integer.toString(myMinute2));
            alkuText.setText(Integer.toString(myHour1) + ":" + Integer.toString(myMinute1));

            Document doc = builder2.parse(url);

            System.out.println(doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

            //--------------------------------------------------------------------------------------------
            // https://stackoverflow.com/questions/999172/how-to-parse-a-date
            String time1 = (Integer.toString(myYear) + "-" + Integer.toString(myMonth + 1) + "-" + Integer.toString(myDay) + "T"
                    + Integer.toString(myHour1) + ":" + Integer.toString(myMinute1) + ":00");
            String time2 = (Integer.toString(myYear) + "-" + Integer.toString(myMonth + 1) + "-" + Integer.toString(myDay) + "T"
                    + Integer.toString(myHour2) + ":" + Integer.toString(myMinute2) + ":00");
            System.out.println("start: " + time1);
            System.out.println("end: " + time2);

            SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss");
            Date date1 = formatter.parse(time1);
            Date date2 = formatter.parse(time2);

            //--------------------------------------------------------------------------------------------

            movies = parseNodeMovie(nList, theaterID, date1, date2);
            System.out.println("Movies retrieved. Adding...");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movies);
            listView.setAdapter(adapter);

        }catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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

    private ArrayList<String> parseNodeMovie(NodeList nList, String theaterID, Date date1, Date date2) throws ParseException {
        ArrayList<String> elokuva_array = new ArrayList<String>();

        for (int i=0; i<nList.getLength(); i++) {
            Node node = nList.item(i);
            //System.out.println(node.getNodeName());

            if (node.getNodeType() == node.ELEMENT_NODE) {
                // tehdään elementistä olio
                Element element = (Element) node;

                elokuva_array = list.addMovies(element, theaterID, date1, date2, elokuva_array);
            }
        }

        return elokuva_array;
    }
}
