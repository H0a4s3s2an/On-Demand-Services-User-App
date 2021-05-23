package com.example.sample.usertribe.Helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sample.usertribe.Fragments.JobFragment;
import com.example.sample.usertribe.Interface.ItemClickListener;
import com.example.sample.usertribe.Interface.OnLoadMoreListener;
import com.example.sample.usertribe.Model.History;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hassan Javaid on 9/6/2018.
 */

public class HistoryAdapter extends RecyclerView.ViewHolder {
   public TextView tv_trip_car;
   public TextView tv_trip_total;
   public ImageView iv_driver_photo;
   public TextView tv_trip_date;
    public HistoryAdapter(final View itemView) {
        super(itemView);
        tv_trip_car = itemView.findViewById(R.id.tv_trip_car);
        tv_trip_total = itemView.findViewById(R.id.tv_trip_total);
        iv_driver_photo = itemView.findViewById(R.id.iv_driver_photo);
        tv_trip_date = itemView.findViewById(R.id.tv_trip_date);
    }

}
