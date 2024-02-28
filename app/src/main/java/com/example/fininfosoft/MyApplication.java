package com.example.fininfosoft;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Realm initialisation
        Realm.init(this);
        //get the realm instance
        Realm realm = Realm.getDefaultInstance();
        //do transaction with the realm instance

        //Insert - write operation , we surround the operation bw transaction
//        realm.beginTransaction();
//        //method 1
//        Person person = realm.createObject(Person.class);
//        person.setName("Rajesh");
//        person.setAge(24);
//        person.setCity("EC4");
//        realm.commitTransaction();
//
//        //method 2
//        Person person1 = new Person("Ajay",24,"VZM");
//        realm.beginTransaction();
//        realm.copyFromRealm(person1);
//        realm.commitTransaction();

        //multiple entries
        List<Person> users = Arrays.asList(new Person("Ajay", 24, "VZM"), new Person("Uday", 24, "KKD"), new Person("Anil", 24, "SKLM"));
        realm.beginTransaction();
        realm.insert(users);
        realm.commitTransaction();

        //query
        RealmQuery<Person> realmQuery = realm.where(Person.class);
        //execute the query
//        RealmResults<Person> results = realmQuery.findAll();
//        results.get(0).getName();
//        results.get(0).getAge();
//        results.get(0).getCity();
//        System.out.println("Name "+        results.get(0).getName());
//        System.out.println("Age "+        results.get(0).getAge());
//        System.out.println("City "+        results.get(0).getCity());


        RealmResults<Person> sortByName = realm.where(Person.class).findAll().sort("name", Sort.ASCENDING);
        RealmResults<Person> sortByAge = realm.where(Person.class).findAll().sort("age", Sort.ASCENDING);
        RealmResults<Person> sortByCity = realm.where(Person.class).findAll().sort("city", Sort.ASCENDING);
        //System.out.println("Name "+sortByName.get(0).getName().equals("Ajay"));

        for (Person person : sortByName) {
            Log.d("RealmData", "Name: " + person.getName() + ", Age: " + person.getAge() + ", City: " + person.getCity());
        }

        realm.close();



    }
}
