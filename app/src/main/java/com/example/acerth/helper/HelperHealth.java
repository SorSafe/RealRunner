package com.example.acerth.helper;


public class HelperHealth {
    private  int mem_id;
    private String mem_topic;
    private String mem_description;
    private String mem_pic_path;

    public HelperHealth() {
    }

    public HelperHealth(int mem_id, String mem_topic, String mem_description, String mem_pic_path) {
        this.mem_id = mem_id;
        this.mem_topic = mem_topic;
        this.mem_description = mem_description;
        this.mem_pic_path = mem_pic_path;
    }

    public int getMem_id() {
        return mem_id;
    }

    public void setMem_id(int mem_id) {
        this.mem_id = mem_id;
    }

    public String getMem_topic() {
        return mem_topic;
    }

    public void setMem_topic(String mem_topic) {
        this.mem_topic = mem_topic;
    }

    public String getMem_description() {
        return mem_description;
    }

    public void setMem_description(String mem_description) {
        this.mem_description = mem_description;
    }

    public String getMem_pic_path() {
        return mem_pic_path;
    }

    public void setMem_pic_path(String mem_pic_path) {
        this.mem_pic_path = mem_pic_path;
    }
}
