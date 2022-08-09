package com.example.kim_dev.Cocktail;

public class Cocktail_data {

    String id;
    String c_day;
    String c_name;
    String image_name;
    String c_content;
    String c_key;

    public Cocktail_data() {

    }

    // getter, setter 설정
    public String get_id() {return id;}
    public void set_id(String id) {this.id = id;}

    public String get_c_day() {return c_day;}
    public void set_c_day(String c_day) {this.c_day = c_day;}

    public String get_c_name() {return c_name;}
    public void set_c_name(String c_name) {this.c_name = c_name;}

    public String get_image_name() {return image_name;}
    public void set_image_name(String image_name) {this.image_name = image_name;}

    public String get_c_content() {return c_content;}
    public void set_c_content(String c_content) {this.c_content = c_content;}

    public String get_c_key() {return c_key;}
    public void set_c_key(String c_key) {this.c_key = c_key;}


    // 값을 추가할때 쓰는 함수.
    public Cocktail_data(String id, String c_day, String c_name, String image_name, String c_content) {
        this.id = id;
        this.c_day = c_day;
        this.c_name = c_name;
        this.image_name = image_name;
        this.c_content = c_content;
    }
}

