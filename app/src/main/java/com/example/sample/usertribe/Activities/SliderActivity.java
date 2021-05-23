package com.example.sample.usertribe.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.sample.usertribe.R;
import com.example.sample.usertribe.Helper.SlidePager;

public class SliderActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private SlidePager pageadapter;
    private Button skip;
    private Button next;
    private int[] layout={R.layout.slide1,R.layout.slide2,R.layout.slide3};
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        skip=(Button)findViewById(R.id.skip);
        next=(Button)findViewById(R.id.next);
        skip.setOnClickListener(this);
        next.setOnClickListener(this);
         if(Build.VERSION.SDK_INT>=16)
         {
             getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
         }
         else
         {
             getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
         }
        viewPager=(ViewPager) findViewById(R.id.pager);
        pageadapter=new SlidePager(layout,this);
        viewPager.setAdapter(pageadapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==layout.length-1)
                {
                    next.setText("Start");
                    skip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    next.setText("Next");
                    skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.next:
                nextSlide();
                break;
            case R.id.skip:
                loadHome();
                break;
        }
    }

    private void loadHome() {
         Intent intent=new Intent(SliderActivity.this,FlatActivity.class);
         startActivity(intent);
    }
    private void nextSlide()
    {
        int n=viewPager.getCurrentItem()+1;
        if(n<layout.length)
        {
            viewPager.setCurrentItem(n);
        }
        else
        {
            loadHome();
        }
    }
}
