package com.example.sample.usertribe.Helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sample.usertribe.Interface.ItemClickListener;
import com.example.sample.usertribe.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Hassan Javaid on 6/17/2018.
 */

public class SubCategoryAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
   public TextView sublist_text;
   public ImageView sublist_img;
   public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SubCategoryAdapter(View itemView) {
        super(itemView);
        sublist_img=itemView.findViewById(R.id.sublist_img);
        sublist_text= itemView.findViewById(R.id.sublist_text);
        itemView.setOnClickListener(this);
    }
//public void setName(String name)
//{
//    sublist_text.setText(name);
//}
//public void setIcon(String icon)
//{
//    Picasso.get()
//            .load(icon)
//            .into(sublist_img);
//}

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
