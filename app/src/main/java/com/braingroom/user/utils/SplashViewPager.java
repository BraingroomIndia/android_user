package com.braingroom.user.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.braingroom.user.R;

import java.util.ArrayList;

/**
 * Created by godara on 30/07/17.
 */

public class SplashViewPager extends PagerAdapter {
    private ArrayList<Integer> images;
    private ArrayList<String> text;
    private LayoutInflater inflater;
    private Context context;

    public SplashViewPager(Context context, ArrayList<Integer> images) {
        this.context = context;
        this.images = images;
        this.text = text;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.splash_slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        /*TextView textView = (TextView)  myImageLayout.findViewById(R.id.text);*/
        myImage.setImageResource(images.get(position));
       /* textView.setText(text.get(position));*/
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
