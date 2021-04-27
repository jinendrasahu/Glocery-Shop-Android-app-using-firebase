package com.e.jcart;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

public class CategoryAdaapter extends RecyclerView.ViewHolder {
    public ImageView imageView;
    TextView name,price;
    public CategoryAdaapter(@NonNull View itemView) {
        super(itemView);
    }
    public  void setCategoryItem(FragmentActivity activity, String mname, String url, String mprice){
        imageView=itemView.findViewById(R.id.item_image);
        name=itemView.findViewById(R.id.item_name);
        price=itemView.findViewById(R.id.item_price);

        Picasso.get().load(url).into(imageView);
        name.setText(mname);
        price.setText(mprice+" "+"\u20B9");
    }

}

