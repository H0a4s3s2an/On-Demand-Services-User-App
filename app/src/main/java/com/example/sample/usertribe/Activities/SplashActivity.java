package com.example.sample.usertribe.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.sample.usertribe.Helper.SharedHelper;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class SplashActivity extends AppCompatActivity {
    public  static final int SPLASH_TIME_OUT=5000;
    public Activity activity=SplashActivity.this;
    public Context context=SplashActivity.this;
    NetworkUtils network;
    boolean isInternet;
    Handler handler;
    AlertDialog alertDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        firebaseAuth=FirebaseAuth.getInstance();
        network=new NetworkUtils();
        isInternet=network.isNetworkAvailable(this);
        handler=new Handler();
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(network.isNetworkAvailable(context)) {
                    if(firebaseAuth.getCurrentUser()!=null)
                    {
                        getProfile();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(),SliderActivity.class));
                        finish();
                    }
                    if(alertDialog!=null && alertDialog.isShowing())
                    {
                        alertDialog.dismiss();
                    }
                }
                else
                {
                    showDialog();
                    handler.postDelayed(this,3000);
                }
            }
        },5000);
    }
    private void getProfile() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String fname = datas.child("fname").getValue().toString();
                    String lname = datas.child("lname").getValue().toString();
                    String email = datas.child("email").getValue().toString();
                    String photo=datas.child("picture").getValue().toString();
                    String phone = datas.child("mobile").getValue().toString();
                    SharedHelper.putKey(context,"fname",fname);
                    SharedHelper.putKey(context,"lname",lname);
                    SharedHelper.putKey(context,"email",email);
                    SharedHelper.putKey(context,"phone",phone);
                    SharedHelper.putKey(context,"photo",photo);
                }
                startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("",databaseError.toString());
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to Internet")
                .setCancelable(false)
                .setPositiveButton("Connect to WiFi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();
                        finish();
                    }
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}