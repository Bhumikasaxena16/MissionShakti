package com.upicon.app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.upicon.app.Admin.QuestionList;
import com.upicon.app.Agent.Applications;
import com.upicon.app.Models.Application;
import com.upicon.app.Models.Department;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyViewHolder> implements Filterable {

    private List<Department> OriginalList;
    private List<Department> FilteredList;
    private Context context;

    public DepartmentAdapter(Context context, List<Department> OriginalList) {
        this.context = context;
        this.OriginalList = OriginalList;
        this.FilteredList = OriginalList;
        notifyItemChanged(0, FilteredList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_department, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Department department = FilteredList.get(position);


        holder.tv_dept_name.setText(department.getName());
        holder.tv_dept_code.setText(department.getCode());
        holder.tv_dept_mobile.setText(department.getMobile());
        holder.tv_dept_alt_mobile.setText(department.getAlt_mobile());
        holder.tv_dept_email.setText(department.getEmail());
        holder.tv_dept_website.setText(department.getWebsite());

        holder.tv_dept_district.setText(department.getDistrict());

        holder.tv_dept_state.setText(department.getState());

        holder.tv_created_at.setText(department.getCreated_at());


        if (department.getStatus().equals("1")){
            holder.border_layout.setBackgroundResource(R.color.approved_color);
        }
        else if (department.getStatus().equals("0")){
            holder.border_layout.setBackgroundResource(R.color.pending_color);
        }

        holder.tv_dept_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethod.INSTANCE.callToNumber(context,department.getMobile());
            }
        });

        holder.tv_dept_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethod.INSTANCE.sendEmail(context,department.getEmail());
            }
        });


        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, QuestionList.class);
                intent.putExtra("dept_id", department.getId());
                context.startActivity(intent);
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
                    List<Department> filteredList = new ArrayList<>();
                    for (Department list : OriginalList) {
                        if (list.getName().toLowerCase().contains(charString.toLowerCase())||
                                list.getAlt_mobile().toLowerCase().contains(charString.toLowerCase())||
                                list.getMobile().toLowerCase().contains(charString.toLowerCase())||
                                list.getDistrict().toLowerCase().contains(charString.toLowerCase())||
                                list.getState().toLowerCase().contains(charString.toLowerCase())||
                                list.getCode().toLowerCase().contains(charString.toLowerCase()))
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
                FilteredList = (ArrayList<Department>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_dept_name,tv_dept_code,tv_dept_mobile,tv_dept_alt_mobile,tv_dept_email,tv_dept_website,tv_dept_district,tv_dept_state,tv_created_at;
        LinearLayout border_layout,card_layout;

        MyViewHolder(View view) {
            super(view);
            tv_dept_name = (TextView) view.findViewById(R.id.tv_dept_name);
            tv_dept_code = (TextView) view.findViewById(R.id.tv_dept_code);
            tv_dept_mobile = (TextView) view.findViewById(R.id.tv_dept_mobile);
            tv_dept_alt_mobile = (TextView) view.findViewById(R.id.tv_dept_alt_mobile);
            tv_dept_email = (TextView) view.findViewById(R.id.tv_dept_email);
            tv_dept_website = (TextView) view.findViewById(R.id.tv_dept_website);
            tv_dept_district = (TextView) view.findViewById(R.id.tv_dept_district);
            tv_dept_state = (TextView) view.findViewById(R.id.tv_dept_state);
            tv_created_at = (TextView) view.findViewById(R.id.tv_created_at);
            border_layout = (LinearLayout) view.findViewById(R.id.border_layout);
            card_layout = (LinearLayout) view.findViewById(R.id.card_layout);
        }
    }

}
