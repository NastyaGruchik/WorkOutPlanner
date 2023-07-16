package com.example.workoutplanner.trainingData;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;


public class Training {
    private int id;
    private String Uid;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private static int countId = 0;
    private int registeredNum;
    private int maxParticipants;

    private ArrayList<String> registeredUsers;
    private ArrayList<String> waitingListUsers;



    public Training() {

        this.maxParticipants=10;
        this.registeredUsers = new ArrayList<>();
        this.waitingListUsers = new ArrayList<>();
    }
    public Training(String name, String description, LocalDateTime dateTime) {
        this.id = countId++;
        this.Uid= "";
        this.name = name;
        this.description = description;
        this.dateTime =dateTime;
        this.registeredNum =0;
        this.maxParticipants = 10;
        this.registeredUsers = new ArrayList<>();
        this.waitingListUsers = new ArrayList<>();
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void returnToLocalDateTime(Timestamp dateTime) {
        if (dateTime != null) {
            this.dateTime= dateTime.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime  getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getRegisteredNum() {
        return registeredNum;
    }

    public void setRegisteredNum(int registeredNum) {
        this.registeredNum = registeredNum;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public ArrayList<String> getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(ArrayList<String> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public ArrayList<String> getWaitingListUsers() {
        return waitingListUsers;
    }

    public void setWaitingListUsers(ArrayList<String> waitingListUsers) {
        this.waitingListUsers = waitingListUsers;
    }

    public String getHour(){
        if(dateTime.getHour()<10)
            return "0"+dateTime.getHour();
        else
            return dateTime.getHour()+"";
    }
    public String getMinutes(){
        if(dateTime.getMinute()<10)
            return "0"+dateTime.getMinute();
        else
            return dateTime.getMinute()+"";
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", registeredNum=" + registeredNum +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", maxParticipants=" + maxParticipants +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return id == training.id;
    }
}

