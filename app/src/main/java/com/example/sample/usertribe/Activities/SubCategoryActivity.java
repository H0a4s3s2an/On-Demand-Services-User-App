package com.example.sample.usertribe.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Helper.SharedHelper;
import com.example.sample.usertribe.Helper.SubCategoryAdapter;
import com.example.sample.usertribe.Interface.ItemClickListener;
import com.example.sample.usertribe.Model.Category;
import com.example.sample.usertribe.Model.SubCategory;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SubCategoryActivity extends AppCompatActivity {
    RecyclerView mrecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    RecyclerView.LayoutManager layoutManager;
    String id="";
    String title;
    TextView subtitle;
    FirebaseRecyclerAdapter<SubCategory,SubCategoryAdapter> firebaseRecyclerAdapter;
    public Activity activity=SubCategoryActivity.this;
    public Context context=SubCategoryActivity.this;
    NetworkUtils network;
    Handler handler;
    CustomDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Service");
        mrecyclerView = (RecyclerView) findViewById(R.id.sublist);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("SubCategory");
        subtitle = (TextView) findViewById(R.id.sub_title);
        network = new NetworkUtils();
        customDialog=new CustomDialog(context);
//        Intent intent=getIntent();
//       Bundle bundle=getIntent().getExtras();
        title = getIntent().getStringExtra("title");
        handler=new Handler();
        subtitle.setText(title);
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }
        if (!id.isEmpty() && id != null) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                customDialog.show();
                customDialog.setCancelable(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customDialog.dismiss();
                        loadSubcategory(id);
                    }
                },3000);
            } else {
                NetworkUtils.showNoNetworkDialog(context);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("Id",id);
        super.onSaveInstanceState(outState);
    }


    private void loadSubcategory(final String id) {
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<SubCategory>().setQuery(mRef.orderByChild("categoryId").equalTo(id), SubCategory.class).build();
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<SubCategory,SubCategoryAdapter>(personsOptions
                ) {
                    @Override
                    protected void onBindViewHolder(SubCategoryAdapter holder, int position, SubCategory model) {
                       holder.sublist_text.setText(model.getName());
                        Picasso.get().load(model.getIcon()).into(holder.sublist_img);
                        final SubCategory itemClick=model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                String subId=firebaseRecyclerAdapter.getRef(position).getKey();
                                Intent intent=new Intent(SubCategoryActivity.this,DetailedActivity.class);
                                intent.putExtra("id",subId);
                                intent.putExtra("title",firebaseRecyclerAdapter.getItem(position).getName());
                                intent.putExtra("category_title",title);
                                intent.putExtra("category_id",id);
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public SubCategoryAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sublist_row, parent, false);
                        return new SubCategoryAdapter(view);
                    }
        };
        mrecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseRecyclerAdapter.startListening();
            }
        },3000);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}