package com.example.sujeeth.dochelper;

/**
 * Created by sujeeth on 1/6/2018.
 */

public class Prescription {
    public String name, type, dose, frequency_of_ingestion, quantity, instructions, status;
    public boolean before_breakfast, breakfast, lunch, dinner;
    public String created_by, created_on, modified_by, modified_on;

    public Prescription(){
        status = "A";
        modified_by = "";
        modified_on = "";
    }
}
