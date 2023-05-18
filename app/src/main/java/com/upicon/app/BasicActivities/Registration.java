package com.upicon.app.BasicActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upicon.app.Admin.UserList;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity{

    SessionManager sessionManager;
    HashMap<String, String> user;
    EditText et_full_name,et_mobile_number,et_aadhaar_number,et_password,r,et_address;
    AutoCompleteTextView auto_district,auto_state;

    Button btn_create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        initialization();
        toolBar();
        //changeStatusBarColor();

        clickListener();
    }

    private void initialization() {
        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();



        et_full_name=findViewById(R.id.et_full_name);
        et_mobile_number=findViewById(R.id.et_mobile_number);
        et_aadhaar_number=findViewById(R.id.et_aadhaar_number);
        et_password=findViewById(R.id.et_password);
        et_address=findViewById(R.id.et_address);
        auto_district=findViewById(R.id.auto_district);
        auto_state=findViewById(R.id.auto_state);


        btn_create_account=findViewById(R.id.btn_create_account);

        auto_district.setKeyListener(null);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, getResources().getStringArray(R.array.up_district));
        auto_district.setAdapter(districtAdapter);
        auto_district.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                auto_district.showDropDown();
                return false;
            }
        });

        auto_state.setKeyListener(null);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, getResources().getStringArray(R.array.states));
        auto_state.setAdapter(stateAdapter);
        auto_state.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                auto_state.showDropDown();
                return false;
            }
        });
    }



    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create Account");
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void clickListener() {

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }

    private void validateForm() {
        if (et_full_name.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter full name");
        }
        else if (et_mobile_number.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter mobile number");
        }
        else if (et_mobile_number.getText().toString().length()<10){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter 10 digit mobile number");
        }
        else if (et_aadhaar_number.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter aadhaar number");
        }
        else if (et_aadhaar_number.getText().toString().length()<12){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter 12 digit aadhaar number");
        }
        else if (et_password.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter password");
        }
        else if (et_password.getText().toString().length()<5){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Too shot password");
        }
        else if (auto_district.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select district");
        }
        else if (auto_state.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select state");
        }
        else if (et_address.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter address");
        }
        else if (et_address.getText().toString().length()<10){
            UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter full address");
        }
        else{
            addSurveyor();
        }
    }


    private void addSurveyor() {
        final ProgressDialog pd=new ProgressDialog(Registration.this);
        pd.setMessage("Creating account....");
        pd.setCancelable(false);
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.SURVEYOR_REGISTRATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SetUserResponse(response);
                        pd.dismiss();
                        Log.e("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Log.e("error",error.toString());

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
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("s_id","0");
                params.put("name",et_full_name.getText().toString());
                params.put("mobile",et_mobile_number.getText().toString());
                params.put("aadhaar",et_aadhaar_number.getText().toString());
                params.put("password",et_password.getText().toString());
                params.put("address",et_address.getText().toString());
                params.put("district",auto_district.getText().toString());
                params.put("state",auto_state.getText().toString());
                params.put("role","Supervisor");
                params.put("token",BaseURL.TOKEN);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void SetUserResponse(String response) {

        try {
            JSONObject obj = new JSONObject(response);
            if(obj.get("Response").equals(true)){
                if(sessionManager.isLoggedIn()){
                    UtilsMethod.INSTANCE.successToast(Registration.this,obj.getString("Message"));
                    Intent intent=new Intent(getApplicationContext(), UserList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
                else{
                    UtilsMethod.INSTANCE.successToast(Registration.this,obj.getString("Message"));
                    Intent intent=new Intent(getApplicationContext(), Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }

            }
            else if(obj.get("Response").equals(false)){
                UtilsMethod.INSTANCE.errorToast(Registration.this,obj.getString("Message"));
            }
            else {
                UtilsMethod.INSTANCE.errorToast(Registration.this,"Something went wrong");
            }

        }

        catch (JSONException e) { e.printStackTrace(); }
    }

}