package com.e.jcart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyCatAdapter extends RecyclerView.Adapter<MyCatAdapter.ViewHolder> {
     List<Integer> data;

    public MyCatAdapter(List<Integer> data){
        this.data = data;
    }

    @Override
    public MyCatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_items, parent, false);
        return new ViewHolder(rowItem);
    }
    @Override
    public void onBindViewHolder(final MyCatAdapter.ViewHolder holder, final int position) {
        Picasso.get().load(this.data.get(position)).into(holder.pimageView);

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView pimageView;
        private final Context context;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            context=view.getContext();
            this.pimageView = view.findViewById(R.id.hom_cat_buk);

        }
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(itemView.getContext(), ListProducts.class);
            Bundle bundle=new Bundle();
            if(data.get(getAdapterPosition()).equals(R.drawable._fruits)){
                bundle.putString("cat","Fruits");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }else if(data.get(getAdapterPosition()).equals(R.drawable._bekery)) {
                bundle.putString("cat", "Bekery");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
            else if(data.get(getAdapterPosition()).equals(R.drawable._vegetables)) {
                bundle.putString("cat", "Vegetables");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
            else if(data.get(getAdapterPosition()).equals(R.drawable._meat)) {
                bundle.putString("cat", "Meat");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
            else if(data.get(getAdapterPosition()).equals(R.drawable._icecream)) {
                bundle.putString("cat", "Icecreams");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        }
    }
}