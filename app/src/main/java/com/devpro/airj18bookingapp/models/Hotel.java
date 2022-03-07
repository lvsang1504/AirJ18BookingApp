package com.devpro.airj18bookingapp.models;

public class Hotel {
    public int id;
    public String title;
    public String imageUrl;
    public double rating;
    public int numberOfVotes;
    public String description;

    public Hotel(int id, String title, String imageUrl, double rating, int numberOfVotes, String description) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.numberOfVotes = numberOfVotes;
        this.description = description;
    }
}
