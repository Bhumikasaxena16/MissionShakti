package com.upicon.app.Dashboards;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentFour extends androidx.fragment.app.Fragment {


    View view;
    SessionManager sessionManager;
    HashMap<String, String> user;

    TextView user_name;
    TextView tv_id,tv_user_designation,tv_name,tv_mobile,tv_email,tv_address;
    Button btn_share_app,btn_logout;
    TextView tv_app_version,tv_privacy_policy,tv_term_condition;
    ShimmerFrameLayout shimmerFrameLayout;


    public FragmentFour() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_four, container, false);

        Init();
        getDetails();
        ClickListeners();

        return view;
    }



    private void Init() {
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();



        shimmerFrameLayout = (ShimmerFrameLayout)view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();


        user_name=view.findViewById(R.id.tv_user_name);



        user_name.setText(user.get(SessionManager.KEY_NAME));

        tv_user_designation =view.findViewById(R.id.tv_user_designation);
        tv_id=view.findViewById(R.id.tv_id);
        tv_name=view.findViewById(R.id.tv_name);
        tv_mobile=view.findViewById(R.id.tv_mobile);
        tv_email=view.findViewById(R.id.tv_email);
        tv_address=view.findViewById(R.id.tv_address);


        btn_share_app=view.findViewById(R.id.btn_share_app);
        btn_logout=view.findViewById(R.id.btn_logout);

        tv_app_version=view.findViewById(R.id.tv_app_version);

        tv_privacy_policy=view.findViewById(R.id.tv_privacy_policy);
        tv_term_condition=view.findViewById(R.id.tv_term_condition);

        //tv_app_version.setText("Credil - "+ BuildConfig.VERSION_NAME);

    }

    private void ClickListeners() {

        btn_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareApp();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutPopUp();
            }
        });


        tv_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com"));
             startActivity(intent);
            }
        });
        tv_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com"));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    public void getDetails(){

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("dc_response",response);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            if(jsonObject.get("Response").equals(true)){
                                setProfileDetails(jsonObject.getString("Data"));
                            }

                        }
                        catch (JSONException e) { e.printStackTrace(); }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dc_error",error.toString());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", user.get(SessionManager.KEY_ID));
                return headers;
            }
            @SuppressLint("HardwareIds")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user.get(SessionManager.KEY_ID));
                params.put("user_role",user.get(SessionManager.KEY_ROLE));
                params.put("token",BaseURL.TOKEN);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void setProfileDetails(String data) {
        try {
            JSONArray jsonarray = new JSONArray(data);
            JSONObject obj = jsonarray.getJSONObject(0);

            tv_id.setText(obj.getString("id"));
            tv_name.setText( obj.getString("name"));
            tv_mobile.setText( obj.getString("mobile"));
            tv_email.setText( obj.getString("email"));
            tv_user_designation.setText( obj.getString("role"));
            tv_address.setText(obj.getString("address"));

        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    private void LogoutPopUp() {
        Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.show();
        Window window=dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setLayout(700, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);
        Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sessionManager.logoutUser();

            }
        });
    }

    private void RateApp() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }
        try {
            startActivity(goToMarket);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" +getActivity().getPackageName())));
        }
    }

    private void ShareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out DayLogs app at: https://play.google.com/store/apps/details?id="+getActivity().getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}
