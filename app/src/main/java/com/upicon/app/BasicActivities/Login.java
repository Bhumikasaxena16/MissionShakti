package com.upicon.app.BasicActivities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upicon.app.Agent.AgentDashboard;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.Dashboards.DashBoard;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    SessionManager sessionManager;
    HashMap<String, String> user;

    EditText et_user_mobile,et_user_password;
    TextView tv_forget_password, tv_new_account;
    Button btn_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        changeStatusBarColor();
        initialization();
        clickListener();
    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void initialization() {

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        et_user_mobile=(EditText)findViewById(R.id.et_user_mobile);
        et_user_password=(EditText)findViewById(R.id.et_user_password);
        tv_forget_password=(TextView)findViewById(R.id.tv_forget_password);
        tv_new_account=(TextView)findViewById(R.id.tv_new_account);
        btn_sign_in=(Button) findViewById(R.id.btn_sign_in);

    }

    private void clickListener() {
        tv_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registration.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin();
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethod.INSTANCE.forgetPassword(Login.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void validateLogin() {
        if(et_user_mobile.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(Login.this,et_user_mobile.getHint().toString());
        }
        else if(et_user_mobile.getText().toString().length()<10){
            UtilsMethod.INSTANCE.errorToast(Login.this,"Enter valid mobile number");
        }
        else if(et_user_password.getText().toString().isEmpty()){
            UtilsMethod.INSTANCE.errorToast(Login.this,et_user_password.getHint().toString());
        }
        else{
            passwordLogin();
        }
    }

    private void passwordLogin() {

        final ProgressDialog pd=new ProgressDialog(Login.this);
        pd.setMessage("Authenticating....");
        pd.setCancelable(false);
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.USER_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.e("volleyResponse",response);

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.get("Response").equals(true)){
                                setLoginResponse(jsonObject.getString("Data"));
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
                        pd.dismiss();
                        Log.e("volleyError",error.toString());
                    }
                }
        ) {
            @SuppressLint("HardwareIds")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", et_user_mobile.getText().toString());
                params.put("password",et_user_password.getText().toString());
                params.put("firebaseToken","firebaseToken");
                params.put("AndroidID", Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID)+"");
                params.put("device",Build.MODEL+"");
                params.put("token",BaseURL.TOKEN);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void setLoginResponse(String response) {

        try {
            JSONArray jsonarray = new JSONArray(response);
            JSONObject obj = jsonarray.getJSONObject(0);


            if (obj.getString("status").equals("1")) {

                sessionManager.CreateLoginSession(obj.getString("id"),obj.getString("name"), obj.getString("mobile"), obj.getString("role"));
                UtilsMethod.INSTANCE.successToast(this,"Login successfully");

                Intent intent = new Intent(this, DashBoard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
            else{
                UtilsMethod.INSTANCE.errorToast(this,"Your account has been deactivated");
            }


        }
        catch (JSONException e) { e.printStackTrace(); }

    }

}