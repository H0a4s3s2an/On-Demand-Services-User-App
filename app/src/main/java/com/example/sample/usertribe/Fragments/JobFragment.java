package com.example.sample.usertribe.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Helper.HistoryAdapter;
import com.example.sample.usertribe.Model.History;
import com.example.sample.usertribe.Model.UserAccount;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Hassan Javaid on 6/8/2018.
 */

public class JobFragment extends Fragment {
    private static DecimalFormat REAL_FORMAT = new DecimalFormat("0.#");
    Context context;
    View rootView;
    RecyclerView rv_trips;
    ProgressBar pb_loading_trips;
    TextView tv_empty_view;
    private HistoryAdapter mTripListAdapter;
    private ArrayList<History> mTrips;
    private DatabaseReference databaseReference;
    private UserAccount mCurrentUser;
    FirebaseRecyclerAdapter<History, HistoryAdapter> firebaseRecyclerAdapter;
    CustomDialog customDialog;
    Handler handler;
    public JobFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.frament_job, container, false);
        getActivity().setTitle("History");
        rv_trips = (RecyclerView) rootView.findViewById(R.id.rv_trips);
        pb_loading_trips = (ProgressBar) rootView.findViewById(R.id.pb_loading_trips);
        tv_empty_view = (TextView) rootView.findViewById(R.id.tv_empty_view);
        mTrips = new ArrayList<History>();
        tv_empty_view.setVisibility(View.INVISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_trips.setLayoutManager(linearLayoutManager);
        rv_trips.addItemDecoration(new DividerItemDecoration(rv_trips.getContext(),
                linearLayoutManager.getOrientation()));

        rv_trips.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("History").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        customDialog = new CustomDialog(getActivity());
        handler=new Handler();
        if(NetworkUtils.isNetworkAvailable(getActivity())) {
            customDialog.show();
            customDialog.setCancelable(false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initialLoad();
                    customDialog.dismiss();
                }
            }, 3000);
        }
        else
        {
            NetworkUtils.showNoNetworkDialog(getActivity());
        }
        return rootView;
    }

    private void initialLoad() {
//        showLoadingIndicator(true);
        Query personsQuery = databaseReference.orderByKey();
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<History>().setQuery(personsQuery, History.class).build();
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<History, HistoryAdapter>(personsOptions
                ) {
                    @Override
                    public void onBindViewHolder(HistoryAdapter holder, int position, History model) {
                        holder.tv_trip_car.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).into(holder.iv_driver_photo);
                        holder.tv_trip_total.setText(REAL_FORMAT.format(model.getPrice()));
                        holder.tv_trip_date.setText(model.getTime());
                    }

                    @Override
                    public HistoryAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
                        return new HistoryAdapter(view);
                    }

                    @Override
                    public void onDataChanged() {
                        if(firebaseRecyclerAdapter.getItemCount()==0)
                        {
                            tv_empty_view.setVisibility(View.VISIBLE);
                        }
                    }
                };
        rv_trips.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
}