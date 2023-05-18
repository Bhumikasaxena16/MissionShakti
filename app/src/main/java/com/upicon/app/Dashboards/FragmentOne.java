package com.upicon.app.Dashboards;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.upicon.app.Adapters.SlidingImageAdapter;
import com.upicon.app.Adapters.UserIconAdapter;
import com.upicon.app.Agent.AgentDashboard;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.Models.UserIcon;
import com.upicon.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentOne extends androidx.fragment.app.Fragment {

    View view;
    SessionManager sessionManager;
    HashMap<String, String> user;

    private static int mcurrentPage = 0;
    ArrayList<String> dcurls;


    public FragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);

        initialization();
        clickListener();
       // getDashboardIcons();
        MainSliderImages();

        return view;
    }

    private void initialization() {

        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails();



    }

    private void clickListener() {


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void MainSliderImages() {
        dcurls = new ArrayList<>();

        dcurls.add(BaseURL.SLIDER_IMAGE+"slider1.jpg");
        dcurls.add(BaseURL.SLIDER_IMAGE+"slider2.jpg");
        dcurls.add(BaseURL.SLIDER_IMAGE+"slider3.jpg");


        ViewPager mainViewpager = (ViewPager)view.findViewById(R.id.main_slider_viewpager);
        mainViewpager.setAdapter(new SlidingImageAdapter(getContext(),dcurls));

        TextView tv_dot=(TextView)view.findViewById(R.id.tv_dot);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (mcurrentPage ==  dcurls.size()) { mcurrentPage = 0;}
                mainViewpager.setCurrentItem(mcurrentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 3000);

        mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {

                //pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });
    }



}
