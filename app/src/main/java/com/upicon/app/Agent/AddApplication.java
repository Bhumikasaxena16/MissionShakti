package com.upicon.app.Agent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddApplication extends AppCompatActivity {

    EditText et_first_name,et_last_name,et_father_name,et_mobile_number,et_dob,et_aadhaar,et_voter_id,et_locality,et_village,et_pincode;
    AutoCompleteTextView auto_gender,auto_marital,auto_category,auto_minority,auto_district,auto_state;

    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_application);

        initialization();
        toolBar();
    }

    private void initialization() {

        et_first_name=findViewById(R.id.et_first_name);
        et_last_name=findViewById(R.id.et_last_name);
        et_father_name=findViewById(R.id.et_father_name);
        et_mobile_number=findViewById(R.id.et_mobile_number);
        et_dob=findViewById(R.id.et_dob);
        et_aadhaar=findViewById(R.id.et_aadhaar);
        et_voter_id=findViewById(R.id.et_voter_id);
        et_locality=findViewById(R.id.et_locality);
        et_village=findViewById(R.id.et_village);
        et_pincode=findViewById(R.id.et_pincode);


        auto_gender=(AutoCompleteTextView) findViewById(R.id.auto_gender);
        auto_marital=(AutoCompleteTextView) findViewById(R.id.auto_marital);
        auto_category=(AutoCompleteTextView) findViewById(R.id.auto_category);
        auto_minority=(AutoCompleteTextView) findViewById(R.id.auto_minority);
        auto_district=(AutoCompleteTextView) findViewById(R.id.auto_district);
        auto_state=(AutoCompleteTextView) findViewById(R.id.auto_state);


        btn_add=findViewById(R.id.btn_add);


        auto_gender.setKeyListener(null);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, getResources().getStringArray(R.array.gender));
        auto_gender.setAdapter(genderAdapter);
        auto_gender.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                auto_gender.showDropDown();
                return false;
            }
        });

        auto_marital.setKeyListener(null);
        ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, getResources().getStringArray(R.array.marital_status));
        auto_marital.setAdapter(materialAdapter);
        auto_marital.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                auto_marital.showDropDown();
                return false;
            }
        });

        auto_category.setKeyListener(null);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, getResources().getStringArray(R.array.category));
        auto_category.setAdapter(categoryAdapter);
        auto_category.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                auto_category.showDropDown();
                return false;
            }
        });

        auto_minority.setKeyListener(null);
        ArrayAdapter<String> minorityAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, getResources().getStringArray(R.array.minority));
        auto_minority.setAdapter(minorityAdapter);
        auto_minority.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                auto_minority.showDropDown();
                return false;
            }
        });

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


        final Calendar calendar= Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        long upperLimit = calendar.getTimeInMillis();


        final DatePickerDialog.OnDateSetListener date= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth){
                datePicker.setMaxDate(upperLimit);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat= "yyyy-MM-dd";
                SimpleDateFormat sdf= new SimpleDateFormat(myFormat, Locale.US);
                et_dob.setText(sdf.format(calendar.getTime()));
            }
        };

        et_dob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(AddApplication.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.getDatePicker().setMaxDate((upperLimit)-1000);
                datePickerDialog.show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_first_name.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter first name");
                }
                else if (et_father_name.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter father/spouse name");
                }
                else if (et_mobile_number.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter mobile number");
                }
                else if (et_mobile_number.getText().toString().length()<10){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter 10 digit mobile number");
                }
                else if (et_dob.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter date of birth");
                }
                else if (auto_gender.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select gender");
                }
                else if (auto_marital.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select marital status");
                }
                else if (auto_category.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.infoToast(getApplicationContext(),"Select category");
                }
                else if (auto_minority.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.infoToast(getApplicationContext(),"Select minority");
                }
                else if (et_aadhaar.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter aadhaar number");
                }
                else if (et_aadhaar.getText().toString().length()<12){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter 12 digit aadhaar number");
                }
                else if (et_voter_id.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter voter Id number");
                }
                else if (et_locality.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter house no or locality");
                }
                else if (et_village.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter ward or village name");
                }
                else if (auto_district.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select district");
                }
                else if (auto_state.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select state");
                }
                else if (et_pincode.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter pincode");
                }
                else if (et_aadhaar.getText().toString().length()<6){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter 6 digit pincode");
                }
                else{
                    addNewApplication();
                }
            }
        });
    }

    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Application");
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void addNewApplication() {
        final ProgressDialog pd=new ProgressDialog(AddApplication.this);
        pd.setMessage("Adding user....");
        pd.setCancelable(false);
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.ADD_NEW_APPLICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        pd.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.get("Response").equals(true)){
                                UtilsMethod.INSTANCE.successToast(getApplicationContext(),jsonObject.getString("Message"));
                                finish();
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            }
                            else if (jsonObject.get("Response").equals(false)){
                                UtilsMethod.INSTANCE.errorToast(getApplicationContext(),jsonObject.getString("Message"));
                            }
                            else {
                                UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Something went wrong");
                            }

                        }
                        catch (JSONException e) { e.printStackTrace(); }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                        pd.dismiss();

                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UtilsMethod.INSTANCE.getUserId(getApplicationContext()));
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UtilsMethod.INSTANCE.getUserId(getApplicationContext()));
                //params.put("ro_code", UtilsMethod.INSTANCE.getDeptId(getApplicationContext()));
                params.put("first_name", et_first_name.getText().toString());
                params.put("last_name",et_last_name.getText().toString());
                params.put("father_name",et_father_name.getText().toString());
                params.put("mobile",et_mobile_number.getText().toString());
                params.put("dob",et_dob.getText().toString());
                params.put("gender",auto_gender.getText().toString());
                params.put("marital",auto_marital.getText().toString());
                params.put("category",auto_category.getText().toString());
                params.put("minority",auto_minority.getText().toString());
                params.put("aadhaar",et_aadhaar.getText().toString());
                params.put("voter",et_voter_id.getText().toString());
                params.put("locality",et_locality.getText().toString());
                params.put("village",et_village.getText().toString());
                params.put("district",auto_district.getText().toString());
                params.put("state",auto_state.getText().toString());
                params.put("pincode",et_pincode.getText().toString());
                params.put("same_district","0");
                params.put("same_state","1");
                params.put("token",UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


}