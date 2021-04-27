package com.e.jcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.jcart.Model.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ListProducts extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference cartRef,categoryProductRef;
    String currentUid,mycat;
    RecyclerView recyclerView;
    androidx.appcompat.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
           mycat =bundle.getString("cat");
        }
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(ListProducts.this, LoginActivity.class);
            startActivity(intent);
        }else {
            currentUid = user.getUid();
            categoryProductRef = database.getReference("All Category").child(mycat);
            cartRef = database.getReference("All Cart").child(currentUid);
        }
        searchView=findViewById(R.id.list_search);
        recyclerView=findViewById(R.id.recycle_prolist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListProducts.this,LinearLayoutManager.VERTICAL,false));

        LoadData("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null){
                    LoadData(newText);
                }else{
                    LoadData("");
                }
                return false;
            }
        });
    }

    private void LoadData(String data) {
        Query query=categoryProductRef.orderByChild("name").startAt(data).endAt(data+"\uf8ff");
        FirebaseRecyclerOptions<ProductModel> options=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(categoryProductRef,ProductModel.class).build();

        FirebaseRecyclerAdapter<ProductModel,ListProductAdapter> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<ProductModel, ListProductAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ListProductAdapter holder, int position, @NonNull final ProductModel model) {

                        final  String itemKey=getRef(position).getKey();
                        final String catTxt=getItem(position).getCat();


                        holder.setListItems(ListProducts.this,model.getName(),model.getUrl(),model.getPrice());
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ListProducts.this, ProductActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("key",itemKey);
                                bundle.putString("cat",catTxt);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        holder.fav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cartRef.child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            cartRef.child(itemKey).removeValue();
                                            holder.fav.setImageResource(R.drawable.ic_add);
                                        }else{
                                            cartRef.child(itemKey).setValue(model);
                                            holder.fav.setImageResource(R.drawable.ic_add_active);                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                        cartRef.child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    holder.fav.setImageResource(R.drawable.ic_add_active);
                                }else{
//                                    Picasso.get().load(R.drawable.ic_add).into(holder.fav);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ListProductAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
                        return new ListProductAdapter(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
