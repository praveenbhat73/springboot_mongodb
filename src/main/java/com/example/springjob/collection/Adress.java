package com.example.springjob.collection;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Adress {
    private String address1;
    private String address2;
    private String city;
}
