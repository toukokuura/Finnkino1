package com.example.smartpost;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class Teatteri {

    private String nimi = "";
    private String id = "";
    private ArrayList<Elokuva> elokuva_array = new ArrayList<Elokuva>();

    // annetut nimet
    public Teatteri(String a, String b){
        nimi = a;
        id = b;
    }

    //--------------------------------------------------------------------------------

    public void addMovies(Element e){
        elokuva_array.add(new Elokuva(
                e.getElementsByTagName("ID").item(0).getTextContent(),
                e.getElementsByTagName("Title").item(0).getTextContent(),
                e.getElementsByTagName("ProductionYear").item(0).getTextContent(),
                e.getElementsByTagName("LengthInMinutes").item(0).getTextContent(),
                e.getElementsByTagName("Rating").item(0).getTextContent()
        ));
    }

    //--------------------------------------------------------------------------------

    public ArrayList<Elokuva> getInfo() {
        // return all info of a elokuva entities
        return elokuva_array;
    }

    public String getNimi() {
        return nimi;
    }

    public String getID(){
        return id;
    }


}
