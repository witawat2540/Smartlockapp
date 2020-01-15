package com.xe.witawatd.smartlock;

import android.content.Context;
import android.os.AsyncTask;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;



public class SelectAlldata extends AsyncTask<Void, Void, String> {
private Context context ;
private static final String UrlGetdata = "http://iot.rmu.ac.th/iot/Smartlock/Select_Door.php";
private String IMEI;

    public SelectAlldata(Context context,String IMEI) {
        this.context = context;
        this.IMEI = IMEI;


    }
    @Override
    protected String doInBackground(Void... voids) {

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url("http://iot.rmu.ac.th/iot/Smartlock/Select_Door.php?IMEI="+IMEI).build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

}
