package com.example.apprealm.RealM;

import android.app.Application;

import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ExApp extends Application {
    private String DbName = "FirstDb.realm";
    public static AtomicLong DbRmKey;
    public static AtomicLong RmDbKey;
    @Override
    public void onCreate() {
        super.onCreate();
        initRealM();
    }

    public void initRealM() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(DbName)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
//      sét cấu hình cho app
        Realm.setDefaultConfiguration(configuration);
//      lấy mẫu cấu hình ra
        Realm realm = Realm.getInstance(configuration);
//      set key auto cho các bảng
        try{
            RmDbKey = new AtomicLong(realm.where(RealmDb.class).max("id").longValue()+1);
        }catch (Exception e){
            // lỗi do chưa có data
            realm.beginTransaction();
            // tạo 1 bảng tạm thời
            RealmDb realmDb = realm.createObject(RealmDb.class,0);
            // set lại key auto 1 lần nữa
            RmDbKey = new AtomicLong(realm.where(RealmDb.class).max("id").longValue()+1);
            // xóa bảng tạm thời
            RealmResults<RealmDb> results = realm.where(RealmDb.class).equalTo("id",0).findAll();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        }
        try{
            DbRmKey = new AtomicLong(realm.where(DbRealm.class).max("id").longValue()+1);
        }catch (Exception e){
            // lỗi do chưa có data
            realm.beginTransaction();
            // tạo 1 bảng tạm thời
            DbRealm dbRealm = realm.createObject(DbRealm.class,0);
            // set lại key auto 1 lần nữa
            DbRmKey = new AtomicLong(realm.where(DbRealm.class).max("id").longValue()+1);
            // xóa bảng tạm thời
            RealmResults<DbRealm> results = realm.where(DbRealm.class).equalTo("id",0).findAll();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        }

    }
}
