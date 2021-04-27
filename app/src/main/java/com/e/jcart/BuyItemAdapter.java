package com.e.jcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.jcart.Model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuyItemAdapter extends RecyclerView.Adapter<BuyItemAdapter.ViewHolder> {
    List<ProductModel> data;
    private Bundle map=new Bundle();
    public BuyItemAdapter (List<ProductModel> data){
        this.data = data;
    }

    @Override
    public BuyItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_items, parent, false);
        return new BuyItemAdapter.ViewHolder(rowItem);
    }
    public Bundle getM(){
        return map;
    }

    @Override
    public void onBindViewHolder(final BuyItemAdapter.ViewHolder holder, final int position) {
        holder.pname.setText(this.data.get(position).getName());
        holder.pprice.setText(this.data.get(position).getPrice());
        Picasso.get().load(this.data.get(position).getUrl()).into(holder.pimageView);
        holder.pquant.setText(this.data.get(position).getQuantity());


    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView pimageView;
        private TextView pname, pprice, pquant;


        public ViewHolder(View view) {
            super(view);
            this.pname = view.findViewById(R.id.buy_name);
            this.pprice = view.findViewById(R.id.buy_price);
            this.pimageView = view.findViewById(R.id.buy_image);
            this.pquant = view.findViewById(R.id.buy_quantity);


        }
    }
}
