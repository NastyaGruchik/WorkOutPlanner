package com.example.workoutplanner;


import java.util.ArrayList;
public class ReadWriteUserDetails {
    public String fullName, doB, email, password;
    private ArrayList<Integer> trainingIds;

    public ReadWriteUserDetails() {
    }
    public ReadWriteUserDetails(String fullName, String doB, String email, String password){
        this.fullName=fullName;
        this.doB=doB;
        this.email=email;
        this.password=password;
        this.trainingIds = new ArrayList<>();
        this.trainingIds.add(-1);

    }

    public ArrayList<Integer> getTrainingIds() {
        return trainingIds;
    }

    public void setTrainingIds(ArrayList<Integer> trainingIds) {
        this.trainingIds = trainingIds;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
