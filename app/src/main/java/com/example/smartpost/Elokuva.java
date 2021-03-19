package com.example.smartpost;

public class Elokuva {
    String movieID;
    String title;
    String productionYear;
    String lengthMin;
    String rating;

    public Elokuva(String a, String b, String c, String d, String e){
        movieID = a;
        title = b;
        productionYear = c;
        lengthMin = d;
        rating = e;
    }

    //--------------------------------------------------------------------------------

    public String getMovieID() { return movieID; }

    public String getTitle() { return title; }

    public String getProductionYear() { return productionYear; }

    public String getLengthMin() { return lengthMin; }

    public String getRating() { return rating; }
}
