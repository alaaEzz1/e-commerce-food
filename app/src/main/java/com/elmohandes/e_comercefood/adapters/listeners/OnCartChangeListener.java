package com.elmohandes.e_comercefood.adapters.listeners;

import com.elmohandes.e_comercefood.models.CartModel;

import java.util.ArrayList;

public interface OnCartChangeListener {
    void onDataChangeListener(int position, ArrayList<CartModel> cartModelList);
}
