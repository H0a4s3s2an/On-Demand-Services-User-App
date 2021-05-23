package com.example.sample.usertribe.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Helper.SharedHelper;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.example.sample.usertribe.utils.Utilities;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    public Context context=LoginActivity.this;
    public Activity activity=LoginActivity.this;
    private boolean fromActivity=false;
    NetworkUtils helper;
    boolean isInternet;
    private EditText login_email, login_password;
    private Button login_button;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String email;
    String password;
    TextView fog_password;
    Utilities utils = new Utilities();
    CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");
        helper=new NetworkUtils();
        isInternet=helper.isNetworkAvailable(context);
        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        login_button = (Button) findViewById(R.id.login_button);
        fog_password = (TextView) findViewById(R.id.fog_password);
        customDialog = new CustomDialog(this);
        fog_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login_email.getText().toString().equals("") || login_email.getText().toString().equalsIgnoreCase("name@example.com")) {
                    displayMessage("Enter your Email ID.");
                } else if (!Utilities.isValidEmail(login_email.getText().toString())) {
                    displayMessage("Email is not valid");
                } else if (login_password.getText().toString().equals("") || login_password.getText().toString().equalsIgnoreCase("******")) {
                    displayMessage("Password could not be Empty");
                } else if (login_password.length() < 6) {
                    displayMessage("password must be atleast 6 number");
                } else {
                    if (isInternet) {
                        customDialog.show();
                        customDialog.setCancelable(false);
                        firebaseAuth.signInWithEmailAndPassword(login_email.getText().toString(), login_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                customDialog.dismiss();
                                getProfile();
                                Intent intent = new Intent(LoginActivity.this,DashBoardActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customDialog.dismiss();
                                displayMessage("Check your Email and password");
                            }
                        });
                    } else {
                        displayMessage("Something went wrong");
                    }
                }
            }
        });
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

    private void resetPassword() {
        startActivity(new Intent(this, PasswordResetActivity.class));
    }
//

    public void Signup(View view) {
        Intent intent=new Intent(LoginActivity.this, AccountKitActivity.class);
        startActivity(intent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (fromActivity) {
            Intent mainIntent = new Intent(LoginActivity.this, FlatActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            LoginActivity.this.finish();
        } else {
            Intent mainIntent = new Intent(LoginActivity.this, FlatActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            LoginActivity.this.finish();
        }
    }
}
