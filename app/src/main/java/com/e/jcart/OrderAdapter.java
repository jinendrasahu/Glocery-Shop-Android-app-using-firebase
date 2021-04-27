package com.e.jcart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.e.jcart.Model.OrderModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    List<OrderModel> data;
    public OrderAdapter (List<OrderModel> data){
        this.data = data;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items, parent, false);
        return new OrderAdapter.ViewHolder(rowItem);
    }


    @Override
    public void onBindViewHolder(final OrderAdapter.ViewHolder holder, final int position) {
        holder.pname.setText(Html.fromHtml(this.data.get(position).getOtext()));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView pname;
        private Button review;
        private ImageView pimage;
        private final Context context;


        public ViewHolder(View view) {
            super(view);
            context=view.getContext();
            this.pname = view.findViewById(R.id.order_name);
            this.pimage = view.findViewById(R.id.order_image);
            this.review = view.findViewById(R.id.order_review);

            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(),ViewOrder.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("uid",data.get(getAdapterPosition()).getUid());
                    bundle.putString("oid",data.get(getAdapterPosition()).getOid());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {
        }
    }
}
