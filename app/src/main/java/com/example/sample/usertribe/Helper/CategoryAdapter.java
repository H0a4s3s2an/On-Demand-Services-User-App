package com.example.sample.usertribe.Helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sample.usertribe.Interface.ItemClickListener;
import com.example.sample.usertribe.Model.Category;
import com.example.sample.usertribe.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hassan Javaid on 6/12/2018.
 */

public class CategoryAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mTitleTv;
    TextView mDetailTv;
    ImageView mImageIv;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryAdapter(final View itemView) {
        super(itemView);
        mTitleTv = itemView.findViewById(R.id.rTitleTv);
        mDetailTv = itemView.findViewById(R.id.rDescriptionTv);
        mImageIv = itemView.findViewById(R.id.rImageView);
        itemView.setOnClickListener(this);
    }
        public void setTitle(String title)
        {
            mDetailTv.setText(title+"");
        }
        public void setDescription(String description)
        {
            mTitleTv.setText(description);
        }
        public void setImage(String image)
        {
            Picasso.get()
                    .load(image)
                    .into(mImageIv);
        }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
