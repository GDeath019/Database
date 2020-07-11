package com.example.apprealm.RealM;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmDb extends RealmObject {
    @PrimaryKey
    private long id;
    private String specialized;
    private String point;
    private String imageUrl;

    public RealmDb() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpecialized() {
        return specialized;
    }

    public void setSpecialized(String specialized) {
        this.specialized = specialized;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
