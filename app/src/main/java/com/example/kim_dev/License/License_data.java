package com.example.kim_dev.License;

public class License_data {

    String id;
    String l_name;
    String l_num;
    String l_date;
    String l_org;
    String l_key;
    public License_data() {

    }

    // getter, setter 설정
    public String get_id() {return id;}
    public void set_id(String id) {this.id = id;}

    public String get_l_name() {return l_name;}
    public void set_l_name(String l_name) {this.l_name = l_name;}

    public String get_l_num() {return l_num;}
    public void set_l_num(String l_num) {this.l_num = l_num;}

    public String get_l_date() {return l_date;}
    public void set_l_date(String l_date) {this.l_date = l_date;}

    public String get_l_org() {return l_org;}
    public void set_l_org(String l_org) {this.l_org = l_org;}

    public String get_l_key() {return l_key;}
    public void set_l_key(String l_key) {this.l_key = l_key;}


    // 값을 추가할때 쓰는 함수.
    public License_data(String id, String l_name, String l_num, String l_date, String l_org) {
        this.id = id;
        this.l_name = l_name;
        this.l_num = l_num;
        this.l_date = l_date;
        this.l_org = l_org;
    }
}

