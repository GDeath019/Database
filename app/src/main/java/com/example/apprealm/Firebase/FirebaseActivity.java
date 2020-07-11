package com.example.apprealm.Firebase;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apprealm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseActivity extends AppCompatActivity {
    private static final String TAG = FirebaseActivity.class.getSimpleName();
    EditText edtName,edtPhone,edtID;
    Button btnInsert, btnUpdate, btnLoad;
    ListView lv;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    ArrayAdapter arrayAdapter;
    ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        anhXa();
        arr = new ArrayList<>();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String id = edtID.getText().toString();
                // Check for already existed userId
                if (name.length()==0 || phone.length()==0 || id.length() == 0) {
                    Toast.makeText(FirebaseActivity.this, "Nhap du du lieu!!", Toast.LENGTH_SHORT).show();
                }else{
                    createUser(id, name, phone);
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String id = edtID.getText().toString();
                // Check for already existed userId
                if (name.length()==0 || phone.length()==0 || id.length() == 0) {
                    Toast.makeText(FirebaseActivity.this, "Nhap du du lieu!!", Toast.LENGTH_SHORT).show();
                }else{
                    updateUser(id, name, phone);
                }
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               load();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int i = position;
                DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("users");
                df.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j=0;
                        Map<String, Object> temp =(Map<String, Object>)  dataSnapshot.getValue();
                        for (Map.Entry<String, Object> entry : temp.entrySet()) {
                            Map singleUs = (Map) entry.getValue();
                            edtID.setText(entry.getKey());
                            edtName.setText(singleUs.get("name").toString());
                            edtPhone.setText(singleUs.get("mobile").toString(), TextView.BufferType.EDITABLE);
                            if (i == j){ break;}
                            j++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int i = position;
                DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("users");
                df.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j=0;
                        Map<String, Object> temp =(Map<String, Object>)  dataSnapshot.getValue();
                        for (Map.Entry<String, Object> entry : temp.entrySet()) {
                            Map singleUs = (Map) entry.getValue();
                            if (i == j){
                                mFirebaseDatabase.child(entry.getKey()).removeValue();
                                load();
                                break;
                            }
                            j++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            }
        });
    }


    //end OnCreate


    public void load(){
        DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("users");
        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collectData((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        edtName.setText("");
        edtID.setText("");
        edtPhone.setText("", TextView.BufferType.EDITABLE);
    }
    private void anhXa() {
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtID = findViewById(R.id.edtID);
        btnInsert = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnLoad = findViewById(R.id.btnRefresh);
        lv = findViewById(R.id.lv1);
    }

    private void collectData(Map<String, Object> users){
        arr.clear();
        if(users == null){
            arr.add("Database Null.");
        }else {
            for (Map.Entry<String, Object> entry : users.entrySet()) {
                Map singleUs = (Map) entry.getValue();
                arr.add(singleUs.get("mobile") + "     " + singleUs.get("name"));
            }
        }
        arrayAdapter = new ArrayAdapter(FirebaseActivity.this, android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(arrayAdapter);
    }

    private void createUser(String id, String name, String phone) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        final String[] ID = {id};
        final String NAME = name;
        final String PHONE = phone;
        DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("users");
        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> temp =(Map<String, Object>)  dataSnapshot.getValue();
                if(temp == null){
//                    ID[0] = mFirebaseDatabase.push().getKey();
                    User user = new User(NAME, PHONE);

                    mFirebaseDatabase.child(ID[0]).setValue(user);
                    Toast.makeText(FirebaseActivity.this, "Insert Success!", Toast.LENGTH_SHORT).show();
                    addUserChangeListener(ID[0]);
                }else {
                    boolean check=true;
                    for (Map.Entry<String, Object> entry : temp.entrySet()) {
                        if (entry.getKey().equals(ID[0])){
                            check = false;
                            break;
                        }
                    }
                    if (check){
//                        ID[0] = mFirebaseDatabase.push().getKey();
                        User user = new User(NAME, PHONE);

                        mFirebaseDatabase.child(ID[0]).setValue(user);
                        Toast.makeText(FirebaseActivity.this, "Insert Success!", Toast.LENGTH_SHORT).show();
                        addUserChangeListener(ID[0]);
                    }else {
                        Toast.makeText(FirebaseActivity.this, "Đã có ID này!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateUser(String id, String name, String phone) {
        final String[] ID = {id};
        final String NAME = name;
        final String PHONE = phone;
        DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("users");
        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> temp =(Map<String, Object>)  dataSnapshot.getValue();
                if(temp == null){
                    Toast.makeText(FirebaseActivity.this, "Database Null!", Toast.LENGTH_SHORT).show();
                }else {
                    boolean check=false;
                    for (Map.Entry<String, Object> entry : temp.entrySet()) {
                        if (entry.getKey().equals(ID[0])){
                            check = true;
                            break;
                        }
                    }
                    if (check){
                        if (!TextUtils.isEmpty(NAME))
                            mFirebaseDatabase.child(ID[0]).child("name").setValue(NAME);

                        if (!TextUtils.isEmpty(PHONE))
                            mFirebaseDatabase.child(ID[0]).child("mobile").setValue(PHONE);
                        Toast.makeText(FirebaseActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();
                        load();
//                        addUserChangeListener(ID[0]);
                    }else {
                        Toast.makeText(FirebaseActivity.this, "Không có ID này!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addUserChangeListener(String id) {
        // User data change listener
        mFirebaseDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name + ", " + user.mobile);

                // Display newly updated name and email
                arr.add(user.mobile + "   " + user.name);
                arrayAdapter = new ArrayAdapter(FirebaseActivity.this, android.R.layout.simple_list_item_1, arr);
                lv.setAdapter(arrayAdapter);
//                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
}