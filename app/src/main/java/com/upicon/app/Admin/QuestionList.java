package com.upicon.app.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upicon.app.Adapters.QuestionAdapter;
import com.upicon.app.Adapters.UserAdapter;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.BasicActivities.UserProfile;
import com.upicon.app.Models.Questions;
import com.upicon.app.Models.User;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuestionList extends AppCompatActivity {
    private List<Questions> questionsList;
    private QuestionAdapter questionAdapter;

    ShimmerFrameLayout shimmerFrameLayout;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    FloatingActionButton floatingActionButton;

    Intent intent;
    String dept_id="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);


        intent=getIntent();
        dept_id=intent.getStringExtra("dept_id");

        initialization();
        toolBar();
        refresh();
        clickListener();
        searchView();
        recyclerView();
        getUserList(dept_id);
    }

    private void initialization() {
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();

        floatingActionButton=findViewById(R.id.floatingActionButton);

    }

    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Questionnaires");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.voice_search:

                Intent intent;
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.speech_prompt));
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                assert result != null;
                questionAdapter.getFilter().filter(result.get(0));

            }
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void searchView() {
        AutoCompleteTextView autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.search);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                questionAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void clickListener() {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddQuestionPopup();
            }
        });
    }



    private void refresh() {
        SwipeRefreshLayout swipeLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout) ;
        swipeLayout.setColorSchemeColors(Color.RED, Color.MAGENTA, Color.GREEN);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getUserList(dept_id);
                        swipeLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void recyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        //recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        questionsList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(QuestionList.this, questionsList);
        recyclerView.setAdapter(questionAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });
    }

    private void getUserList(String dept_id) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.QUESTION_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        setResponse(UtilsMethod.INSTANCE.setApiResponse(getApplicationContext(),response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UtilsMethod.INSTANCE.getUserId(getApplicationContext()));
                params.put("dept_id",dept_id);
                params.put("token",UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void setResponse(String response) {

        if (!response.isEmpty()){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Questions>>(){}.getType();
            List<Questions> items = gson.fromJson(response, listType);
            questionsList.clear();
            questionsList.addAll(items);
            questionAdapter.notifyDataSetChanged();
        }

    }


    private void AddQuestionPopup() {
        Dialog dialog=new Dialog(QuestionList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_question_dialog);
        dialog.show();
        Window window=dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setLayout(700, LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText et_question=(EditText) dialog.findViewById(R.id.et_question);
        EditText et_option_one=(EditText) dialog.findViewById(R.id.et_option_one);
        EditText et_option_two=(EditText) dialog.findViewById(R.id.et_option_two);
        EditText et_option_three=(EditText) dialog.findViewById(R.id.et_option_three);
        EditText et_option_four=(EditText) dialog.findViewById(R.id.et_option_four);


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
                if(et_question.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter question");
                }
                else if(et_option_one.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter option 1");
                }
                else if(et_option_two.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter option 2");
                }
                else if(et_option_three.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter option 3");
                }
                else if(et_option_four.getText().toString().isEmpty()){
                    UtilsMethod.INSTANCE.errorToast(getApplicationContext(),"Enter option 4");
                }

                else{
                    dialog.dismiss();
                    AddQuetion(et_question.getText().toString(),et_option_one.getText().toString(),et_option_two.getText().toString(),et_option_three.getText().toString(),et_option_four.getText().toString());
                }

            }
        });
    }

    private void AddQuetion(String question, String option1, String option2, String option3, String option4) {
        final ProgressDialog pd=new ProgressDialog(QuestionList.this);
        pd.setMessage("Adding question....");
        pd.setCancelable(false);
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.ADD_QUESTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        pd.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.get("Response").equals(true)){
                                //SetResponse(jsonObject.getString("Data"));
                                UtilsMethod.INSTANCE.successToast(getApplicationContext(),jsonObject.getString("Message"));
                                getUserList(dept_id);

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
                headers.put("Authorization", UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UtilsMethod.INSTANCE.getUserId(getApplicationContext()));
                params.put("dept_id",dept_id);
                params.put("question",question);
                params.put("option_one",option1);
                params.put("option_two",option2);
                params.put("option_three",option3);
                params.put("option_four",option4);
                params.put("token",UtilsMethod.INSTANCE.getUserToken(getApplicationContext()));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }
    

}