package com.example.sample.usertribe.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Helper.SharedHelper;
import com.example.sample.usertribe.Model.UserAccount;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.example.sample.usertribe.utils.Utilities;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "RegisterActivity";
    public Context context = RegisterActivity.this;
    public Activity activity = RegisterActivity.this;
    private EditText first_name, last_name, register_email, register_password;
    NetworkUtils helper;
    Boolean isInternet;
    private String blockCharacterSet = "~#^|$%&*!()_-*.,@/";
    private Button proceed;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    SpotsDialog dialog;
    String firstname, lastname, r_email, pass, phone;
    UserAccount userAccount=new UserAccount();
    CustomDialog customDialog;
    private boolean fromActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        if (Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        proceed = (Button) findViewById(R.id.proceed);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        register_email = (EditText) findViewById(R.id.register_email);
        register_password = (EditText) findViewById(R.id.register_password);
        helper = new NetworkUtils();
        isInternet = helper.isNetworkAvailable(context);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup();
            }
        });
//        if(AccountKit.getCurrentAccessToken()!=null)
//        {
//            dialog=new SpotsDialog(this);
//            dialog.show();
//            dialog.setMessage("Loading...");
//            dialog.setCancelable(false);
//            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                @Override
//                public void onSuccess(Account account) {
//                    dbreference.child(account.getId())
//                            .setValue(userAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            startActivity(new Intent(RegisterActivity.this,DashBoardActivity.class));
//                            dialog.dismiss();
//                            finish();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onError(AccountKitError accountKitError) {
//
//                }
//            });
//        }
    }

    private void Signup() {
        Pattern ps = Pattern.compile(".*[0-9].*");
        Matcher firstName = ps.matcher(first_name.getText().toString());
        Matcher lastName = ps.matcher(last_name.getText().toString());

        if (register_email.getText().toString().equals("") || register_email.getText().toString().equalsIgnoreCase("name@example.com")) {
            displayMessage("Enter a valid Email.");
        } else if (!Utilities.isValidEmail(register_email.getText().toString())) {
            displayMessage("Not a valid Email");
        } else if (first_name.getText().toString().equals("") || first_name.getText().toString().equalsIgnoreCase("First Name")) {
            displayMessage("First name required.");
        } else if (firstName.matches()) {
            displayMessage("First name do not accept numbers");
        } else if (last_name.getText().toString().equals("") || last_name.getText().toString().equalsIgnoreCase("Last Name")) {
            displayMessage("Last name required.");
        } else if (lastName.matches()) {
            displayMessage("Last name do not accept numbers.");
        } else if (register_password.getText().toString().equals("") || register_password.getText().toString().equalsIgnoreCase("******")) {
            displayMessage("Password could not be empty");
        } else if (register_password.length() < 6) {
            displayMessage("password size must be at least 6 numbers.");
        } else {
            if (isInternet) {
                customDialog = new CustomDialog(context);
                customDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(register_email.getText().toString(), register_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        customDialog.dismiss();
                        userAccount.setMobile(getIntent().getStringExtra("number"));
                        userAccount.setEmail(register_email.getText().toString());
                        userAccount.setFname(first_name.getText().toString());
                         userAccount.setLname(last_name.getText().toString());
                        userAccount.setPicture("");
                        String user_id = databaseReference.push().getKey();
                        userAccount.setUid(user_id);
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child(user_id).setValue(userAccount);
                        getProfile();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayMessage("something went wrong");
                        customDialog.dismiss();
                    }
                });
            } else {
                displayMessage("something went wrong");
                customDialog.dismiss();
            }
        }
    }

    private void getProfile() {
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                   String fname = datas.child("fname").getValue().toString();
                   String lname = datas.child("lname").getValue().toString();
                   String mail = datas.child("email").getValue().toString();
                   String photo=datas.child("picture").getValue().toString();
                   String phone = datas.child("mobile").getValue().toString();
                   String id=datas.child("uid").getValue().toString();
                    if(fname.equals("") || lname.equals("") || mail.equals("")  || phone.equals(""))
                    {
                        startActivity(new Intent(RegisterActivity.this,FlatActivity.class));
                    }
                    else
                    {
                        SharedHelper.putKey(context,"id",id);
                        SharedHelper.putKey(context,"fname",fname);
                        SharedHelper.putKey(context,"lname",lname);
                        SharedHelper.putKey(context,"email",mail);
                        SharedHelper.putKey(context,"photo",photo);
                        SharedHelper.putKey(context,"phone",phone);
                        goToDashBoardActivity();
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("",databaseError.toString());
            }
        });
    }

    private void goToDashBoardActivity() {
        if (customDialog != null && customDialog.isShowing())
            customDialog.dismiss();
        Intent mainIntent = new Intent(RegisterActivity.this,DashBoardActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        RegisterActivity.this.finish();
    }

    public void SignIn(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void displayMessage(String toastString) {
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
            Intent mainIntent = new Intent(RegisterActivity.this, FlatActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            RegisterActivity.this.finish();
        } else {
            Intent mainIntent = new Intent(RegisterActivity.this, FlatActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            RegisterActivity.this.finish();
        }
    }
}

