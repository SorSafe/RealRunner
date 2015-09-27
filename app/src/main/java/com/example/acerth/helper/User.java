package com.example.acerth.helper;

import java.util.ArrayList;

public class User {
    private int user_id;
    private int user_name;

    private ArrayList<String> genre;

    public User() {
    }

    public User(ArrayList<String> genre) {
        this.genre = genre;
    }
}
