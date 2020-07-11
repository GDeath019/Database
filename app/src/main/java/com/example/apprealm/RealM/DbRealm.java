package com.example.apprealm.RealM;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DbRealm extends RealmObject {
    @PrimaryKey
    private Long id;
    private String Name;
    private String University;
    private int Age;
//    private RealmList<RealmDb> Unv;

    public DbRealm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUniversity() {
        return University;
    }

    public void setUniversity(String university) {
        University = university;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

//    public RealmList<RealmDb> getUnv() {
//        return Unv;
//    }
//
//    public void setUnv(RealmList<RealmDb> unv) {
//        Unv = unv;
//    }
}

