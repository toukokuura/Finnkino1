package com.example.smartpost;

import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Teatteri {

    private String nimi = "";
    private String id = "";

    // annetut nimet
    public Teatteri(String a, String b){
        nimi = a;
        id = b;
    }

    //--------------------------------------------------------------------------------

    public ArrayList<String> addMovies(Element e, Date date1, Date date2, ArrayList<String> elokuva_array) throws ParseException {

        // make movie start time into Date format
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss");
        Date mdate = formatter.parse(e.getElementsByTagName("dttmShowStart").item(0).getTextContent());

        // check if mdate is after date1 and date2 is after mdate
        if (mdate.after(date1) && date2.after(mdate)) {
            System.out.println("Movie in range");
            elokuva_array.add(e.getElementsByTagName("Title").item(0).getTextContent() + " ("
                    + e.getElementsByTagName("ProductionYear").item(0).getTextContent() + ") \nPituus: "
                    + e.getElementsByTagName("LengthInMinutes").item(0).getTextContent() + "min \nIkäraja: "
                    + e.getElementsByTagName("Rating").item(0).getTextContent() + "\nAikaväli: "
                    + e.getElementsByTagName("dttmShowStart").item(0).getTextContent() + " : "
                    + e.getElementsByTagName("dttmShowEnd").item(0).getTextContent());
        } else {System.out.println("Movie outside of range");}

        return elokuva_array;
    }

    //--------------------------------------------------------------------------------

    public String getNimi() {
        return nimi;
    }

    public String getID(){
        return id;
    }


}
