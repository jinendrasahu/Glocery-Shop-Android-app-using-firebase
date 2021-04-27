package com.e.jcart;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class BuyAdapter extends RecyclerView.ViewHolder {
    public ImageView imageew;
    TextView name,price;
    public TextView quant;
    public BuyAdapter(@NonNull View itemView) {
        super(itemView);
    }
    public  void setBuyItem(Activity activity, String mname, String url, String mprice){
        imageew=itemView.findViewById(R.id.buy_image);
        name=itemView.findViewById(R.id.buy_name);
        price=itemView.findViewById(R.id.buy_price);
        quant=itemView.findViewById(R.id.buy_quantity);

        Picasso.get().load(url).into(imageew);
        name.setText(mname);
        price.setText(mprice+" "+"\u20B9");
    }

}

