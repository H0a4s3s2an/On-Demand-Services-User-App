package com.example.sample.usertribe.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.usertribe.Activities.SubCategoryActivity;
import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Interface.ItemClickListener;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.example.sample.usertribe.Helper.CategoryAdapter;
import com.example.sample.usertribe.Helper.ImageSliderAdapter;
import com.example.sample.usertribe.Model.Category;
import com.example.sample.usertribe.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * Created by Hassan Javaid on 6/12/2018.
 */

public class BookServiceFragment extends Fragment {
    View rootview;
    Context context;
    LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    Category category;
    FirebaseRecyclerAdapter<Category, CategoryAdapter> firebaseRecyclerAdapter;
    CustomDialog customDialog;
    Handler handler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.book_service_fragment, container,false);
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.list);
//        mRecyclerView.setHasFixedSize(true);
        //set layout as LinearLayout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Category");
        customDialog = new CustomDialog(getActivity());
        handler=new Handler();
        if(NetworkUtils.isNetworkAvailable(getActivity())) {
            customDialog.show();
            customDialog.setCancelable(false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    customDialog.dismiss();
                    initialLoad();
                }
            }, 3000);
        }
        else
        {
            NetworkUtils.showNoNetworkDialog(getActivity());
        }
        return rootview;
    }

    private void initialLoad() {
        Query personsQuery = mRef.orderByKey();
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Category>().setQuery(personsQuery, Category.class).build();
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Category, CategoryAdapter>(personsOptions
                ) {
                    @Override
                    public void onBindViewHolder(CategoryAdapter holder, int position, Category model) {
                        holder.setTitle(model.getTitle());
                        holder.setDescription(model.getDescription());
                        holder.setImage(model.getImage());
                        final Category clickItem=model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                String cid=firebaseRecyclerAdapter.getRef(position).getKey();
//                                Bundle bundle=new Bundle();
//                                bundle.putInt("id", Integer.parseInt(cid));
                                Intent intent=new Intent(getActivity(),SubCategoryActivity.class);
                                intent.putExtra("id",cid);
                                intent.putExtra("title",firebaseRecyclerAdapter.getItem(position).getTitle());
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public CategoryAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_row, parent, false);
                        return new CategoryAdapter(view);
                    }

                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


