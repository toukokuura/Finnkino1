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

    public void addMovies(Element e, String theaterID, String date) {
        //etsi oikea teatteri
        int i = findTeatteriID(theaterID);

        if (i>-1) {
            //lisätään elokuvat tiettyyn teatteriin
            teatteri_array.get(i).addMovies(e);
        }
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

    public ArrayList<String> getMovieInfo(String theaterID) {
        // return all movies from a specific theater

        int i = findTeatteriID(theaterID);

        // change format to string
        ArrayList<String> movies = new ArrayList<String>();

        if (i==-1) {
            return movies;
        } else {
            ArrayList<Elokuva> elokuva_array = teatteri_array.get(i).getInfo();



            for (int j = 0; j < elokuva_array.size(); j++) {

                movies.add(elokuva_array.get(j).getTitle()
                        + " (" + elokuva_array.get(j).getProductionYear() + ") \nPituus: "
                        + elokuva_array.get(j).getLengthMin() + "min \nIkäraja: "
                        + elokuva_array.get(j).getRating());
            }
            return movies;
        }
    }

    public ArrayList<String> getNames () {
        ArrayList<String> name_array = new ArrayList<String>();

        for (int i=0; i<teatteri_array.size(); i++) {
            name_array.add(teatteri_array.get(i).getNimi());
        }

        return name_array;
    }

}
