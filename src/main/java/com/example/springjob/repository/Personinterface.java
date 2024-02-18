package com.example.springjob.repository;

import com.example.springjob.collection.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Personinterface extends MongoRepository<Person, String> {
     List<Person> findByfnameStartsWith(String name);

     @Query(value="{'age': { $gt : ?0 , $lt:?1}}")
     List<Person> findByageBetween(Integer minage,Integer maxage);
}
