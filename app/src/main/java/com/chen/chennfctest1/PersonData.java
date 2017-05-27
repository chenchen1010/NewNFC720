package com.chen.chennfctest1;

/**
 * Created by chenc on 2017/4/3.
 */

public class PersonData {
    String id;
    String name;
    String sex;
    String section;
    String duty;
    String tel;
    String email;

    public PersonData(String id, String email, String tel, String duty, String section, String sex, String name) {
        this.id = id;
        this.email = email;
        this.tel = tel;
        this.duty = duty;
        this.section = section;
        this.sex = sex;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
