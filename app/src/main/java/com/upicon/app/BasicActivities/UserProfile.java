package com.upicon.app.BasicActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    SessionManager sessionManager;
    HashMap<String, String> user;

    Toolbar toolbar;
    TextView tv_user_name,tv_user_id,tv_user_designation,tv_user_name2,tv_user_mobile,tv_user_email,tv_user_address;
    TextView tv_change_password,tv_logout;

    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        Init();
        MyToolbar();
        MyClickListener();
        getDetails();
    }

    private void Init() {
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        tv_user_name=findViewById(R.id.tv_user_name);
        tv_user_id=findViewById(R.id.tv_user_id);
        tv_user_designation =findViewById(R.id.tv_user_designation);
        tv_user_name2=findViewById(R.id.tv_user_name2);
        tv_user_mobile=findViewById(R.id.tv_user_mobile);
        tv_user_email=findViewById(R.id.tv_user_email);
        tv_user_address=findViewById(R.id.tv_user_address);


        tv_change_password=findViewById(R.id.tv_change_password);
        tv_logout=findViewById(R.id.tv_logout);


        tv_user_name.setText(UtilsMethod.INSTANCE.getUserName(getApplicationContext()));
        tv_user_name2.setText(UtilsMethod.INSTANCE.getUserName(getApplicationContext()));
        tv_user_id.setText("VS"+UtilsMethod.INSTANCE.getUserId(getApplicationContext()));

    }

    private void MyToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

    private void MyClickListener() {
        tv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordView();
            }
        });
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutPopUp();
            }
        });

    }

    public void ChangePasswordView(){

        Dialog dialog=new Dialog(UserProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_password_dialog);
        dialog.show();
        Window window=dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setLayout(700, LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText et_old_password=(EditText) dialog.findViewById(R.id.et_old_password);
        EditText et_password=(EditText) dialog.findViewById(R.id.et_password);
        EditText et_confirm_password=(EditText) dialog.findViewById(R.id.et_confirm_password);


        Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);
        Button btn_update=(Button)dialog.findViewById(R.id.btn_update);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_old_password.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),et_old_password.getHint().toString());
                }
                if(et_password.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),et_password.getHint().toString());
                }
                else if(et_password.getText().toString().length()<5){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Minimum 5 digit password required");
                }
                else if(et_confirm_password.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),et_confirm_password.getHint().toString());
                }
                else if(!et_confirm_password.getText().toString().equals(et_password.getText().toString())){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Confirm password not matched");
                }
                else{
                    dialog.dismiss();
                    ChangePassword(et_old_password.getText().toString(),et_confirm_password.getText().toString());
                }

            }
        });
    }

    private void LogoutPopUp() {
        Dialog dialog=new Dialog(UserProfile.this);
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
               // logoutUser();
                sessionManager.logoutUser();

            }
        });
    }


    public void getDetails(){

        RequestQueue queue = Volley.newRequestQueue(UserProfile.this);
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
                            else {
                                UtilsMethod.INSTANCE.errorToast(getApplicationContext(),jsonObject.getString("Message"));

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
                headers.put("Authorization", UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return headers;
            }
            @SuppressLint("HardwareIds")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UtilsMethod.INSTANCE.getUserId(getApplicationContext()));
                params.put("user_role",UtilsMethod.INSTANCE.getUserRole(getApplicationContext()));
                params.put("token",UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
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
           ;
            tv_user_email.setText( obj.getString("email"));
            tv_user_mobile.setText( obj.getString("mobile"));
            tv_user_designation.setText( obj.getString("role"));
            tv_user_address.setText(obj.getString("address"));


        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    private void ChangePassword(String old_password, String new_password) {

        ProgressDialog pd=new ProgressDialog(UserProfile.this);
        pd.setMessage("Updating please wait.....");
        pd.setCancelable(false);
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(UserProfile.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.CHANGE_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("dc_response",response);
                        pd.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            if(jsonObject.get("Response").equals(true)){

                                sessionManager.logoutUser();
                                UtilsMethod.INSTANCE.successToast(getApplicationContext(),jsonObject.getString("Message"));

                            }
                            else {
                                UtilsMethod.INSTANCE.errorToast(getApplicationContext(),jsonObject.getString("Message"));

                            }

                        }
                        catch (JSONException e) { e.printStackTrace(); }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Log.e("dc_error",error.toString());

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return headers;
            }
            @SuppressLint("HardwareIds")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UtilsMethod.INSTANCE.getUserId(getApplicationContext()));
                params.put("user_role",UtilsMethod.INSTANCE.getUserRole(getApplicationContext()));
                params.put("old_password", old_password);
                params.put("new_password", new_password);
                params.put("token",UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void logoutUser() {
        ProgressDialog pd=new ProgressDialog(UserProfile.this);
        pd.setMessage("Please wait.....");
        pd.setCancelable(false);
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(UserProfile.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.USER_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("dc_response",response);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            if(jsonObject.get("Response").equals(true)){
                                sessionManager.logoutUser();
                                UtilsMethod.INSTANCE.successToast(getApplicationContext(),jsonObject.getString("Message"));
                            }
                            else {
                                UtilsMethod.INSTANCE.errorToast(getApplicationContext(),jsonObject.getString("Message"));

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
                headers.put("Authorization", UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return headers;
            }
            @SuppressLint("HardwareIds")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UtilsMethod.INSTANCE.getUserId(getApplicationContext()));
                params.put("user_role",UtilsMethod.INSTANCE.getUserRole(getApplicationContext()));
                params.put("token",UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


}