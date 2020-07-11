package com.example.apprealm.Room;

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
import androidx.room.Room;

import com.example.apprealm.R;

import java.util.ArrayList;
import java.util.List;

public class roomActivity extends AppCompatActivity {
    AppDatabase adb;
    ListView lv;
    Button btnRf, btnInsert, btnUpdate;
    EditText edtName,edtPhone,edtID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        setTitle("RoomDatabase");
        anhXa();
        adb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user.db").allowMainThreadQueries().build();
        // get data
        List<User> users = adb.userDao().getAll();
        if (users.size()==0){
            addData();
        }
        addLv();
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = false;
                if (edtID.getText().length()>0){
                    User user = adb.userDao().findUser(Integer.parseInt(edtID.getText().toString()));
                    try{
                        int a = user.ID;
                        check = true;
                    }catch(Exception e){

                    }
                    if (check==true){
                        Toast.makeText(roomActivity.this, "ID đã được sử dụng !", Toast.LENGTH_SHORT).show();
                    }else{
                        if (edtName.getText().length()==0 || edtPhone.getText().length()==0){
                            Toast.makeText(roomActivity.this, "Nhap du du lieu!", Toast.LENGTH_SHORT).show();
                        }else{
                            User userIs = new User();
                            userIs.ID = Integer.parseInt(edtID.getText().toString());
                            userIs.name = edtName.getText().toString();
                            userIs.phone = edtPhone.getText().toString();
                            long[] result = adb.userDao().insertAll(userIs);
                            if (result[0] == 0){
                                Toast.makeText(roomActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(roomActivity.this, "Success data!", Toast.LENGTH_SHORT).show();
                                addLv();
                            }
                        }
                    }
                }else {
                    Toast.makeText(roomActivity.this, "Nhap ID!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = false;
                if (edtID.getText().length()>0 || edtName.getText().length()==0 || edtPhone.getText().length()==0){
                    User users = adb.userDao().findUser(Integer.parseInt(edtID.getText().toString()));
                    try{
                        int a = users.ID;
                        check = true;
                    }catch(Exception e){
                        Toast.makeText(roomActivity.this, "Khong co ID nay!!!", Toast.LENGTH_SHORT).show();
                    }
                    if (check==true){
                        User userUp = new User();
                        userUp.ID = Integer.parseInt(edtID.getText().toString());
                        userUp.name = edtName.getText().toString();
                        userUp.phone = edtPhone.getText().toString();
                        int a = adb.userDao().update(userUp);
                        if (a == 0){
                            Toast.makeText(roomActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(roomActivity.this, "Success data!", Toast.LENGTH_SHORT).show();
                            addLv();
                        }
                    }
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<User> users = adb.userDao().getAll();
                edtID.setText(String.valueOf(users.get(position).ID), TextView.BufferType.EDITABLE);
                edtPhone.setText(String.valueOf(users.get(position).phone), TextView.BufferType.EDITABLE);
                edtName.setText(users.get(position).name);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                List<User> users = adb.userDao().getAll();
                User user = users.get(position);
                int dl = adb.userDao().delete(user);
                if (dl == 0){
                    Toast.makeText(roomActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(roomActivity.this, "Success delete!", Toast.LENGTH_SHORT).show();
                    addLv();
                }
                return true;
            }
        });
    }
    private void anhXa() {
        lv = findViewById(R.id.lv1);
        btnRf = findViewById(R.id.btnRefresh);
        edtName = findViewById(R.id.edtName);
        edtID = findViewById(R.id.edtID);
        edtPhone = findViewById(R.id.edtPhone);
        btnInsert = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void addLv() {
        List<User> users = adb.userDao().getAll();
        final ArrayList<String> arr = new ArrayList<>();
        for (int i=0;i<users.size();i++){
            arr.add(users.get(i).ID+"\n   "+users.get(i).name+"\n   "+users.get(i).phone);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(roomActivity.this, android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(arrayAdapter);
    }

    private void addData() {
        // add data
        User user = new User();
        user.ID = 1;
        user.name = "Duong";
        user.phone = "098272887";

        long[] result = adb.userDao().insertAll(user);
        if (result[0] == 0){
            Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Success data!", Toast.LENGTH_SHORT).show();
        }
    }
}