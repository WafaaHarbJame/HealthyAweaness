package com.healthy.healthyaweaness.Model;

public class Medicine {
    private  String Medicine_Name;
    private  String Medicine_Description;
    private String id;


    public Medicine() {
    }

    public Medicine(String medicine_Name, String medicine_Description, String id) {
        Medicine_Name = medicine_Name;
        Medicine_Description = medicine_Description;
        this.id = id;
    }

    public String getMedicine_Name() {
        return Medicine_Name;
    }

    public void setMedicine_Name(String medicine_Name) {
        Medicine_Name = medicine_Name;
    }

    public String getMedicine_Description() {
        return Medicine_Description;
    }

    public void setMedicine_Description(String medicine_Description) {
        Medicine_Description = medicine_Description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
