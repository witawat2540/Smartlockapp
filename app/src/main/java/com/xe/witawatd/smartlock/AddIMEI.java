package com.xe.witawatd.smartlock;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by BILLY-PC on 06/02/60.
 */

public class AddIMEI extends AsyncTask<Void, Void, String> {
    private Context context;
    private static final String stringUrl = "http://iot.rmu.ac.th/iot/Smartlock/AddEMEI.php";
    private String Door,Encode,IMEI;

    public AddIMEI(Context context,
                   String door,
                   String encode,
                   String imei
                   ) {
        this.context = context;
        Door = door;
        Encode = encode;
        IMEI = imei;

    }

    @Override
    protected String doInBackground(Void... voids) {
            //add data to mysql
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isAdd", "true")
                    .add("name", Door)
                    .add("pass", Encode)
                    .add("IMEI", IMEI)
                    .build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(stringUrl).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();


            Log.d("myTag", response.body().string());


            return response.body().string();





        } catch (Exception e) {


            e.printStackTrace();
            return null;
        }


    }
}
