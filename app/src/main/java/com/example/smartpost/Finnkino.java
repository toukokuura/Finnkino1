package com.example.smartpost;

import org.w3c.dom.Element;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Finnkino {

    private ArrayList<Teatteri> teatteri_array;

    // singleton, eli näitä voi olla vain yksi:
    private static Finnkino list = new Finnkino();

    public static Finnkino getInstance() {
        // pääohjelmasta kutsutaan tätä
        return list;
    }

    private Finnkino() {
        // privaatti rakentaja

        teatteri_array = new ArrayList<Teatteri>();

    }

    //--------------------------------------------------------------------------------

    public void addTeatteri(Element e) {
        // lisätään teatteriolio XML-tiedostosta
        teatteri_array.add(new Teatteri(
                e.getElementsByTagName("Name").item(0).getTextContent(),
                e.getElementsByTagName("ID").item(0).getTextContent()));
    }

    public String findTeatteri(String name) {
        // returns all info of a Teatteri as a string

        for (int i=0; i<teatteri_array.size(); i++) {

            // account i from array equal to accNUmber
            if (teatteri_array.get(i).getNimi().compareTo(name) == 0) {
                return (getInfo(i));
            }
        }

        return("Ei teattereita");
    }

    //--------------------------------------------------------------------------------

    public String returnTeatteriID(String name) {
        // returns the theaterID based on name

        for (int i=0; i<teatteri_array.size(); i++) {

            // account i from array equal to accNUmber
            if (teatteri_array.get(i).getNimi().compareTo(name) == 0) {
                return (teatteri_array.get(i).getID());
            }
        }

        return("0000");
    }

    //--------------------------------------------------------------------------------

    public ArrayList<String> addMovies(Element e, String theaterID, Date date1, Date date2, ArrayList<String> elokuva_array) throws ParseException {
        //etsi oikea teatteri
        int i = findTeatteriID(theaterID);

        if (i>-1) {
            //lisätään elokuvat tiettyyn teatteriin
            elokuva_array = teatteri_array.get(i).addMovies(e, date1, date2, elokuva_array);
        }

        return elokuva_array;
    }


    public int findTeatteriID(String theaterID) {
        // !!!! remember exception handling if calling this function !!!!
        // returns place of theater in theater array based on ID
        int i;
        for (i=0; i<teatteri_array.size(); i++) {

            if (teatteri_array.get(i).getID().compareTo(theaterID) == 0) {
                return (i);
            }
        }

        System.out.println("No theatre selected");
        return i=-1;
    }

    //--------------------------------------------------------------------------------

    public ArrayList<String> getAll() {
        // return all teatteri entities as string array
        ArrayList<String> tname_array = new ArrayList<String>();

        for (int i=0; i<teatteri_array.size(); i++) {
            tname_array.add(getInfo(i));
        }

        return tname_array;
    }

    public String getInfo(int i) {
        // return all info of a teatteri entity

        return ("Teatteri: " + teatteri_array.get(i).getNimi()
                + "\nID: " + teatteri_array.get(i).getID());
    }

    public ArrayList<String> getNames () {
        ArrayList<String> name_array = new ArrayList<String>();

        for (int i=0; i<teatteri_array.size(); i++) {
            name_array.add(teatteri_array.get(i).getNimi());
        }

        return name_array;
    }

}
