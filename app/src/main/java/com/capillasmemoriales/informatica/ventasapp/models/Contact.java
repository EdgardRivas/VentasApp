package com.capillasmemoriales.informatica.ventasapp.models;

public class Contact {

    private int id;
    private String fName;
    private String lName;
    private String phone;
    private String mail;

    public Contact () {

    }

    public Contact(int id, String fName, String lName, String phone, String mail) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
