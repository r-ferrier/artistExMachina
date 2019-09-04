package com.example.workinprogress;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.workinprogress.paintings.Painting;
import com.example.workinprogress.paintings.shapes.BumpyShape;
import com.example.workinprogress.paintings.shapes.CurvedShape;
import com.example.workinprogress.paintings.shapes.Shape;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class About extends AppCompatActivity {

    private LayoutInflater inflater;
    private ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Reference ViewPager defined in activity
        viewPager = findViewById(R.id.aboutPager);
        //set the adapter that will create the individual pages
        viewPager.setAdapter(new ImagePagerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.aboutTabLayout);
        tabLayout.setupWithViewPager(viewPager, true);

        tabLayout.getTabAt(0).setText(getString(R.string.about));
        tabLayout.getTabAt(1).setText(getString(R.string.instructions));
    }


    /**
     * This class will allow two pages to be made, an about page and an instructions page, with tabs at
     * the top so that they can be swiped between
     */
    class ImagePagerAdapter extends PagerAdapter {

        private Context context;

        private ImagePagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            //Return total pages, one for each view
            return 2;
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page;
            if(position==0) {
                page = inflater.inflate(R.layout.fragment_about, null);
            }else{
                page = inflater.inflate(R.layout.fragment_instructions, null);
            }
            //Add the page to the front of the queue
            container.addView(page, 0);
            return page;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //this is required to check if the object instantiated is related to the view
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }

        //this forces images to update when notifyDataChange is called
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }
}
