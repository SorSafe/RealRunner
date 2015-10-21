package com.example.acerth.helper;

import java.util.ArrayList;

public class User {

    private String thumbnailUrl;
    private int user_id;
    private String user_game_name;
    private double user_score;


    public User() {
    }

    public User(String thumbnailUrl, double user_score, int user_id, String user_game_name) {
        this.thumbnailUrl = thumbnailUrl;
        this.user_score = user_score;
        this.user_id = user_id;
        this.user_game_name = user_game_name;
    }

    public String getThumbnailUrl() {

        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {

        this.thumbnailUrl = thumbnailUrl;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_game_name() {

        return user_game_name;
    }

    public void setUser_game_name(String user_game_name) {

        this.user_game_name = user_game_name;
    }

    public double getUser_score() {
        return user_score;
    }

    public void setUser_score(double user_score) {
        this.user_score = user_score;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_game_name='" + user_game_name + '\'' +
                '}';
    }
}
