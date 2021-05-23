package com.example.sample.usertribe.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;

public class PaymentModeActivity extends AppCompatActivity {
    String id="";
    String title;
    String category_id="";
    String category_title;
    String value;
    Button next;
    public final Context context=PaymentModeActivity.this;
    RadioGroup radio;
    RadioButton radio_pirates;
    RadioButton radio_ninjas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment Mode");
        radio = (RadioGroup)findViewById(R.id.radio);
        radio_pirates = (RadioButton)findViewById(R.id.radio_pirates);
        radio_ninjas = (RadioButton)findViewById(R.id.radio_ninjas);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                  switch (checkedId){
                      case R.id.radio_ninjas:
                          next.setVisibility(View.GONE);
                          break;
                      case R.id.radio_pirates:
                          next.setVisibility(View.VISIBLE);
                  }
            }
        });
        next=(Button)findViewById(R.id.next);
        if(getIntent()!=null)
        {
             value=getIntent().getStringExtra("detail_name");
             id=getIntent().getStringExtra("id");
             title=getIntent().getStringExtra("subcategory_name");
             category_title=getIntent().getStringExtra("category_name");
             category_id=getIntent().getStringExtra("category_id");
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    private void next() {
        if(NetworkUtils.isNetworkAvailable(context))
        {
            Intent intent=new Intent(PaymentModeActivity.this,ServiceProMapActivity.class);
            intent.putExtra("detail_name",value);
            intent.putExtra("subcategory_id",id);
            intent.putExtra("subcategory_name",title);
            intent.putExtra("category_name",category_title);
            intent.putExtra("category_id",category_id);
            startActivity(intent);
        }else
        {
            NetworkUtils.showNoNetworkDialog(context);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
