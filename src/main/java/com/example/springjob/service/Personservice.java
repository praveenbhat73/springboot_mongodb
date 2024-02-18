package com.example.springjob.service;

import com.example.springjob.collection.Person;
import com.example.springjob.repository.Personinterface;
import org.bson.Document;
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

//import javax.swing.text.Document;
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
        //THIS IS QUERY OBJECT WHICH HELPS TO FETCH DATA FROM THE DATABASE.
        //IT TAKES PAGABLE PARAMETER WHICH HELPS IN pagination
        List<Criteria> criteria = new ArrayList<>();
        //This is used setup criteria query object helps to match the data from db and request param are same
        //if same add into criteria arraylist
        if(name !=null && !name.isEmpty()) {
            criteria.add(Criteria.where("fname").regex(name,"i"));
            //if it matches the condition add to creteria where fname == name and case sensitive
        }

        if(minAge !=null && maxAge !=null) {
            criteria.add(Criteria.where("age").gte(minAge).lte(maxAge));
            //same add to creteria where min<age>max
        }

        if(city !=null && !city.isEmpty()) {
            criteria.add(Criteria.where("address.city").regex(city,"i"));
            //where address.city == city
        }

        if(!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
            //if criteria isnt empty pass the criteria to query to fetch results as criteria is object list
            //has to convert it into arraytype and new Criteria[0] is used to create
            //right size array otherwise it creates object of it ..which is error
        }

        Page<Person> people = PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Person.class
                ), pageable, () -> mongoTemplate.count(query.skip(0).limit(0),Person.class));
        //used to fetch and store result in people
        return people;
    }
    public List<Document> getoldperson(){
        UnwindOperation unwindOperation = Aggregation.unwind("address");
        SortOperation sortOperation=Aggregation.sort(Sort.Direction.DESC,"age");
        GroupOperation groupOperation=Aggregation.group("address.city").first(Aggregation.ROOT)
                .as("oldestPerson");;
        Aggregation aggregation= Aggregation.newAggregation(unwindOperation,sortOperation,groupOperation);
        List<Document> person = mongoTemplate.aggregate(aggregation,Person.class, Document.class).getMappedResults();

        return person;

    }
    public List<Document> getpopulation(){
        UnwindOperation unwindOperation=Aggregation.unwind("address");
        GroupOperation groupOperation=Aggregation.group("address.city").count().as("Population");
        SortOperation sortOperation=Aggregation.sort(Sort.Direction.DESC,"Population");
        ProjectionOperation projectionOperation=Aggregation.project().
                andExpression("_id").as("city")
                .andExpression("Population").as("count").andExclude("_id");
        Aggregation aggregation=Aggregation.newAggregation(unwindOperation,groupOperation,sortOperation,projectionOperation);
        List<Document> doc= mongoTemplate.aggregate(aggregation, Person.class, Document.class).getMappedResults();
        return doc;
    }


}
