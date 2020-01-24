package com.xe.witawatd.smartlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Mainnifo extends AppCompatActivity {
    private TextView tximei,txdevice,txnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnifo);
        tximei =(TextView)findViewById(R.id.tx_imei);
        txdevice =(TextView)findViewById(R.id.tx_device);


        String imei = getIntent().getStringExtra("imei");
        String namephone = getIntent().getStringExtra("deviceName");

        tximei.setText("IMEI: "+imei);
        txdevice.setText("deviceName: "+namephone);

    }
}
