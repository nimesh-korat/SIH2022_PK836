package com.example.pk836_6senses.Model;

public class User {
    String USERID, F_NAME, L_NAME, EMAIL_ID, PASSWORD, PHONE_NO, ADDRESS, CITY, STATE, PINCODE, DOB, GENDER, ROLE,DP_FILE, REG_DATE_TIME;


    public User() {
    }

    public User(String USERID, String F_NAME, String L_NAME, String EMAIL_ID, String PASSWORD, String PHONE_NO, String ADDRESS, String CITY, String STATE, String PINCODE, String DOB, String GENDER, String ROLE,String DP_FILE, String REG_DATE_TIME) {
        this.USERID = USERID;
        this.F_NAME = F_NAME;
        this.L_NAME = L_NAME;
        this.EMAIL_ID = EMAIL_ID;
        this.PASSWORD = PASSWORD;
        this.PHONE_NO = PHONE_NO;
        this.ADDRESS = ADDRESS;
        this.CITY = CITY;
        this.STATE = STATE;
        this.PINCODE = PINCODE;
        this.DOB = DOB;
        this.GENDER = GENDER;
        this.ROLE = ROLE;
        this.DP_FILE = DP_FILE;
        this.REG_DATE_TIME = REG_DATE_TIME;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
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

    public String getEMAIL_ID() {
        return EMAIL_ID;
    }

    public void setEMAIL_ID(String EMAIL_ID) {
        this.EMAIL_ID = EMAIL_ID;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getPHONE_NO() {
        return PHONE_NO;
    }

    public void setPHONE_NO(String PHONE_NO) {
        this.PHONE_NO = PHONE_NO;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getPINCODE() {
        return PINCODE;
    }

    public void setPINCODE(String PINCODE) {
        this.PINCODE = PINCODE;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }

    public String getDP_FILE() {
        return DP_FILE;
    }

    public void setDP_FILE(String DP_FILE) {
        this.DP_FILE = DP_FILE;
    }

    public String getREG_DATE_TIME() {
        return REG_DATE_TIME;
    }

    public void setREG_DATE_TIME(String REG_DATE_TIME) {
        this.REG_DATE_TIME = REG_DATE_TIME;
    }
}
