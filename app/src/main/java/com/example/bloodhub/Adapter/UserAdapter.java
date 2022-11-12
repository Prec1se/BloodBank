package com.example.bloodhub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodhub.Model.User;
import com.example.bloodhub.R;

import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = userList.get(position);

        holder.userEmail.setText((user.getEmail()));
        holder.emailNow.setVisibility(View.GONE);
        holder.userName.setText(user.getName());
        holder.phoneNumber.setText(user.getPhonenumber());
//        holder.bg.setText(user.getBloodgroup());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView userProfileImage;
        public TextView userName, userEmail, phoneNumber, bg;
        public Button emailNow;

        public ViewHolder(@Nullable View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            userName = itemView.findViewById(R.id.name);
            userEmail = itemView.findViewById(R.id.email);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            emailNow = itemView.findViewById(R.id.emailNow);

        }
    }
}
