package com.example.acerth.helper;

/**
 * Created by Admin PC on 18/10/2558.
 */
public class HistoryOfUser {
    private float distance;
    private int calories;
    private int step;
    private String elapsedTime;
    private String time_start;
    private String time_stop;

    public HistoryOfUser() {
    }

    public HistoryOfUser(int calories, float distance, String elapsedTime, int step, String time_start, String time_stop) {
        this.calories = calories;
        this.distance = distance;
        this.elapsedTime = elapsedTime;
        this.step = step;
        this.time_start = time_start;
        this.time_stop = time_stop;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_stop() {
        return time_stop;
    }

    public void setTime_stop(String time_stop) {
        this.time_stop = time_stop;
    }
}

