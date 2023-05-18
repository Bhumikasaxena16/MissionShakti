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
import com.upicon.app.Admin.QuestionList;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.Models.Questions;
import com.upicon.app.Models.User;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> implements Filterable {

    private List<Questions> OriginalList;
    private List<Questions> FilteredList;
    private Context context;

    public QuestionAdapter(Context context, List<Questions> OriginalList) {
        this.context = context;
        this.OriginalList = OriginalList;
        this.FilteredList = OriginalList;
        notifyItemChanged(0, FilteredList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_question_list, parent, false);
        return new MyViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Questions questionList = FilteredList.get(position);


        holder.tv_question.setText((position+1)+". "+questionList.getQuestion());
        holder.tv_option_one.setText("A. "+questionList.getOption_one());
        holder.tv_option_two.setText("B. "+questionList.getOption_two());
        holder.tv_option_three.setText("C. "+questionList.getOption_three());
        holder.tv_option_four.setText("D. "+questionList.getOption_four());


        if (questionList.getStatus().equals("1")){
            holder.border_layout.setBackgroundResource(R.color.approved_color);

        }
        else if (questionList.getStatus().equals("0")){
            holder.border_layout.setBackgroundResource(R.color.pending_color);
        }

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
                    List<Questions> filteredList = new ArrayList<>();
                    for (Questions list : OriginalList) {
                        if (list.getQuestion().toLowerCase().contains(charString.toLowerCase())||
                                list.getOption_four().toLowerCase().contains(charString.toLowerCase())||
                                list.getOption_one().toLowerCase().contains(charString.toLowerCase())||
                                list.getOption_three().toLowerCase().contains(charString.toLowerCase())||
                                list.getOption_two().toLowerCase().contains(charString.toLowerCase()))
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
                FilteredList = (ArrayList<Questions>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_question,tv_option_one,tv_option_two,tv_option_three,tv_option_four;
        LinearLayout border_layout,card_layout;

        MyViewHolder(View view) {
            super(view);
            tv_question = (TextView) view.findViewById(R.id.tv_question);
            tv_option_one = (TextView) view.findViewById(R.id.tv_option_one);
            tv_option_two = (TextView) view.findViewById(R.id.tv_option_two);
            tv_option_three = (TextView) view.findViewById(R.id.tv_option_three);
            tv_option_four = (TextView) view.findViewById(R.id.tv_option_four);
            border_layout = (LinearLayout) view.findViewById(R.id.border_layout);
            card_layout = (LinearLayout) view.findViewById(R.id.card_layout);
        }
    }


}
