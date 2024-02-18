package com.example.springjob.service;

import com.example.springjob.collection.Person;
import com.example.springjob.repository.Personinterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class Personservice  implements  Personserviceinterface{

//    @Autowired
    private Personinterface pi;

    private MongoTemplate mongoTemplate;
    @Autowired
    public Personservice(Personinterface pi,MongoTemplate mongoTemplate){
        this.pi=pi;
        this.mongoTemplate=mongoTemplate;
    }
public String save(Person ps){
  return pi.save(ps).getPersionid();
}
public List<Person> getbyname(String s){
    return pi.findByfnameStartsWith(s);
}
public void delete(String id){
        pi.deleteById(id);
}
public  List<Person> findbyage(Integer minage,Integer maxage){
        return pi.findByageBetween(minage,maxage);
}
    public Page<Person> search(String name, Integer minAge, Integer maxAge, String city, Pageable pageable) {

        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        if(name !=null && !name.isEmpty()) {
            criteria.add(Criteria.where("fname").regex(name,"i"));
        }

        if(minAge !=null && maxAge !=null) {
            criteria.add(Criteria.where("age").gte(minAge).lte(maxAge));
        }

        if(city !=null && !city.isEmpty()) {
            criteria.add(Criteria.where("address.city").is(city));
        }

        if(!criteria.isEmpty()) {
            query.addCriteria(new Criteria()
                    .andOperator(criteria.toArray(new Criteria[0])));
        }

        Page<Person> people = PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Person.class
                ), pageable, () -> mongoTemplate.count(query.skip(0).limit(0),Person.class));
        return people;
    }

}
