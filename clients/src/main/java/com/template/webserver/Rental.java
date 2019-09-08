package com.template.webserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rental {

    private final String renter;
    private final String owner;
    private String startDay;
    private String endDay;
    private String startTime;
    private String endTime;
    private String dailyPrice;
    private ArrayList<Image> images;

    public Rental(String renter, String owner, String startDay, String endDay, String startTime, String endTime, String dailyPrice) {
        this.renter = renter;
        this.owner = owner;
        this.startDay = startDay;
        this.endDay = endDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dailyPrice = dailyPrice;
        this.images = new ArrayList<Image>();
    }

    public String getRenter() {
        return renter;
    }

    public String getDailyPrice() {
        return dailyPrice;
    }

    public String getEndDay() {
        return endDay;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getOwner() {
        return owner;
    }

    public String getStartDay() {
        return startDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setDailyPrice(String dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void addImage(Image image) {
        this.images.add(image);
    }

    public ArrayList<List<String>>  getImages() {
        ArrayList<List<String>> temp = new ArrayList<List<String>>();
        for(int i = 0; i < images.size(); i++)
        {
            List<String> img = Arrays.asList("hash: " + images.get(i).getHash(), "caption: " + images.get(i).getCaption(), "location: "+ images.get(i).getLocation(), "timestamp: " + images.get(i).getTimestamp() );
            temp.add(img);
        }
            return temp;
    }
}

