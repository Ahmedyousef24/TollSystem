package Model;

import java.util.Date;

public class Trip {

    private double longitude;
    private double latitude;
    private Date timeStamp;
    private String dueDate;
    private String ticketPrice;
    private String address;
    private Boolean isPaid;

    public Trip(double longitude, double latitude, String ticketPrice, String address, String dueDate , Date date,Boolean isPaid){
        this.longitude = longitude;
        this.latitude = latitude;
        this.ticketPrice = ticketPrice;
        this.timeStamp = date;
        this.dueDate = dueDate;
        this.address = address;
        this.isPaid = isPaid;
        // add DUE DATE
    }


    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public String getDueDate() {
        return dueDate;
    }


    public String getAddress() {
        return address;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
