package com.example.sujeeth.dochelper;

/**
 * Created by sujeeth on 1/4/2018.
 */

public class Patient {
    public String first_name,last_name,date_of_birth,gender,height,weight,
            blood_group,address,primary_number,primary_number_type,secondary_number,
            secondary_number_type,blood_pressure,email_id,created_on,created_by,modified_on,modified_by;
    public boolean diabetic;

    public Patient()
    {
        modified_on = "";
        modified_by = "";
    }
}
