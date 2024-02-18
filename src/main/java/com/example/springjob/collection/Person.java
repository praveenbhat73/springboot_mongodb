package com.example.springjob.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//@Data
//@Builder
@Document("person")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    @Id
    private String persionid;
    private String fname;
    private String lname;
    private int age;
    private List<String> hobbies;
    private List<Adress> address;

    public Person(String persionid, String fname, String lname, int age, List<String> hobbies, List<Adress> address) {
        this.persionid = persionid;
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.hobbies = hobbies;
        this.address = address;
    }

    public String getPersionid() {
        return persionid;
    }

    public void setPersionid(String persionid) {
        this.persionid = persionid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public List<Adress> getAddress() {
        return address;
    }

    public void setAddress(List<Adress> address) {
        this.address = address;
    }
}
