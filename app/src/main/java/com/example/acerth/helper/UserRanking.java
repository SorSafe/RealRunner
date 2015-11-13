package com.example.acerth.helper;

/**
 * Created by ACERTH on 10/11/2015.
 */
public class UserRanking {

    private int num_row;
    private int user_id;
    private String user_game_name;
    private String user_image_name;
    private double sumDistance;

    public UserRanking() {
    }

    public UserRanking(int num_row, int user_id, String user_game_name, String user_image_name, double sumDistance) {
        this.num_row = num_row;
        this.user_id = user_id;
        this.user_game_name = user_game_name;
        this.user_image_name = user_image_name;
        this.sumDistance = sumDistance;
    }

    public int getNum_row() {
        return num_row;
    }

    public void setNum_row(int num_row) {
        this.num_row = num_row;
    }

    public void increaseNumRow(){
        num_row++;
    }

    public double getSumDistance() {
        return sumDistance;
    }

    public void setSumDistance(double sumDistance) {
        this.sumDistance = sumDistance;
    }

    public String getUser_game_name() {
        return user_game_name;
    }

    public void setUser_game_name(String user_game_name) {
        this.user_game_name = user_game_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_image_name() {
        return user_image_name;
    }

    public void setUser_image_name(String user_image_name) {
        this.user_image_name = user_image_name;
    }
}
