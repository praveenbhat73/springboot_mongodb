package com.example.springjob.service;

import com.example.springjob.collection.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Personserviceinterface {
   public  String save(Person ps);
   public List<Person> getbyname(String s);
   public void delete(String id);
   public  List<Person> findbyage(Integer minage,Integer maxage);
   Page<Person> search(String name, Integer minAge, Integer maxAge, String city, Pageable pageable);

}
