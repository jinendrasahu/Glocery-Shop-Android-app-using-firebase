package com.e.jcart;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ListProductAdapter extends RecyclerView.ViewHolder {
    public ImageView imageView,fav;
    TextView name,price;
    public ListProductAdapter(@NonNull View itemView) {
        super(itemView);
    }
    public  void setListItems(Activity activity, String mname, String url, String mprice){
        imageView=itemView.findViewById(R.id.list_image);
        name=itemView.findViewById(R.id.list_name);
        price=itemView.findViewById(R.id.list_price);
        fav=itemView.findViewById(R.id.list_fav);

        Picasso.get().load(url).into(imageView);
        name.setText(mname);
        price.setText(mprice+" "+"\u20B9");
    }
    public void addFav(String itemKey){
//        cartRef.child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    cat.child(itemKey).removeValue();
//                    Picasso.get().load(R.drawable.ic_add_active).into(holder.fav);
//                }else{
//                    categoryProductRef.child(itemKey).setValue(model);
//                    Picasso.get().load(R.drawable.ic_add).into(holder.fav);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

}