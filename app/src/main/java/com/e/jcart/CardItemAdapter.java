package com.e.jcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.List;

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.ViewHolder> {
     List<ProductModel> data;
     private Bundle map=new Bundle();
//    private ImageView imageView,remove;
//    private TextView name,price,seelText;
//    private SeekBar quant;
FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference addToCartRef;
    String currentUid;
    public CardItemAdapter (List<ProductModel> data){
        this.data = data;
    }

    @Override
    public CardItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
        return new ViewHolder(rowItem);
    }
//    public Bundle getM(){
//        return this.map;
//    }

    @Override
    public void onBindViewHolder(final CardItemAdapter.ViewHolder holder, final int position) {
        holder.pname.setText(this.data.get(position).getName());
        holder.pprice.setText(this.data.get(position).getPrice());
        Picasso.get().load(this.data.get(position).getUrl()).into(holder.pimageView);
        holder.pseelText.setText(this.data.get(position).getQuantity());
        holder.pquant.setMax(Integer.parseInt(this.data.get(position).getQuantity()));
        holder.pquant.setProgress(1);
        final String key=this.data.get(position).getKey();
        addToCartRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.pseelText.setText(""+snapshot.getValue());
                holder.pquant.setProgress(Integer.parseInt(""+snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        holder.pseelText.setText(""+holder.pquant.getProgress());

//        map.putString(key,"1");
        holder.pquant.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                holder.pseelText.setText(""+progress);
//                map.putString(key,""+position);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                addToCartRef.child(key).setValue(""+seekBar.getProgress());
            }
        });

//        holder.premove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addToCartRef.child(key).removeValue();
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private ImageView pimageView,premove;
        private TextView pname,pprice,pseelText;
        private SeekBar pquant;


        public ViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            this.pname = view.findViewById(R.id.cart_name);
            this.pprice = view.findViewById(R.id.cart_price);
            this.pimageView = view.findViewById(R.id.cart_image);
            this.pseelText = view.findViewById(R.id.cart_seekText);
            this.premove = view.findViewById(R.id.cart_remove);
            this.pquant = view.findViewById(R.id.cart_seek);
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            currentUid=user.getUid();
            addToCartRef = database.getReference("All Cart").child(currentUid);

        }

        @Override
        public boolean onLongClick(View v) {

//            premove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
                    final int pos=getAdapterPosition();
                    addToCartRef.child(data.get(pos).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            data.remove(pos);
//                            notifyItemRemoved(pos);
                            if(data.isEmpty()){
                                map.clear();
                                notifyItemRemoved(pos);
                            }
                        }
                    });


//                }
//            });
            return true;
        }
    }
}