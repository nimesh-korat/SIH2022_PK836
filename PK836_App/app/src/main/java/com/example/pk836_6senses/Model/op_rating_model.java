package com.example.pk836_6senses.Model;

public class op_rating_model {
    public String F_NAME;
    public String L_NAME;
    public String COMMENTS;
    public String BEHAVIOUR;
    public String ACCURACY;
    public String APPT_ID;
    public String USERID;

    public op_rating_model() {
    }

    public op_rating_model(String F_NAME, String L_NAME, String COMMENTS, String BEHAVIOUR, String ACCURACY, String APPT_ID, String USERID) {
        this.F_NAME = F_NAME;
        this.L_NAME = L_NAME;
        this.COMMENTS = COMMENTS;
        this.BEHAVIOUR = BEHAVIOUR;
        this.ACCURACY = ACCURACY;
        this.APPT_ID = APPT_ID;
        this.USERID = USERID;
    }

    public String getF_NAME() {
        return F_NAME;
    }

    public void setF_NAME(String f_NAME) {
        F_NAME = f_NAME;
    }

    public String getL_NAME() {
        return L_NAME;
    }

    public void setL_NAME(String l_NAME) {
        L_NAME = l_NAME;
    }

    public String getCOMMENTS() {
        return COMMENTS;
    }

    public void setCOMMENTS(String COMMENTS) {
        this.COMMENTS = COMMENTS;
    }

    public String getBEHAVIOUR() {
        return BEHAVIOUR;
    }

    public void setBEHAVIOUR(String BEHAVIOUR) {
        this.BEHAVIOUR = BEHAVIOUR;
    }

    public String getACCURACY() {
        return ACCURACY;
    }

    public void setACCURACY(String ACCURACY) {
        this.ACCURACY = ACCURACY;
    }

    public String getAPPT_ID() {
        return APPT_ID;
    }

    public void setAPPT_ID(String APPT_ID) {
        this.APPT_ID = APPT_ID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }
}
