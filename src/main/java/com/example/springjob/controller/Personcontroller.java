package com.example.springjob.controller;

import com.example.springjob.collection.Person;
import com.example.springjob.service.Personservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import org.bson.Document;
import java.util.List;

@RestController
public class Personcontroller {
//    @Autowired
    private Personservice ps;
    @Autowired
    public Personcontroller(Personservice ps){
        this.ps=ps;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping ("/person")
        public String post(@RequestBody Person ps1){
       return  ps.save(ps1);
        }
        @GetMapping("/personlist")
    public List<Person> retrivebyfirstname(@RequestParam("name") String n){
        return ps.getbyname(n);
        }

//        @ResponseStatus(HttpStatus.Succ)
        @DeleteMapping("/delete/{id}")
    public void deleteperson(@PathVariable("id") String id){
        ps.delete(id);
        }
        @GetMapping("/age")
    public List<Person> getbyage(@RequestParam Integer minage,@RequestParam Integer maxage){
        return ps.findbyage(minage,maxage);
        }
    @GetMapping("/search")
    public Page<Person> searchPerson(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ) {

        Pageable pageable
                = PageRequest.of(page,size);
        /*
        This line creates a Pageable object which represents pagination information,
        like which page of results to return and how many results per page.
         It's created using the PageRequest.of method with the page and size parameters provided.
         */
        return ps.search(name,minAge,maxAge,city,pageable);
    }
    /*
    what does above function does
    getmapping api endpoint is search
    which returns page of person type and accepts the requestparam and everything except page and size set to false


     */
    @GetMapping("/oldperson")
    public List<Document> getoldpersonbycity(){
        return ps.getoldperson();
    }
    @GetMapping("/population")
    public List<Document> getpop(){
        return ps.getpopulation();
    }



}
