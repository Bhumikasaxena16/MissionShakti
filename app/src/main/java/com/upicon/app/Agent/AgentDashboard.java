package com.upicon.app.Agent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.upicon.app.Adapters.UserIconAdapter;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.BasicActivities.UserProfile;
import com.upicon.app.Models.UserIcon;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AgentDashboard extends AppCompatActivity {

    SessionManager sessionManager;
    HashMap<String, String> user;

    ArrayList<UserIcon> userIconList;
    UserIconAdapter userIconAdapter;

    TextView tv_user_name,tv_user_mobile;
    ImageView iv_app,iv_setting;
    LinearLayout ll_all_application,ll_approved_application,ll_pending_application;


    TextView tv_dept_name,tv_dept_code,tv_dept_admin,tv_dept_mobile,tv_dept_alt_mobile,tv_dept_district,tv_dept_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_dashboard);

        initialization();
        clickListener();
        getDashboardIcons();
        GetDepartmentData();

    }

    private void initialization() {

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        iv_app=(ImageView) findViewById(R.id.iv_app);
        iv_setting=(ImageView) findViewById(R.id.iv_setting);

        tv_user_name=(TextView)findViewById(R.id.txt_user_name);
        tv_user_mobile=(TextView)findViewById(R.id.txt_user_mobile);

        ll_all_application=findViewById(R.id.ll_all_application);
        ll_approved_application=findViewById(R.id.ll_approved_application);
        ll_pending_application=findViewById(R.id.ll_pending_application);



        tv_user_name.setText(user.get(SessionManager.KEY_NAME));


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){tv_user_mobile.setText("Good Morning");}
        else if(timeOfDay >= 12 && timeOfDay < 16){tv_user_mobile.setText("Good Afternoon");}
        else if(timeOfDay >= 16 && timeOfDay < 21){tv_user_mobile.setText("Good Evening");}
        else if(timeOfDay >= 21 && timeOfDay < 24){tv_user_mobile.setText("Good Night");}
        else{tv_user_mobile.setText("Good Day");}




        tv_dept_name=findViewById(R.id.tv_dept_name);
        tv_dept_code=findViewById(R.id.tv_dept_code);
        tv_dept_admin=findViewById(R.id.tv_dept_admin);
        tv_dept_mobile=findViewById(R.id.tv_dept_mobile);
        tv_dept_alt_mobile=findViewById(R.id.tv_dept_alt_mobile);
        tv_dept_district=findViewById(R.id.tv_dept_district);
        tv_dept_state=findViewById(R.id.tv_dept_state);



        tv_dept_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethod.INSTANCE.callToNumber(AgentDashboard.this,tv_dept_mobile.getText().toString());
            }
        });
        tv_dept_alt_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethod.INSTANCE.callToNumber(AgentDashboard.this,tv_dept_alt_mobile.getText().toString());
            }
        });

    }

    private void clickListener() {
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AgentDashboard.this, UserProfile.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        ll_all_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Applications.class);
                intent.putExtra("status","All");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        ll_approved_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Applications.class);
                intent.putExtra("status","Approved");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        ll_pending_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Applications.class);
                intent.putExtra("status","Pending");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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
        finish();
    }


    private void GetDepartmentData() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.GET_DEPT_BY_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.get("Response").equals(true)){
                                SetDepartmentResponse(jsonObject.getString("Data"));

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
                params.put("user_id",UtilsMethod.INSTANCE.getUserId(getApplicationContext()));
               // params.put("dept_id",UtilsMethod.INSTANCE.getDeptId(getApplicationContext()));
                params.put("token",BaseURL.TOKEN);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


    private void SetDepartmentResponse(String response) {


        try {
            JSONArray jsonarray = new JSONArray(response);
            JSONObject obj = jsonarray.getJSONObject(0);


            if (obj.getString("status").equals("1")) {
                tv_dept_name.setText(obj.getString("name"));
                tv_dept_code.setText(obj.getString("code"));
                tv_dept_admin.setText(obj.getString("admin_name"));
                tv_dept_mobile.setText(obj.getString("mobile"));
                tv_dept_alt_mobile.setText(obj.getString("alt_mobile"));
                tv_dept_district.setText(obj.getString("district"));
                tv_dept_state.setText(obj.getString("state"));


            }
            else{
                UtilsMethod.INSTANCE.errorToast(this,"Department has been deactivated");
            }


        }
        catch (JSONException e) { e.printStackTrace(); }
    }



    private void getDashboardIcons() {

        userIconList = new ArrayList<>();

        userIconList.add(new UserIcon("Survey","Add new application",R.drawable.survey));
        userIconList.add(new UserIcon("Questionnaire","View all application",R.drawable.questionnaire));
        userIconList.add(new UserIcon("Surveyors","View approved application",R.drawable.surveyor));
        userIconList.add(new UserIcon("Departments","View pending application",R.drawable.departments));

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(AgentDashboard.this, LinearLayoutManager.VERTICAL, false));
        userIconAdapter = new UserIconAdapter(AgentDashboard.this, userIconList);
        recyclerView.setAdapter(userIconAdapter);

    }
}