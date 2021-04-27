package com.e.jcart;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MyOrderAdapter extends RecyclerView.ViewHolder {
    public ImageView imageView,rate;
    TextView name,price,quantity;
    public MyOrderAdapter(@NonNull View itemView) {
        super(itemView);
    }
    public  void setMyOrderItems(Activity activity, String mname, String url, String mprice,String mquntity){
        imageView=itemView.findViewById(R.id.my_image);
        name=itemView.findViewById(R.id.my_name);
        price=itemView.findViewById(R.id.my_price);
        quantity=itemView.findViewById(R.id.my_quantity);
        rate=itemView.findViewById(R.id.my_rate);

        Picasso.get().load(url).into(imageView);
        name.setText(mname);
        quantity.setText(mquntity);
        price.setText(mprice+" "+"\u20B9");
    }
}
