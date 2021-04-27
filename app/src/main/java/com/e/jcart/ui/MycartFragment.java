package com.e.jcart.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.jcart.BuyActivity;
import com.e.jcart.CardItemAdapter;
import com.e.jcart.CartAdapter;
import com.e.jcart.CategoryAdaapter;
import com.e.jcart.ListProductAdapter;
import com.e.jcart.ListProducts;
import com.e.jcart.LoginActivity;
import com.e.jcart.Model.ProductModel;
import com.e.jcart.ProductActivity;
import com.e.jcart.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MycartFragment extends Fragment {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference allProductRef,addToCartRef;
    String currentUid;
    TextView catTitle1;
    RecyclerView recyclerView;
    Button buyCart;
    Bundle bundle=new Bundle();
    List<ProductModel> productModels=new ArrayList<>();
    CardItemAdapter cardItemAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mycart, container, false);






        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }else {
            currentUid = user.getUid();
            allProductRef = database.getReference("All Product");
            addToCartRef = database.getReference("All Cart").child(currentUid);
        }
        buyCart=getActivity().findViewById(R.id.myCart_Buy);
        recyclerView=getActivity().findViewById(R.id.cart_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ProductModel> options=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(addToCartRef,ProductModel.class).build();

        final FirebaseRecyclerAdapter<ProductModel, CartAdapter> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<ProductModel, CartAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartAdapter holder, final int position, @NonNull final ProductModel model) {
//
                        final String itemKey = getRef(position).getKey();

                        bundle.putString(itemKey,"1");

                        holder.setCartItems(getActivity(),model.getName(),model.getUrl(),model.getPrice(),model.getQuantity());
                        holder.remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addToCartRef.child(itemKey).removeValue();
                            }
                        });
                        holder.quant.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                holder.seelText.setText(""+progress);
                                bundle.putString(itemKey,""+progress);
                            }
                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                   }
//                                 }

                    @NonNull
                    @Override
                    public CartAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items,parent,false);
                        return new CartAdapter(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        buyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), BuyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void Myadapter() {

        FirebaseRecyclerOptions<ProductModel> options=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(allProductRef,ProductModel.class).build();

        final FirebaseRecyclerAdapter<ProductModel, CartAdapter> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<ProductModel, CartAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartAdapter holder, final int position, @NonNull final ProductModel model) {

                        final String itemKey = getRef(position).getKey();

                        final DatabaseReference newRef = addToCartRef.child(itemKey);
                        newRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    holder.setCartItems(getActivity(), model.getName(), model.getUrl(), model.getPrice(), model.getQuantity());
                                    bundle.putString(itemKey, "" + 1);
                                    holder.remove.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            bundle.remove(itemKey);
                                            newRef.removeValue();
//                                            notifyItemRemoved(position);

//                                            MycartFragment.this.notify();
                                            Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    holder.quant.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            holder.seelText.setText("" + progress);
                                            bundle.putString(itemKey, "" + progress);
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
//                                 }

                    @NonNull
                    @Override
                    public CartAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items,parent,false);
                        return new CartAdapter(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    private  void NewAdapter(){
        addToCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
//                productModels=new ProductModel[snapshot.getChildrenCount()]
                productModels.clear();
                recyclerView.setAdapter(null);

                for (DataSnapshot item : snapshot.getChildren()) {

                    DatabaseReference newRef = allProductRef.child(item.getKey());
                    newRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot shot) {
                            ProductModel model = shot.getValue(ProductModel.class);
                            productModels.add(model);
                            cardItemAdapter = new CardItemAdapter(productModels);
                            recyclerView.setAdapter(cardItemAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


//                    final HashMap<String, String> hashMap = new HashMap<>();
//                    addToCartRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            hashMap.put(snapshot.getKey(), "1");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                  Myadapter();

//                    Log.d("mesg",productModels.toString());
//
//                }

//                CardItemAdapter cardItemAdapter=new CardItemAdapter(productModels);
//                    cardItemAdapter = new CardItemAdapter(productModels);
//                    recyclerView.setAdapter(cardItemAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle= new Bundle(cardItemAdapter.getM());
//                Log.d("jinu",bundle.toString());
                Intent intent=new Intent(getActivity(), BuyActivity.class);
//                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//        buyCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), BuyActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
    }
}