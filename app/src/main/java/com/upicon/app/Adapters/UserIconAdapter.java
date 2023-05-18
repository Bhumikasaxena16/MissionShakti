package com.upicon.app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.upicon.app.Admin.DepartmentList;
import com.upicon.app.Admin.QuestionList;
import com.upicon.app.Admin.UserList;
import com.upicon.app.Agent.AddApplication;
import com.upicon.app.Agent.Applications;
import com.upicon.app.Agent.SurveyList;
import com.upicon.app.Models.UserIcon;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;

import java.util.List;

public class UserIconAdapter extends RecyclerView.Adapter<UserIconAdapter.MyViewHolder> {

    private List<UserIcon> OriginalList;
    private Context context;

    public UserIconAdapter(Context context, List<UserIcon> OriginalList) {
        this.context = context;
        this.OriginalList = OriginalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_emp_icon, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final UserIcon userIcon = OriginalList.get(position);

        Picasso.get().load(userIcon.getImage()).placeholder(R.drawable.ic_default_image).into(holder.iv_image);
        holder.tv_title.setText(userIcon.getTitle());

        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tv_title.getText().equals("Survey")){
                    Intent intent=new Intent(context, SurveyList.class);
                    intent.putExtra("status","New");
                    context.startActivity(intent);
                }
                else if (holder.tv_title.getText().equals("Questionnaire")){
                    Intent intent=new Intent(context, QuestionList.class);
                    //intent.putExtra("dept_id", UtilsMethod.INSTANCE.getDeptId(context));
                    context.startActivity(intent);
                }

                else if (holder.tv_title.getText().equals("Surveyors")){
                    Intent intent=new Intent(context, UserList.class);
                    intent.putExtra("status","Users");
                    context.startActivity(intent);
                }
                else if (holder.tv_title.getText().equals("Departments")){
                    Intent intent=new Intent(context, DepartmentList.class);
                    intent.putExtra("status","Pending");
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return OriginalList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_title;
        LinearLayout card_layout;

        MyViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            card_layout = (LinearLayout) view.findViewById(R.id.card_layout);
        }
    }

}
