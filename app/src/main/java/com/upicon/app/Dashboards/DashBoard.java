package com.upicon.app.Dashboards;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.BottomNavigationBehavior;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashBoard extends AppCompatActivity{

    SessionManager sessionManager;
    HashMap<String, String> user;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board);

        initialization();
        checkUserSession();
        UtilsMethod.INSTANCE.checkUpdate(getApplicationContext());


    }

    private void initialization() {

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        //layoutParams.setBehavior(new BottomNavigationBehavior());
        bottomNavigationView.setSelectedItemId(R.id.navigationOne);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigationOne:
                    fragment = new FragmentOne();
                    loadFragment(R.id.frame_container,fragment);
                    return true;
                case R.id.navigationTwo:
                    fragment = new FragmentTwo();
                    loadFragment(R.id.frame_container,fragment);
                    return true;
                case R.id.navigationThree:
                    fragment = new FragmentThree();
                    loadFragment(R.id.frame_container,fragment);
                    return true;
                case R.id.navigationFour:
                    fragment = new FragmentFour();
                    loadFragment(R.id.frame_container,fragment);
                    return true;
                default:
                    return true;
            }

        }
    };

    private void loadFragment(int view ,Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkUserSession() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.USER_SESSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("volleyResponse",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.get("Response").equals(false)){
                                sessionManager.logoutUser();
                                finish();
                                UtilsMethod.INSTANCE.infoToast(getApplicationContext(),jsonObject.getString("Message"));
                            }

                        }
                        catch (JSONException e) { e.printStackTrace(); }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volleyError",error.toString());
                    }
                }
        ) {
            @SuppressLint("HardwareIds")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user.get(SessionManager.KEY_ID));
                params.put("device","Android Device");
                params.put("token",BaseURL.TOKEN);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

}