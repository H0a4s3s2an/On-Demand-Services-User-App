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
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Helper.SharedHelper;
import com.example.sample.usertribe.Model.UserAccount;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileVisitActivity extends AppCompatActivity {
    ImageView img_profile;
    TextView f_name;
    TextView l_name;
    TextView mobile_no;
    TextView email;
    Button edit_profile_proceed;
    private static String TAG = "Profile";
    public Context context = ProfileVisitActivity.this;
    public Activity activity = ProfileVisitActivity.this;
    CustomDialog customDialog;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    AlertDialog alertDialog;
    NetworkUtils network;
    boolean isInternet;
    Handler handler;
    String fname,lname,mobile,mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_visit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        customDialog = new CustomDialog(context);
        customDialog.show();
        customDialog.setCancelable(false);
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        network = new NetworkUtils();
        isInternet = network.isNetworkAvailable(this);
        handler = new Handler();
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        img_profile = (ImageView) findViewById(R.id.img_profile);
        f_name = (TextView) findViewById(R.id.text_fname);
        l_name = (TextView) findViewById(R.id.text_lname);
        mobile_no = (TextView) findViewById(R.id.mobile_no);
        email = (TextView) findViewById(R.id.email);
        edit_profile_proceed = (Button) findViewById(R.id.edit_profile_proceed);
        edit_profile_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(context)) {
                    Intent intent=new Intent(ProfileVisitActivity.this,EditProfileActivity.class);
                    intent.putExtra("fname",fname);
                    intent.putExtra("lname",lname);
                    intent.putExtra("email",mail);
                    intent.putExtra("phone",mobile);
                    startActivity(intent);

                } else {
                    NetworkUtils.showNoNetworkDialog(context);
                }
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        if (!SharedHelper.getKey(context, "photo").equalsIgnoreCase("")
                && !SharedHelper.getKey(context, "photo").equalsIgnoreCase(null)
                && SharedHelper.getKey(context, "photo") != null) {
            Picasso.get()
                    .load(SharedHelper.getKey(context, "photo"))
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(img_profile);
        } else {
            Picasso.get()
                    .load(R.drawable.ic_dummy_user)
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(img_profile);
        }
        if (NetworkUtils.isNetworkAvailable(context)) {
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        fname=dataSnapshot1.child("fname").getValue().toString();
                        lname=dataSnapshot1.child("lname").getValue().toString();
                        mobile=dataSnapshot1.child("mobile").getValue().toString();
                        mail=dataSnapshot1.child("email").getValue().toString();
                        f_name.setText(dataSnapshot1.child("fname").getValue().toString());
                        l_name.setText(dataSnapshot1.child("lname").getValue().toString());
                        mobile_no.setText(dataSnapshot1.child("mobile").getValue().toString());
                        email.setText(dataSnapshot1.child("email").getValue().toString());
                    }
                    customDialog.hide();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            customDialog.hide();
            NetworkUtils.showNoNetworkDialog(context);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
