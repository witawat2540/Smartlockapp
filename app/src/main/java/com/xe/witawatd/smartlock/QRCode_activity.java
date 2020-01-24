package com.xe.witawatd.smartlock;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class QRCode_activity extends AppCompatActivity {
    Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask timetask;
    private ImageView imageQR;
    private final long startTime = 240000;  // 1000 = 1 second
    private final long interval = 1000;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap ;
    private TextView textView,txtime;
    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz|!£$%&/=@#";
    public static Random RANDOM = new Random();
    private String ramdom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_activity);
        imageQR= (ImageView) findViewById(R.id.imageQR);
        textView = (TextView)findViewById(R.id.textView3);
        txtime = (TextView)findViewById(R.id.txtime);
        final MyCountDown countdown = new MyCountDown(startTime,interval);
        countdown.start();
        String Door = getIntent().getStringExtra("Door");
        String Imei = getIntent().getStringExtra("imei");
        ramdom = randomString(6);
        String data= Door+Imei+ramdom;
        String encode = SHA(data);
        textView.setText("Door:"+Door);
        AddIMEI addIMEI = new AddIMEI(QRCode_activity.this,Door,encode,Imei);
        addIMEI.execute();



        try {
            bitmap = TextToImageEncode(encode);

            imageQR.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(QRCode_activity.this);
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
        }


    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
    public String SHA(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-384");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public class MyCountDown extends CountDownTimer {
        public MyCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub

            finish();
        }

        @Override
        public void onTick(long remain) {
            // TODO Auto-generated method stub

            int timeRemain = (int) (remain) ;
            int hours = (int) (timeRemain / 1000) / 3600;
            int minutes = (int) ((timeRemain / 1000) % 3600) / 60;
            int seconds = (int) (timeRemain / 1000) % 60;


            txtime.setText("Please use at the time:"+minutes+":"+seconds);
        }

    }
    
}
