package com.e.jcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.e.jcart.Model.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewOrder extends AppCompatActivity {
    TextView orderid,total;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference orderRef,allRef,catRef;
    Button cancle;
    String oid;
    int t=0;
    String myuid;
    ProductModel productModel;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Bundle bundle=getIntent().getExtras();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        myuid=user.getUid();
        if(bundle!=null){
            oid=bundle.getString("oid");
            orderRef = database.getReference("All Order").child(myuid).child(oid);
            catRef = database.getReference("All Category");
            allRef = database.getReference("All Product");
        }
        recyclerView=findViewById(R.id.myRecycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewOrder.this,LinearLayoutManager.VERTICAL,false));

        cancle=findViewById(R.id.my_cancle);
        orderid=findViewById(R.id.my_oid);
        orderid.setText("Order Id: "+oid);
        total=findViewById(R.id.my_total);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderRef.removeValue();
                Intent intent=new Intent(ViewOrder.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        FetchOrder();
    }

    private void FetchOrder() {
        FirebaseRecyclerOptions<ProductModel> options=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(orderRef,ProductModel.class).build();
        recyclerView.clearOnChildAttachStateChangeListeners();
        FirebaseRecyclerAdapter<ProductModel,MyOrderAdapter> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<ProductModel, MyOrderAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final MyOrderAdapter holder, int position, @NonNull final ProductModel model) {

                        final  String itemKey=getRef(position).getKey();
                        holder.setMyOrderItems(ViewOrder.this,model.getName(),model.getUrl(),model.getPrice(),model.getQuantity());
//                        if(Integer.parseInt(model.getRating())==1){
//                          holder.rate.setVisibility(View.INVISIBLE);
//                        }

                        t=t+(Integer.parseInt(model.getQuantity())*Integer.parseInt(model.getPrice()));
                        total.setText("Total: "+t+" \u20B9");
//                        holder.rate.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                allRef.child(model.getKey()).child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        allRef.child(model.getKey()).child("rating").setValue(""+(Integer.parseInt(snapshot.getValue().toString())+1));
//                                            catRef.child(model.getCat()).child(model.getKey()).child("rating").setValue(""+(Integer.parseInt(snapshot.getValue().toString())+1));
//                                            orderRef.child(itemKey).child("rating").setValue("1");
//                                            holder.rate.setVisibility(View.INVISIBLE);
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//                        });

                    }

                    @NonNull
                    @Override
                    public MyOrderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_item,parent,false);
                        return new MyOrderAdapter(view);
                    }
                };
        recyclerView.clearOnChildAttachStateChangeListeners();
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
