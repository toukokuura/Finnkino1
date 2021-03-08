package com.example.smartpost;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class Finnkino {

    private int amount;
    private ArrayList<Teatteri> teatteri_array;

    // singleton, eli näitä voi olla vain yksi:
    private static Finnkino list = new Finnkino();

    public static Finnkino getInstance() {
        // pääohjelmasta kutsutaan tätä
        return list;
    }

    private Finnkino() {
        // privaatti rakentaja

        // Taulukon alustus (initializing)
        teatteri_array = new ArrayList<Teatteri>();

    }

    public void addTeatteri(Element e) {
        // lisätään smartpost-olioita XML-tiedostosta
        teatteri_array.add(new Teatteri(
                e.getElementsByTagName("Name").item(0).getTextContent(),
                e.getElementsByTagName("ID").item(0).getTextContent()));
    }

    public String findTeatteri(String name) {
        // returns all info of a Teatteri as a string

        for (int i=0; i<teatteri_array.size(); i++) {

            // account i from array equal to accNUmber
            if (teatteri_array.get(i).getNimi().compareTo(name) == 0) {
                // print account:
                return (getInfo(i));
            }
        }

        return("Ei teattereita");
    }

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
