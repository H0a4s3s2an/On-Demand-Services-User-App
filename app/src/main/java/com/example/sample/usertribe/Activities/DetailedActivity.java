package com.example.sample.usertribe.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Helper.ServiceTypeAdapter;
import com.example.sample.usertribe.Helper.SubCategoryAdapter;
import com.example.sample.usertribe.Interface.ItemClickListener;
import com.example.sample.usertribe.Model.ServiceType;
import com.example.sample.usertribe.Model.SubCategory;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.firebase.geofire.core.GeoHashQuery;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailedActivity extends AppCompatActivity {
    private Context context = DetailedActivity.this;
    RecyclerView mrecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    RecyclerView.LayoutManager layoutManager;
    String id="";
    String title;
    String category_id="";
    String category_title;
    TextView subtitle;
    FirebaseRecyclerAdapter<ServiceType,ServiceTypeAdapter> firebaseRecyclerAdapter;
    Button book_now;
    Button book_later;
    public int lastSlectedposition=-1;
    String value;
    String radio_price;
    String radio_name;
    Handler handler;
    CustomDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Service");
        subtitle=(TextView)findViewById(R.id.sub_type_title);
       book_now=(Button)findViewById(R.id.book_now);
       book_later = (Button)findViewById(R.id.book_later);
       book_later.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(DetailedActivity.this);
               builder.setMessage("This Feature is not Available yet.")
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               //do things
                           }
                       });
               AlertDialog alert = builder.create();
               alert.show();
           }
       });
       book_now.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(value==null)
               {
                   Toast.makeText(DetailedActivity.this, "Select one value", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Intent intent=new Intent(DetailedActivity.this,PaymentModeActivity.class);
                   intent.putExtra("detail_name",value);
                   intent.putExtra("subcategory_id",id);
                   intent.putExtra("subcategory_name",title);
                   intent.putExtra("category_name",category_title);
                   intent.putExtra("category_id",category_id);
                   startActivity(intent);
               }
//        intent.putExtra("productId",id);

           }
       });
       title=getIntent().getStringExtra("title");
        subtitle.setText(title);
        category_title=getIntent().getStringExtra("category_title");
        category_id=getIntent().getStringExtra("category_id");
        mrecyclerView=(RecyclerView)findViewById(R.id.detail_list);
        layoutManager=new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setHasFixedSize(true);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("ServiceType");
        handler=new Handler();
        customDialog = new CustomDialog(context);
        if(getIntent()!=null)
        {
            id=getIntent().getStringExtra("id");
        }
        if(!id.isEmpty() && id!=null)
        {
            if(NetworkUtils.isNetworkAvailable(context))
            {
                customDialog.show();
                customDialog.setCancelable(false);
             handler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     loadServieType(id);
                     customDialog.dismiss();
                 }
             },3000);
            }
            else
            {
                NetworkUtils.showNoNetworkDialog(context);
            }

        }
    }
    private void loadServieType(String id) {
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<ServiceType>().setQuery(mRef.orderByChild("subCategoryId").equalTo(id), ServiceType.class).build();
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ServiceType,ServiceTypeAdapter>(personsOptions
                ) {
                    @Override
                    protected void onBindViewHolder(final ServiceTypeAdapter holder, final int position, final ServiceType model) {
                        holder.text_price.setText(model.getPrice());
                        holder.radio_1.setText(model.getName());
                        holder.radio_1.setChecked(position==lastSlectedposition);
                        RadioButton radioButton=(RadioButton)findViewById(R.id.radio_1);
                        holder.radio_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(position==lastSlectedposition)
                                {
                                    holder.radio_1.setChecked(false);
                                }else {
                                    lastSlectedposition = position;
                                    notifyDataSetChanged();
                                    value=holder.radio_1.getText().toString();
                                }
                            }
                        });
                    }
                    @Override
                    public ServiceTypeAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_row, parent, false);
                        return new ServiceTypeAdapter(view);
                    }
                };
        mrecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
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
