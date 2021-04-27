package com.e.jcart;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class CartAdapter extends RecyclerView.ViewHolder {
    public ImageView imageView,remove;
    public TextView name,price,seelText;
    public SeekBar quant;
    public CartAdapter(@NonNull View itemView) {
        super(itemView);
    }
    public  void setCartItems(final FragmentActivity activity, String mname, String url, String mprice, String mquantity){
        imageView=itemView.findViewById(R.id.cart_image);
        remove=itemView.findViewById(R.id.cart_remove);
        name=itemView.findViewById(R.id.cart_name);
        quant=itemView.findViewById(R.id.cart_seek);
        seelText=itemView.findViewById(R.id.cart_seekText);
        price=itemView.findViewById(R.id.cart_price);
        Log.d("hello",mprice+" "+mname);

        Picasso.get().load(url).into(imageView);
        name.setText(mname);
        price.setText(mprice+" "+"\u20B9");
        quant.setMax(50);
        quant.setProgress(1);

        seelText.setText("1");

    }
}
