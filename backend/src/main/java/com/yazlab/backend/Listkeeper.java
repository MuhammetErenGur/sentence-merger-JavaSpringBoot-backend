package com.yazlab.backend;

import java.util.ArrayList;

public class Listkeeper {
    String a;
    ArrayList<Integer> first = new ArrayList<Integer>();
    ArrayList<Integer> second = new ArrayList<Integer>();
    ArrayList<Integer> list = new ArrayList<Integer>();
    ArrayList<Integer> list2 = new ArrayList<Integer>();
    ArrayList<Integer> list3 = new ArrayList<Integer>();
    ArrayList<Integer> list5 = new ArrayList<Integer>();
    ArrayList<Integer> list6 = new ArrayList<Integer>();
    Listkeeper(String a){
        this.a=a;
    }
    Listkeeper(ArrayList<Integer> first,ArrayList<Integer> second){
        this.first=first;
        this.second=second;
    }
    Listkeeper(){

    }
    Listkeeper(ArrayList<Integer> list,ArrayList<Integer> list2,ArrayList<Integer> list3,ArrayList<Integer> list5,ArrayList<Integer> list6){
        this.list=list;
        this.list2=list2;
        this.list3=list3;
        this.list5=list5;
        this.list6=list6;
    }
}
