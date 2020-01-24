package com.xe.witawatd.smartlock;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.device.DeviceName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //private TextView textView;
    private ImageButton btnimQR;
    private Button Btninfo;
    private Spinner dorspinner;
    String Datadoor ;
    //private Button btnQR ;
    private String comment ;
    private TextView textView2;
    private List<String> items;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private ArrayList<String> door = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadIMEI();

    }
    private void init() {
        dorspinner = (Spinner) findViewById(R.id.dorspinner);
        btnimQR = (ImageButton)findViewById(R.id.btnimQR);

    }
    private void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            doPermissionGrantedStuffs();

        }

    }
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Request")
                    .setMessage(getString(R.string.permission_read_phone_state_rationale))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .setIcon(R.drawable.onlinlinew_warning_sign)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                //alertAlert(getString(R.string.permision_available_read_phone_state));
                doPermissionGrantedStuffs();
            } else {
                alertAlert(getString(R.string.permissions_not_granted_read_phone_state));
            }
        }
    }
    private void alertAlert(String msg) {
        new AlertDialog.Builder(MainActivity.this).setTitle("Permission Request").setMessage(msg).setCancelable(false).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do somthing here
            }
        }).setIcon(R.drawable.onlinlinew_warning_sign).show();
    }
    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        final TelephonyManager tm =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
        final String IMEI_phone=tm.getDeviceId();



        Btninfo = (Button)findViewById(R.id.btninfo);
        final String deviceName = DeviceName.getDeviceName();
        Btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this,Mainnifo.class);
                intent2.putExtra("deviceName",deviceName);
                intent2.putExtra("imei",IMEI_phone);
                startActivity(intent2);
            }
        });

        try {
            //textView2 = (TextView)findViewById(R.id.textView2);

            //textView2.setText(deviceName);
            SelectAlldata selectAlldata = new SelectAlldata(MainActivity.this,IMEI_phone,deviceName);
            selectAlldata.execute();
            String data = selectAlldata.get();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray result = jsonObject.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                comment = collegeData.getString("Door");
                door.add(comment);
            }

        } catch (Exception e) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Waring");
            builder.setMessage("Unable to connect to the internet");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            });
            builder.show();
            e.printStackTrace();


        }

        ArrayAdapter<String> adapterThai = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , door);
        adapterThai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dorspinner.setAdapter(adapterThai);
        dorspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Datadoor = door.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnimQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Datadoor==null){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Waring");
                    builder.setMessage("Please contact the staff to receive the code.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                    builder.show();
                    return;

                }
                //String imei=tm.getDeviceId();
                Intent intent = new Intent(MainActivity.this,QRCode_activity.class);
                intent.putExtra("Door",Datadoor);
                intent.putExtra("imei",IMEI_phone);
                startActivity(intent);
                finish();
            }
        });

    }

}
