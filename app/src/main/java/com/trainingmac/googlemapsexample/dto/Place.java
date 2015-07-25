package com.trainingmac.googlemapsexample.dto;

/**
 * Created by pepe on 7/22/15.
 */
public class Place {

    private String placeName;
    private String vicinity;
    private double latitude;
    private double longitude;

    public Place() {
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PLACE INFO >>").append("\n");
        sb.append("Place Name: ").append(placeName).append("\n");
        sb.append("Vicinity: ").append(vicinity).append("\n");
        sb.append("Latitude: ").append(latitude).append("\n");
        sb.append("Longitude: ").append(longitude).append("\n");
        sb.append("\n");
        return sb.toString();
    }


}
