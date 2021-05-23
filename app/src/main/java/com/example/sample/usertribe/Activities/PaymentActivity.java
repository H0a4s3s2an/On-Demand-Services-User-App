package com.example.sample.usertribe.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Model.History;
import com.example.sample.usertribe.Model.Review;
import com.example.sample.usertribe.Model.UserAccount;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {
    TextView tv_trip_total;
    ImageView iv_driver_photo;
    String Image;
    private String pay;
    private String name;
    private String username;
    private String uid;
    private String providerId;
    private float ratingcount;
    double Price;
    RatingBar rating_bar;
    EditText et_review_content;
    Button bt_submit_review;
    DatabaseReference databaseReference,userdb;
    public Context context=PaymentActivity.this;
    Review review=new Review();
    private static DecimalFormat REAL_FORMAT = new DecimalFormat("0.#");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        tv_trip_total=(TextView)findViewById(R.id.tv_trip_total);
        iv_driver_photo=(ImageView) findViewById(R.id.iv_driver_photo);
        rating_bar=(RatingBar)findViewById(R.id.rating_bar);
        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating>0)
                {
             ratingcount=ratingBar.getRating();
                }
            }
        });
        et_review_content=(EditText)findViewById(R.id.et_review_content);
        bt_submit_review=(Button)findViewById(R.id.bt_submit_review);
        bt_submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate();
            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference("Rating");
        userdb=FirebaseDatabase.getInstance().getReference("Users");
        userdb.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAccount userAccount=dataSnapshot.getValue(UserAccount.class);
                username=userAccount.getFname();
                uid=dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(getIntent()!=null)
        {
            providerId=getIntent().getStringExtra("providerId");
            Image=getIntent().getStringExtra("provider_image");
            pay=getIntent().getStringExtra("provider_price");
            name=getIntent().getStringExtra("provider_name");
        }
        Price= Double.valueOf((pay));
            if(Image!=null)
            {
                Picasso.get()
                        .load(Image)
                        .placeholder(R.drawable.ic_account)
                        .error(R.drawable.ic_dummy_user)
                        .into(iv_driver_photo);
            }
            else
            {
                Picasso.get()
                        .load(R.drawable.ic_dummy_user)
                        .placeholder(R.drawable.ic_account)
                        .error(R.drawable.ic_dummy_user)
                        .into(iv_driver_photo);
            }
            tv_trip_total.setText(REAL_FORMAT.format(Price));

    }

    private void Rate() {
        if(NetworkUtils.isNetworkAvailable(context))
        {
            if(!et_review_content.getText().toString().isEmpty() && rating_bar.getRating()>0)
            {
                CustomDialog customDialog=new CustomDialog(context);
                customDialog.show();
               customDialog.setCancelable(false);
               review.setComment(et_review_content.getText().toString());
               review.setUid(uid);
               review.setUsername(username);
               review.setRating(String.valueOf(ratingcount));
               String id=databaseReference.push().getKey();
               databaseReference.child(providerId).child(id).setValue(review);
               customDialog.dismiss();
            }
        }
        else {
            NetworkUtils.showNoNetworkDialog(context);
        }
        History();
        DashBoard();
    }

    private void History() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("History");
        History history=new History();
        history.setPrice(Price);
        history.setStatus("Status: Complete");
        history.setImage(Image);
        history.setTime(strDate);
        history.setUid(uid);
        String userId=databaseReference.push().getKey();
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userId).setValue(history);
    }

    private void DashBoard() {
        startActivity(new Intent(this,DashBoardActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
