package com.example.kim_dev.Memo;

public class Memo_data {

    String id;
    String m_day;
    String m_title;
    String m_content;
    String m_key;
    public Memo_data() {

    }

    // getter, setter 설정
    public String get_id() {return id;}
    public void set_id(String id) {this.id = id;}

    public String get_m_day() {return m_day;}
    public void set_m_day(String m_day) {this.m_day = m_day;}

    public String get_m_title() {return m_title;}
    public void set_m_title(String m_title) {this.m_title = m_title;}

    public String get_m_content() {return m_content;}
    public void set_m_content(String m_content) {this.m_content = m_content;}

    public String get_m_key() {return m_key;}
    public void set_m_key(String m_key) {this.m_key = m_key;}

    // 값을 추가할때 쓰는 함수.
    public Memo_data(String id, String m_day, String m_title, String m_content) {
        this.id = id;
        this.m_day = m_day;
        this.m_title = m_title;
        this.m_content = m_content;
    }
}

