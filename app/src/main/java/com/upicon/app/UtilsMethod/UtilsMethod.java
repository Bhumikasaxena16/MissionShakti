package com.upicon.app.UtilsMethod;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import es.dmoral.toasty.Toasty;

public enum UtilsMethod {

    INSTANCE;
    SessionManager sessionManager;
    HashMap<String, String> user;

    public  String getUserId(Context context){
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        return user.get(SessionManager.KEY_ID);
    }
    public  String getUserName(Context context){
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        return user.get(SessionManager.KEY_NAME);
    }
    public  String getUserRole(Context context){
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        return user.get(SessionManager.KEY_ROLE);
    }
    public  String getUserToken(Context context){
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        return user.get(SessionManager.KEY_ID);
    }

    public void  successToast(Context context,String message){
        Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public void  errorToast(Context context,String message){
        Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public void  infoToast(Context context,String message){
        Toasty.info(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static String formatMobileNo(String mobileNo) {
        if (!mobileNo.isEmpty()) {
            return mobileNo.replace(mobileNo.substring(3, 7), "****");
        }/*from  w w w.ja  va  2s . com*/
        return "";
    }

    public String setApiResponse(Context context, String response) {
        String return_value = "";
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();

        try {
            JSONObject obj = new JSONObject(response);
            if (obj.get("Response").equals(false)) {
                errorToast(context,obj.getString("Message"));
                //sessionManager.logoutUser();
            } else if (obj.get("Response").equals(true)) {
                return_value = obj.getString("Data");
            } else if (obj.get("Response").equals(true)) {
                successToast(context,obj.getString("Message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return return_value;

    }

    public String convertAmPm(String timing) {

        StringTokenizer tk = new StringTokenizer(timing);
        String time = tk.nextToken();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt = null;
        try {
            dt = sdf.parse(time);
            //holder.user_business_timing.setText(sdfs.format(dt)+" to "+userssBusiness.getClose_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfs.format(dt);
    }

    public void callToNumber(Context context, String mobile_number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobile_number, null));
        Intent chooser = Intent.createChooser(intent, "Please Launch call manager");
        context.startActivity(chooser);
    }

    public void sendEmail(Context context, String email) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("email"));
        String[] s={email};
        intent.putExtra(Intent.EXTRA_EMAIL,s);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Tech Support Message");
        intent.putExtra(Intent.EXTRA_TEXT,"I want to enquire from you that ");
        intent.setType("message/rfc822");
        Intent chooser= Intent.createChooser(intent,"Please Launch Email");
        context.startActivity(chooser);

    }

    public void openWebsite(Context context, String website) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(website));
        context.startActivity(i);

    }

    public void hideKeyboard(Context context, android.view.View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void ShowDetails(Context context, String title, String details){

        Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_details);
        dialog.show();
        Window window=dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setLayout(700, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tv_title=(TextView) dialog.findViewById(R.id.txt_title);
        TextView tv_details=(TextView) dialog.findViewById(R.id.txt_details);

        tv_title.setText(title);
        tv_details.setText(details);


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

                dialog.dismiss();


            }
        });


    }

    public void forgetPassword(Context context){

        Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_forget_password);
        dialog.show();
        Window window=dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setLayout(700, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tv_support_one=(TextView) dialog.findViewById(R.id.tv_support_one);
        TextView tv_support_two=(TextView) dialog.findViewById(R.id.tv_support_two);

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
                dialog.dismiss();
            }
        });

        tv_support_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callToNumber(context,tv_support_one.getText().toString());
            }
        });
        tv_support_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callToNumber(context,tv_support_two.getText().toString());
            }
        });


    }

    public void updateApp(Context context,String message){

        Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_app_update);
        dialog.show();
        dialog.setCancelable(false);
        Window window=dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setLayout(700, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView txt_details=(TextView) dialog.findViewById(R.id.txt_details);
        txt_details.setText(message);

        Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);
        Button btn_update=(Button)dialog.findViewById(R.id.btn_update);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }
                try {
                    context.startActivity(goToMarket);
                   //context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" +context.getPackageName())));
                }
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }

    public void checkUpdate(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.APP_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("volleyResponse",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.get("Response").equals(false)){
                                UtilsMethod.INSTANCE.updateApp(context,jsonObject.getString("Message"));
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
               // params.put("current_version", String.valueOf(BuildConfig.VERSION_CODE));
                params.put("token",BaseURL.TOKEN);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

}
