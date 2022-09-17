package com.carpool.partyMatch.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;


@Embeddable
public class Carpooler {

    String userId;
    String name;

    protected Carpooler(){}
    public Carpooler(String userId,String name){
        this.userId = userId;
        this.name = name;
    }

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

}
