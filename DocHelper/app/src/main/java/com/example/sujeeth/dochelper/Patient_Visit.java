package com.example.sujeeth.dochelper;

import java.util.ArrayList;

/**
 * Created by sujeeth on 1/6/2018.
 */

public class Patient_Visit {
    public String date, time, height, weight, blood_pressure, temperature, next_visit, doctor_id,
                created_on, modified_by, modified_on;
    public String observations, lab_recommendations, file_link, previous_visit_log, next_visit_log;
    public ArrayList<String> prescription_ids;

    public Patient_Visit(){
        modified_by = "";
        modified_on = "";
        file_link = "";
        prescription_ids = new ArrayList<String>();
        previous_visit_log = "";
        next_visit_log = null;
    }
}
