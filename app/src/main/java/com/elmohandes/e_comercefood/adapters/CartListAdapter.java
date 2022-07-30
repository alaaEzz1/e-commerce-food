package com.elmohandes.e_comercefood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.listeners.OnCartChangeListener;
import com.elmohandes.e_comercefood.databinding.CartItemsBinding;
import com.elmohandes.e_comercefood.models.CartModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartListVH> {

    private Context context;
    private ArrayList<CartModel> cartModels;
    OnCartChangeListener listener;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public CartListAdapter(Context context, ArrayList<CartModel> cartModels, OnCartChangeListener listener) {
        this.context = context;
        this.cartModels = cartModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartListVH(LayoutInflater.from(context).inflate(R.layout.cart_items
                ,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartListVH holder, int position) {
        CartModel model = cartModels.get(position);
        holder.binding.cartItemTitle.setText(model.getFoodModel().getTitle());
        holder.binding.cartItemPrice.setText("$ "+model.getFoodModel().getFee());
        holder.binding.cartItemTotalPrice.setText("$ "+Math.round(model.getFoodModel().getNumberInCart()
                * model.getFoodModel().getFee() * 100)/100);
        holder.binding.cartItemNumber.setText(String.valueOf(model.getFoodModel().getNumberInCart()));
        Glide.with(context).load(model.getFoodModel().getPic()).into(holder.binding.cardItemImg);

        holder.binding.cartItemImgPlus.setOnClickListener(v -> {
            setPlusCartFireStore(position,cartModels,holder);
        });

        holder.binding.cartItemImgMinus.setOnClickListener(v -> {
            setMinusCartFireStore(position,cartModels,holder);
        });
    }

    private void setMinusCartFireStore(int position, ArrayList<CartModel> cartModels,
                                       CartListVH holder) {

        if (cartModels.get(position).getFoodModel().getNumberInCart() == 1){
            firestore.collection("cart").document(cartModels.get(position).getCartId())
                    .delete();
            cartModels.remove(position);
        }else {
            cartModels.get(position).getFoodModel().setNumberInCart(cartModels.get(position)
                    .getFoodModel().getNumberInCart()-1);
            firestore.collection("cart").document(cartModels.get(position).getCartId())
                    .set(cartModels.get(position));
            holder.binding.cartItemNumber.setText(String.
                    valueOf(cartModels.get(position).getFoodModel().getNumberInCart()));
            listener.onDataChangeListener(position,cartModels);
        }
        notifyDataSetChanged();

    }

    private void setPlusCartFireStore(int position, ArrayList<CartModel> cartModels,
                                      CartListVH holder) {
        cartModels.get(position).getFoodModel().setNumberInCart(cartModels.get(position).
                getFoodModel().getNumberInCart()+1);
        firestore.collection("cart").document(cartModels.get(position).getCartId())
                .set(cartModels.get(position));
        holder.binding.cartItemNumber.setText(String.
                valueOf(cartModels.get(position).getFoodModel().getNumberInCart()));
        listener.onDataChangeListener(position,cartModels);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    public class CartListVH extends RecyclerView.ViewHolder{

        CartItemsBinding binding;

        public CartListVH(@NonNull View itemView) {
            super(itemView);

            binding = CartItemsBinding.bind(itemView);

        }
    }

}
