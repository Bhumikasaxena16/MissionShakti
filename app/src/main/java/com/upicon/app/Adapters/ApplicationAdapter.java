package com.upicon.app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.upicon.app.Agent.Applications;
import com.upicon.app.Models.Application;
import com.upicon.app.Models.User;
import com.upicon.app.R;


import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.MyViewHolder> implements Filterable {

    private List<Application> OriginalList;
    private List<Application> FilteredList;
    private Context context;

    public ApplicationAdapter(Context context, List<Application> OriginalList) {
        this.context = context;
        this.OriginalList = OriginalList;
        this.FilteredList = OriginalList;
        notifyItemChanged(0, FilteredList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_application, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Application application = FilteredList.get(position);


        holder.tv_applicant_name.setText(application.getFirst_name()+" "+application.getLast_name());
        holder.tv_mobile_number.setText(application.getMobile());
        holder.tv_district.setText(application.getDistrict());
        holder.tv_created_at.setText(application.getCreated_at());


        if (application.getApplication_status().equals("Approved")){
            holder.border_layout.setBackgroundResource(R.color.approved_color);
        }
        else if (application.getApplication_status().equals("Pending")){
            holder.border_layout.setBackgroundResource(R.color.pending_color);
        }


        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog(holder, user.getId(), user.getStatus());
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
                    List<Application> filteredList = new ArrayList<>();
                    for (Application list : OriginalList) {
                        if (list.getFirst_name().toLowerCase().contains(charString.toLowerCase())||
                                list.getLast_name().toLowerCase().contains(charString.toLowerCase())||
                                list.getMobile().toLowerCase().contains(charString.toLowerCase())||
                                list.getAadhaar().toLowerCase().contains(charString.toLowerCase())||
                                list.getRo_code().toLowerCase().contains(charString.toLowerCase()))
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
                FilteredList = (ArrayList<Application>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_applicant_name,tv_mobile_number,tv_district,tv_status,tv_created_at;
        LinearLayout border_layout,card_layout;

        MyViewHolder(View view) {
            super(view);
            tv_applicant_name = (TextView) view.findViewById(R.id.tv_applicant_name);
            tv_mobile_number = (TextView) view.findViewById(R.id.tv_mobile_number);
            tv_mobile_number = (TextView) view.findViewById(R.id.tv_mobile_number);
            tv_district=(TextView)view.findViewById(R.id.tv_district);
            tv_created_at = (TextView) view.findViewById(R.id.tv_created_at);
            border_layout = (LinearLayout) view.findViewById(R.id.border_layout);
            card_layout = (LinearLayout) view.findViewById(R.id.card_layout);
        }
    }

}
