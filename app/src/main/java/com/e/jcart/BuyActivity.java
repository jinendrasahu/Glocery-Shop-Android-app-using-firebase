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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyActivity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference addToCartRef,allProductRef,myOrder;
    String currentUid;
    TextView total;
    RecyclerView recyclerView;
    Button buyCart;
    Bundle bundle;
    Integer countT=0;
    List<ProductModel> productModels=new ArrayList<>();
    BuyItemAdapter buyItemAdapter;
    List<ProductModel> order=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        bundle=getIntent().getExtras();
//        Log.d("jinu",bundle.toString());
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(BuyActivity.this, LoginActivity.class);
            startActivity(intent);
        }else {
            currentUid = user.getUid();
            addToCartRef = database.getReference("All Cart").child(currentUid);
            myOrder = database.getReference("All Order").child(currentUid);
            allProductRef= database.getReference("All Product");
        }
        buyCart=findViewById(R.id.buy_makepayment);
        total=findViewById(R.id.buy_total);
        recyclerView=findViewById(R.id.buy_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BuyActivity.this,LinearLayoutManager.VERTICAL,false));

//hello();
//      yo();

        hi();
    }

    private void hi() {
        FirebaseRecyclerOptions<ProductModel> options=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(addToCartRef,ProductModel.class).build();

        FirebaseRecyclerAdapter<ProductModel, BuyAdapter> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<ProductModel, BuyAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final BuyAdapter holder, int position, @NonNull final ProductModel model) {

                        final String itemKey = getRef(position).getKey();

                        model.setQuantity(bundle.getString(itemKey));
                        order.add(model);
                                    holder.setBuyItem(BuyActivity.this, model.getName(), model.getUrl(), model.getPrice());
                                                    holder.quant.setText("Quantity: "+bundle.getString(itemKey));

                                                    countT = countT + (Integer.parseInt(bundle.getString(itemKey)) * Integer.parseInt(model.getPrice()));
                                                    total.setText("Total: " + countT+" "+"\u20B9");

                    }

                    @NonNull
                    @Override
                    public BuyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_items,parent,false);
                        return new BuyAdapter(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        buyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nid=myOrder.push().getKey();
                myOrder.child(nid).setValue(order);
            }
        });
    }

    private void yo() {
        addToCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
//                productModels=new ProductModel[snapshot.getChildrenCount()]
                productModels.clear();
                for (final DataSnapshot item : snapshot.getChildren()) {
//                    Log.d("jinu",bundle.getString(item.getKey(),"1"));
                    DatabaseReference newRef = allProductRef.child(item.getKey());
                    newRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot shot) {

                            ProductModel model = shot.getValue(ProductModel.class);
                            model.setQuantity(item.getValue().toString());
//                            model.setQuantity(bundle.getString(item.getKey(),"1"));
                            countT = countT + (Integer.parseInt(item.getValue().toString()) * Integer.parseInt(model.getPrice()));
                            total.setText("" + countT);
                            productModels.add(model);
                            buyItemAdapter = new BuyItemAdapter(productModels);
                            recyclerView.setAdapter(buyItemAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hello() {
        FirebaseRecyclerOptions<ProductModel> options=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(allProductRef,ProductModel.class).build();

        FirebaseRecyclerAdapter<ProductModel, BuyAdapter> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<ProductModel, BuyAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final BuyAdapter holder, int position, @NonNull final ProductModel model) {

                        final String itemKey = getRef(position).getKey();
                        DatabaseReference newRef = addToCartRef.child(itemKey);
                        newRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

//                                                    Log.d("hi",bundle.getString(itemKey)+"  "+model.getPrice());
                                    holder.setBuyItem(BuyActivity.this, model.getName(), model.getUrl(), model.getPrice());
//                                                    holder.quant.setText(bundle.getString(itemKey));

//                                                    countT = countT + (Integer.parseInt(bundle.getString(itemKey)) * Integer.parseInt(model.getPrice()));
//                                                    total.setText("" + countT);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public BuyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_items,parent,false);
                        return new BuyAdapter(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        buyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BuyActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
