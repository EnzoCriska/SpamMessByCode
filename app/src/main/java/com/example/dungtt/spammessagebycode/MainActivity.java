package com.example.dungtt.spammessagebycode;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ListenerEvent {
    EditText inputAdd;
    Button btnAdd;
    RecyclerView spamRecycler;
    AdapterRecyclerListSpam listSpamAdapter;
    SQLiteHandle sqLiteHandle;
    SMSReceiver smsReceiver = new SMSReceiver();
    ArrayList<ContactSpam> spamNumbers = new ArrayList<>();
    private static final String TAG = "MainActivity" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        checkPermission();
        registerBroadcast();
    }

    public void init(){
        sqLiteHandle = new SQLiteHandle(this);
        spamNumbers = sqLiteHandle.getListSpamNum();
        inputAdd = findViewById(R.id.newSpamNumIn);
        btnAdd = findViewById(R.id.addBtn);
        spamRecycler = findViewById(R.id.recycleSpam);

        btnAdd.setOnClickListener(this);
        }

    public void recyclerUp(ArrayList list){
        listSpamAdapter = new AdapterRecyclerListSpam(list);
        listSpamAdapter.setListenerEvent(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        spamRecycler.setLayoutManager(layoutManager);
        spamRecycler.setAdapter(listSpamAdapter);
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            Log.e(TAG, "onCreate: "+ Build.VERSION.SDK_INT + " " + Build.VERSION.CODENAME );
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS)) {

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.RECEIVE_SMS},
                            1);
                }
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_SMS)) {

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            1);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addBtn:
                String input = String.valueOf(inputAdd.getText());
                sqLiteHandle.addSpamNumber(new ContactSpam(input, 1));
                spamNumbers = sqLiteHandle.getListSpamNum();
                recyclerUp(spamNumbers);
                Log.w("Event", "Add new spam " + input);
                Toast.makeText(this, "Added new spam number", Toast.LENGTH_SHORT).show();
                inputAdd.setText("");
                break;
        }
    }

    @Override
    public void onLongClickItem(int posision) {
        Log.w("Event", "Remove spam "+ spamNumbers.get(posision));
        sqLiteHandle.deletedSpam(spamNumbers.get(posision));
        spamNumbers = sqLiteHandle.getListSpamNum();
        Toast.makeText(this, "On long click remove", Toast.LENGTH_SHORT).show();
        recyclerUp(spamNumbers);
    }

    public void registerBroadcast(){
        this.registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        Log.w("Register", "RegisterBroadcast");
    }

    public void unRegisterBroadcast(){
        this.unregisterReceiver(smsReceiver);
        Log.w("Register", "RegisterBroadcast");
    }
}
