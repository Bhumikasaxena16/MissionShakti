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

import com.upicon.app.Models.Audit;
import com.upicon.app.R;

import java.util.ArrayList;
import java.util.List;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.MyViewHolder> implements Filterable {

    private List<Audit> OriginalList;
    private List<Audit> FilteredList;
    private Context context;

    public AuditAdapter(Context context, List<Audit> OriginalList) {
        this.context = context;
        this.OriginalList = OriginalList;
        this.FilteredList = OriginalList;
        notifyItemChanged(0, FilteredList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_audit, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Audit audit = FilteredList.get(position);


        holder.tv_business_name.setText((position+1)+". "+audit.getBusiness_name());
        holder.tv_contact.setText(audit.getContact_number());
        holder.tv_establishment_year.setText("Since: "+audit.getEstablishment_year());
        holder.tv_nature_of_operation .setText(audit.getNature_of_operation());
        holder.tv_type_of_unit.setText(audit.getType_of_unit());
        holder.tv_nature_of_job.setText(audit.getNature_of_job());
        holder.tv_employment_type.setText(audit.getEmployment_type());
        holder.tv_vendor_agreement.setText(audit.getSv_agreement());
        holder.tv_suppliers_increased.setText(audit.getSuppliers_increase());
        holder.tv_address.setText(audit.getAddress()+" "+audit.getDistrict()+" "+audit.getDistrict()+"- "+audit.getPincode());
        holder.tv_created_at.setText(audit.getCreated_at());


        if (audit.getStatus().equals("1")){
            holder.border_layout.setBackgroundResource(R.color.approved_color);

        }
        else if (audit.getStatus().equals("0")){
            holder.border_layout.setBackgroundResource(R.color.rejected_color);
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
                    List<Audit> filteredList = new ArrayList<>();
                    for (Audit list : OriginalList) {
                        if (list.getBusiness_name().toLowerCase().contains(charString.toLowerCase())||
                                list.getContact_number().toLowerCase().contains(charString.toLowerCase())||
                                list.getDistrict().toLowerCase().contains(charString.toLowerCase())||
                                list.getState().toLowerCase().contains(charString.toLowerCase())||
                                list.getBusiness_premises().toLowerCase().contains(charString.toLowerCase())||
                                list.getNature_of_operation().toLowerCase().contains(charString.toLowerCase())||
                                list.getType_of_unit().toLowerCase().contains(charString.toLowerCase())||
                                list.getNature_of_job().toLowerCase().contains(charString.toLowerCase())||
                                list.getEmployment_type().toLowerCase().contains(charString.toLowerCase())||
                                list.getSv_agreement().toLowerCase().contains(charString.toLowerCase())||
                                list.getSuppliers_increase().toLowerCase().contains(charString.toLowerCase()))
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
                FilteredList = (ArrayList<Audit>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_business_name,tv_contact,tv_establishment_year,tv_nature_of_operation,tv_type_of_unit, tv_nature_of_job,tv_employment_type,tv_vendor_agreement,tv_suppliers_increased,tv_address,tv_created_at;
        LinearLayout border_layout,card_layout;

        MyViewHolder(View view) {
            super(view);
            tv_business_name = (TextView) view.findViewById(R.id.tv_business_name);
            tv_contact = (TextView) view.findViewById(R.id.tv_contact);
            tv_establishment_year = (TextView) view.findViewById(R.id.tv_establishment_year);
            tv_nature_of_operation = (TextView) view.findViewById(R.id.tv_nature_of_operation);
            tv_type_of_unit = (TextView) view.findViewById(R.id.tv_type_of_unit);
            tv_nature_of_job = (TextView) view.findViewById(R.id.tv_nature_of_job);
            tv_employment_type = (TextView) view.findViewById(R.id.tv_employment_type);
            tv_vendor_agreement = (TextView) view.findViewById(R.id.tv_vendor_agreement);
            tv_suppliers_increased = (TextView) view.findViewById(R.id.tv_suppliers_increased);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_created_at = (TextView) view.findViewById(R.id.tv_created_at);
            border_layout = (LinearLayout) view.findViewById(R.id.border_layout);
            card_layout = (LinearLayout) view.findViewById(R.id.card_layout);
        }
    }

}
