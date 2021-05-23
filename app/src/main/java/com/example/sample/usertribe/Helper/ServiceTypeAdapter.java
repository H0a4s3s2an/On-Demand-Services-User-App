package com.example.sample.usertribe.Helper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.sample.usertribe.Interface.ItemClickListener;
import com.example.sample.usertribe.R;

/**
 * Created by Hassan Javaid on 7/1/2018.
 */

public class ServiceTypeAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
   public TextView text_price;
    public RadioButton radio_1;
    public int lastselecteposition=-1;
//    ItemClickListener itemClickListener;
//    public ServiceTypeAdapter(View itemView, ItemClickListener itemClickListener) {
//        super(itemView);
//        this.itemClickListener = itemClickListener;
//    }

    public ServiceTypeAdapter(View itemView) {
        super(itemView);
        text_price=itemView.findViewById(R.id.text_price);
        radio_1=itemView.findViewById(R.id.radio_1);
        radio_1.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        lastselecteposition=getAdapterPosition();

    }
}
