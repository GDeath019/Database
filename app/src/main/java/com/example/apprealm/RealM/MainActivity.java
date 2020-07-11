package com.example.apprealm.RealM;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apprealm.R;
import com.example.apprealm.Room.roomActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private String DbName = "FirstDb.realm";
    public static AtomicLong DbRmKey;
    ListView lv;
    Realm getData;
    Button btnRf, btnInsert, btnUpdate;
    EditText edtName,edtAge,edtUnv;
    FloatingActionButton fab;
    Long ID;
    List<DbRealm> allFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("RealM");
        initRealM();
        anhXa();
        getData = Realm.getDefaultInstance();
        List<DbRealm> dbRealmList = getAll(getData);
        allFunction = getAll(getData);
        if (dbRealmList.size()==0){
            addData();
        }else{
            DbRmKey = new AtomicLong(dbRealmList.get(dbRealmList.size()-1).getId()+1);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ID = allFunction.get(position).getId();
                edtName.setText(allFunction.get(position).getName());
                edtAge.setText(String.valueOf(allFunction.get(position).getAge()), TextView.BufferType.EDITABLE);
                edtUnv.setText(allFunction.get(position).getUniversity());
            }
        });
        btnRf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLv();
                edtUnv.setText("");
                edtAge.setText("");
                edtName.setText("");
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtName.getText().length()==0 || edtUnv.getText().length()==0 || edtAge.getText().length()==0){
                    Toast.makeText(MainActivity.this, "Mời nhập đủ dữ liệu!!", Toast.LENGTH_SHORT).show();
                }else {
                    final Realm insertRm = Realm.getDefaultInstance();
                    final long DbKey = DbRmKey.getAndIncrement()+1;
                    insertRm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm backgroundRm) {
                            DbRealm dbRealm = backgroundRm.createObject(DbRealm.class, DbKey);
                            dbRealm.setName(edtName.getText().toString());
                            dbRealm.setAge(Integer.parseInt(edtAge.getText().toString()));
                            dbRealm.setUniversity(edtUnv.getText().toString());
                        }
                    });
                    insertRm.close();
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                final Long tg = ID;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if(edtName.getText().length()==0 || edtUnv.getText().length()==0 || edtAge.getText().length()==0){
                            Toast.makeText(MainActivity.this, "Mời nhập đủ dữ liệu!!", Toast.LENGTH_SHORT).show();
                        }else {
                            DbRealm dbRealm = realm.where(DbRealm.class).equalTo("id", ID).findFirst();
                            dbRealm.setName(edtName.getText().toString());
                            dbRealm.setAge(Integer.parseInt(edtAge.getText().toString()));
                            dbRealm.setUniversity(edtUnv.getText().toString());
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, roomActivity.class);
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                List<DbRealm> dbDelete = getAll(getData);
                Long DlId = dbDelete.get(position).getId();
                DeleteData(DlId);
                return true;
            }
        });
    }
    public void DeleteData(Long DeleteId){
//        Toast.makeText(this, "??????????", Toast.LENGTH_SHORT).show();
        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        RealmResults<RealmDb> results = realm.where(RealmDb.class).equalTo("id",DeleteId).findAll();
//        results.deleteFirstFromRealm();
//        realm.commitTransaction();
//        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        final Long dlt = DeleteId;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm1) {
                RealmResults<DbRealm> results = realm1.where(DbRealm.class).equalTo("id",dlt).findAll();
                results.deleteFirstFromRealm();
            }
        });
    }


    private void anhXa() {
        lv = findViewById(R.id.lv1);
        btnRf = findViewById(R.id.btnRefresh);
        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        edtUnv = findViewById(R.id.edtUnv);
        btnInsert = findViewById(R.id.btnAdd);
        fab = findViewById(R.id.fabRoom);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void addLv() {
        List<DbRealm> dbRealmList = getAll(getData);
        final ArrayList<String> arr = new ArrayList<>();
        for (int i=0;i<dbRealmList.size();i++){
            arr.add(dbRealmList.get(i).getId()+"\n   "+dbRealmList.get(i).getName()+"\n   "+dbRealmList.get(i).getAge()+"\n   "+dbRealmList.get(i).getUniversity());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(arrayAdapter);
    }

    public void addData(){
        final Realm insertRm = Realm.getDefaultInstance();
        newKey();
        final long DbKey = DbRmKey.getAndIncrement();
        // them data vao phai dinh kem trong 1 transaction
        insertRm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRm) {
                DbRealm dbRealm = backgroundRm.createObject(DbRealm.class, DbKey);
                dbRealm.setName("Đỗ Văn Dương");
                dbRealm.setAge(21);
                dbRealm.setUniversity("DHGTVT");
            }
        });
        insertRm.close();
    }
    public DbRealm CreateOrGet(String name, Realm insertRm){
        DbRealm dbRealm = insertRm.where(DbRealm.class).equalTo("Name",name).findFirst();
        if (dbRealm!=null){
            return dbRealm;
        }else {
            final long id = ExApp.DbRmKey.getAndIncrement();
            DbRealm dbRealm1 = insertRm.createObject(DbRealm.class, id);
            dbRealm1.setName(name);
            return dbRealm1;
        }
    }
    public List<DbRealm> getAll(Realm passedInRealm){
        RealmResults<DbRealm> realms = passedInRealm.where(DbRealm.class).findAll();
        return realms;
    }
    public List<DbRealm> getFilter(Realm passedInRealm){
        // tuổi lớn hơn 20
        RealmResults<DbRealm> realms = passedInRealm.where(DbRealm.class).greaterThan("Age", 20).findAll();
        // nhỏ hơn
        RealmResults<DbRealm> realms1 = passedInRealm.where(DbRealm.class).lessThan("Age", 20).findAll();
        //sorter
//        RealmResults<DbRealm> realms3 = passedInRealm.where(DbRealm.class).findAllSorted("Name", false);
        return realms;
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
//      Realm realm = Realm.getInstance(configuration);

    }
    public void newKey(){
        Realm realm = Realm.getDefaultInstance();
        try{
            List<DbRealm> dbRealmList = getAll(realm);
            DbRmKey = new AtomicLong(dbRealmList.get(dbRealmList.size()-1).getId()+1);
        }catch (Exception e){
            // lỗi do chưa có data
            realm.beginTransaction();
            // tạo 1 bảng tạm thời
            DbRealm dbRealm = realm.createObject(DbRealm.class,0);
            // set lại key auto 1 lần nữa
            List<DbRealm> dbRealmList = getAll(realm);
            DbRmKey = new AtomicLong(dbRealmList.get(dbRealmList.size()-1).getId()+1);
            // xóa bảng tạm thời
            RealmResults<DbRealm> results = realm.where(DbRealm.class).equalTo("id",0).findAll();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        }
        realm.close();
    }
}