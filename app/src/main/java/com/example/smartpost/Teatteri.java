package com.example.smartpost;

public class Teatteri {

    String nimi = "";
    String id = "";

    // annetut nimet
    public Teatteri(String a, String b){
        nimi = a;
        id = b;
    }

    public String getNimi() {
        return nimi;
    }

    public String getID(){
        return id;
    }


}
