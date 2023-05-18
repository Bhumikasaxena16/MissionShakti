package com.upicon.app.Agent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewSurvey extends AppCompatActivity {


    EditText et_business_name,et_mobile_number,et_est_year,et_address,et_pincode,et_udyog_number;
    AutoCompleteTextView auto_district,auto_state,auto_premises;

    private RadioGroup radioGroupNatureOfOperation;
    String nature_of_operation="";

    private RadioGroup radioGroupTypeOfUnit;
    String type_of_unit="";

    private RadioGroup radioGroupNatureOfJob;
    String nature_of_job="";

    private RadioGroup radioGroupEmploymentType;
    String employment_type="";

    private RadioGroup radioGroupVendorAgreement;
    String vendor_agreement="";

    private RadioGroup radioGroupSuppliersIncreased;
    String supplier_increased="";
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_survey);

        initialization();
        toolBar();
    }

    private void initialization() {


        et_business_name=findViewById(R.id.et_business_name);
        et_mobile_number=findViewById(R.id.et_mobile_number);
        et_est_year=findViewById(R.id.et_est_year);
        et_address=findViewById(R.id.et_address);
        et_pincode=findViewById(R.id.et_pincode);
        et_udyog_number=findViewById(R.id.et_udyog_number);



        auto_district=(AutoCompleteTextView) findViewById(R.id.auto_district);
        auto_state=(AutoCompleteTextView) findViewById(R.id.auto_state);
        auto_premises=(AutoCompleteTextView) findViewById(R.id.auto_premises);


        btn_add=findViewById(R.id.btn_add);


        //auto_district.setKeyListener(null);
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

        //auto_state.setKeyListener(null);
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


        //auto_premises.setKeyListener(null);
        ArrayAdapter<String> premisesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.hint_completion_layout, R.id.tvHintCompletion, getResources().getStringArray(R.array.premises));
        auto_premises.setAdapter(premisesAdapter);
        auto_premises.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                auto_premises.showDropDown();
                return false;
            }
        });

        radioGroupNatureOfOperation = (RadioGroup) findViewById(R.id.radioGroupNatureOfOperation);
        radioGroupTypeOfUnit = (RadioGroup) findViewById(R.id.radioGroupTypeOfUnit);
        radioGroupNatureOfJob = (RadioGroup) findViewById(R.id.radioGroupNatureOfJob);
        radioGroupEmploymentType = (RadioGroup) findViewById(R.id.radioGroupEmploymentType);
        radioGroupVendorAgreement = (RadioGroup) findViewById(R.id.radioGroupVendorAgreement);
        radioGroupSuppliersIncreased = (RadioGroup) findViewById(R.id.radioGroupSuppliersIncreased);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rbGroupNatureOfOperation = (RadioButton)findViewById(radioGroupNatureOfOperation.getCheckedRadioButtonId());
                nature_of_operation=rbGroupNatureOfOperation.getText().toString();

                RadioButton rbGroupTypeOfUnit = (RadioButton)findViewById(radioGroupTypeOfUnit.getCheckedRadioButtonId());
                type_of_unit=rbGroupTypeOfUnit.getText().toString();

                RadioButton rbGroupNatureOfJob = (RadioButton)findViewById(radioGroupNatureOfJob.getCheckedRadioButtonId());
                nature_of_job=rbGroupNatureOfJob.getText().toString();

                RadioButton rbGroupEmploymentType = (RadioButton)findViewById(radioGroupEmploymentType.getCheckedRadioButtonId());
                employment_type=rbGroupEmploymentType.getText().toString();

                RadioButton rbGroupVendorAgreement = (RadioButton)findViewById(radioGroupVendorAgreement.getCheckedRadioButtonId());
                vendor_agreement=rbGroupVendorAgreement.getText().toString();

                RadioButton rbGroupSuppliersIncreased = (RadioButton)findViewById(radioGroupSuppliersIncreased.getCheckedRadioButtonId());
                supplier_increased=rbGroupSuppliersIncreased.getText().toString();

                if (et_business_name.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter Business name");
                }
                else if (et_mobile_number.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter contact number");
                }
                else if (et_mobile_number.getText().toString().length()<10){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter 10 digit contact number");
                }
                else if (et_est_year.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter establishment year");
                }
                else if (et_est_year.getText().toString().length()<4){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter 4 digit establishment year");
                }
                else if (et_address.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter address");
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
                else if (et_pincode.getText().toString().length()<6){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter 4 digit pincode");
                }
                else if (auto_premises.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select business premises");
                }
                else if (nature_of_operation.isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select nature of operation");
                }
                else if (type_of_unit.isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select type of unit");
                }
                else if (nature_of_job.isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select nature of job");
                }
                else if (employment_type.isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select employment type");
                }
                else if (vendor_agreement.isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select vendor agreement");
                }
                else if (supplier_increased.isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Select supplier increased");
                }
                else{
                    addNewApplication();
                }
            }
        });

    }

    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Audit");
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
        final ProgressDialog pd=new ProgressDialog(NewSurvey.this);
        pd.setMessage("Adding new survey....");
        pd.setCancelable(false);
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.ADD_NEW_AUDIT,
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
                                Intent intent=new Intent(NewSurvey.this, SurveyList.class);
                                intent.putExtra("status","New");
                                startActivity(intent);
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
                params.put("b_name",et_business_name.getText().toString());
                params.put("b_mobile",et_mobile_number.getText().toString());
                params.put("b_est_year",et_est_year.getText().toString());
                params.put("b_address",et_address.getText().toString());
                params.put("b_district",auto_district.getText().toString());
                params.put("b_state",auto_state.getText().toString());
                params.put("b_pincode",et_pincode.getText().toString());
                params.put("b_udyog_no",et_udyog_number.getText().toString());
                params.put("b_premises",auto_premises.getText().toString());
                params.put("b_nature_operation",nature_of_operation);
                params.put("b_unit_type",type_of_unit);
                params.put("b_nature_of_job",nature_of_job);
                params.put("b_employment_type",employment_type);
                params.put("b_vendor",vendor_agreement);
                params.put("b_supplier",supplier_increased);
                params.put("token",UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }
}