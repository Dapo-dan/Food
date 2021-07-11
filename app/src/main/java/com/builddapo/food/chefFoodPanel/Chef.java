package com.builddapo.food.chefFoodPanel;

public class Chef {

    private String Area, City, ConfirmPassword, Emailid, Fname, Lname, House, Mobile, Password, Postcode, State;

    public Chef(String area, String city, String confirmPassword, String emailid, String fname, String lname, String house, String mobile, String password, String postcode, String state) {
        this.Area = area;
        City = city;
        ConfirmPassword = confirmPassword;
        Emailid = emailid;
        Fname = fname;
        Lname = lname;
        House = house;
        Mobile = mobile;
        Password = password;
        Postcode = postcode;
        State = state;
    }

    public String getArea() {
        return Area;
    }

    public String getCity() {
        return City;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public String getEmailid() {
        return Emailid;
    }

    public String getFname() {
        return Fname;
    }

    public String getLname() {
        return Lname;
    }

    public String getHouse() {
        return House;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getPassword() {
        return Password;
    }

    public String getPostcode() {
        return Postcode;
    }

    public String getState() {
        return State;
    }
}
