package com.elmohandes.e_comercefood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.UsersItemBinding;
import com.elmohandes.e_comercefood.models.Users;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersVH> {

    private Context context;
    private List<Users> usersList;

    public UsersAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UsersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersVH(LayoutInflater.from(context).inflate(R.layout.users_item,
                parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersVH holder, int position) {
        Users users = usersList.get(position);
        holder.binding.usersName.setText(users.getFullName());
        holder.binding.usersEmail.setText(users.getEmail());
        holder.binding.usersPhone.setText(users.getPhone());
        Glide.with(context).load(users.getImgUrl()).placeholder(R.mipmap.ic_person)
                .into(holder.binding.usersImg);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UsersVH extends RecyclerView.ViewHolder{
        UsersItemBinding binding;
        public UsersVH(@NonNull View itemView) {
            super(itemView);
            binding = UsersItemBinding.bind(itemView);
        }
    }

}
