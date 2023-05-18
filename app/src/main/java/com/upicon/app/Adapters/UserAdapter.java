package com.upicon.app.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.Models.User;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> implements Filterable {

    private List<User> OriginalList;
    private List<User> FilteredList;
    private Context context;

    public UserAdapter(Context context, List<User> OriginalList) {
        this.context = context;
        this.OriginalList = OriginalList;
        this.FilteredList = OriginalList;
        notifyItemChanged(0, FilteredList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_list, parent, false);
        return new MyViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final User user = FilteredList.get(position);


        holder.user_name.setText(user.getFirst_name()+" "+user.getLast_name());
        holder.user_contact.setText(user.getRole()+" # "+user.getMobile());
        holder.user_address.setText(user.getAddress());

        if (user.getStatus().equals("1")){
            holder.user_status.setText("Active");
            holder.user_status.setBackgroundResource(R.drawable.approved_background);
            holder.border_layout.setBackgroundResource(R.color.approved_color);

        }
        else if (user.getStatus().equals("0")){
            holder.user_status.setText("Deactivated");
            holder.user_status.setBackgroundResource(R.drawable.pending_background);
            holder.border_layout.setBackgroundResource(R.color.pending_color);
        }



        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(holder, user.getId(), user.getStatus());
            }
        });


        holder.user_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethod.INSTANCE.callToNumber(context,user.getMobile());
            }
        });


    }


    @Override
    public int getItemCount() {
        return FilteredList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    FilteredList = OriginalList;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User list : OriginalList) {
                        if (list.getFirst_name().toLowerCase().contains(charString.toLowerCase())||
                                list.getLast_name().toLowerCase().contains(charString.toLowerCase())||
                                list.getAddress().toLowerCase().contains(charString.toLowerCase())||
                                list.getDistrict().toLowerCase().contains(charString.toLowerCase())||
                                list.getMobile().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(list);
                        }
                    }
                    FilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FilteredList = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_name,user_contact,user_address,user_status;
        LinearLayout border_layout,card_layout;

        MyViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.user_name);
            user_contact = (TextView) view.findViewById(R.id.user_contact);
            user_address = (TextView) view.findViewById(R.id.user_address);
            user_status = (TextView) view.findViewById(R.id.user_status);
            border_layout = (LinearLayout) view.findViewById(R.id.border_layout);
            card_layout = (LinearLayout) view.findViewById(R.id.card_layout);
        }
    }

    private void showDialog(MyViewHolder holder, String id,String userStatus) {
        Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activate_deactivate_dialog);
        dialog.show();
        Window window=dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setLayout(800, LinearLayout.LayoutParams.WRAP_CONTENT);



        TextView txt_title=(TextView)dialog.findViewById(R.id.txt_title);
        TextView txt_message=(TextView)dialog.findViewById(R.id.txt_message);

        RadioGroup rg_status=(RadioGroup)dialog.findViewById(R.id.rg_status);
        RadioButton rb_activate=(RadioButton) dialog.findViewById(R.id.rb_activate);
        RadioButton rb_deactivate=(RadioButton) dialog.findViewById(R.id.rb_deactivate);
        RadioButton rb_block=(RadioButton) dialog.findViewById(R.id.rb_block);


//        txt_title.setText(title);
//        txt_message.setText(message_one);

        if(userStatus.equals("1")){
            rb_activate.setChecked(true);
        }
        else if(userStatus.equals("0")){
            rb_deactivate.setChecked(true);
        }



        Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);
        Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=rg_status.getCheckedRadioButtonId();
                RadioButton rb_approved=(RadioButton)dialog.findViewById(selectedId);
                dialog.dismiss();
                if(rb_approved.getText().toString().equals("Activate")){
                    updateStatus(holder,id,"1");
                }
                else if(rb_approved.getText().toString().equals("Deactivate")){
                    updateStatus(holder,id,"0");
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void updateStatus(MyViewHolder holder, String user_id, String user_status) {
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Updating please wait.....");
        pd.setCancelable(false);
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseURL.UPDATE_USER_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("dc_response",response);
                        pd.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            if(jsonObject.get("Response").equals(true)){

                                if(user_status.equals("1")){
                                    holder.user_status.setText("Activate");
                                    holder.user_status.setBackgroundResource(R.drawable.approved_background);
                                    holder.border_layout.setBackgroundResource(R.color.approved_color);

                                }
                                else if(user_status.equals("0")){
                                    holder.user_status.setText("Deactivate");
                                    holder.user_status.setBackgroundResource(R.drawable.pending_background);
                                    holder.border_layout.setBackgroundResource(R.color.pending_color);

                                }

                                UtilsMethod.INSTANCE.successToast(context,jsonObject.getString("Message"));


                            }
                            else {
                                UtilsMethod.INSTANCE.successToast(context,"Something went wrong");

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
                headers.put("Authorization", UtilsMethod.INSTANCE.getUserToken(context));
                return headers;
            }
            @SuppressLint("HardwareIds")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                 params.put("user_id",UtilsMethod.INSTANCE.getUserId(context));
                 params.put("user_status", user_status);
                 params.put("token", UtilsMethod.INSTANCE.getUserToken(context));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }



}
