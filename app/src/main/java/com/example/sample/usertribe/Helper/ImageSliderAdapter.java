package com.example.sample.usertribe.Helper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.sample.usertribe.R;

/**
 * Created by Hassan Javaid on 4/11/2018.
 */

public class ImageSliderAdapter extends PagerAdapter {
     Context context;
    LayoutInflater inflater;
   public int img[]={R.drawable.one,R.drawable.two,R.drawable.three};

    public ImageSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.imageslider,container,false);
            ImageView image = (ImageView) view.findViewById(R.id.img_view);
            // image.setImageResource(img[position]);
            Glide.with(context).load(img[position]).into(image);
            container.addView(view);
            return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
