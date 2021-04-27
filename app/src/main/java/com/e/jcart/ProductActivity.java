package com.e.jcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.jcart.Model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {
    TextView name,price,desc,cat,rate;
    ImageView imageView;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference categoryProductRef,addtoCartRef;
    Button buy,addToCart;
    String mykey;
    String myuid;
    ProductModel productModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

       Bundle bundle=getIntent().getExtras();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        myuid=user.getUid();
        if(bundle!=null){
            String mycat=bundle.getString("cat");
            mykey=bundle.getString("key");
            categoryProductRef = database.getReference("All Category").child(mycat).child(mykey);
            addtoCartRef = database.getReference("All Cart").child(myuid);
        }
        productModel=new ProductModel();
        imageView=findViewById(R.id.pro_image);
        name=findViewById(R.id.pro_name);
        desc=findViewById(R.id.pro_desc);
        price=findViewById(R.id.pro_price);
        cat=findViewById(R.id.pro_cat);
        rate=findViewById(R.id.pro_rate);
        buy=findViewById(R.id.pro_buyBtn);
        addToCart=findViewById(R.id.pro_cartBtn);
//        Query connectedUser = categoryProductRef.equalTo(mykey);
        categoryProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> map=new HashMap<>();
                for (DataSnapshot item:snapshot.getChildren()){

                      map.put(item.getKey().toString(),item.getValue().toString());
                }
                Picasso.get().load(map.get("url")).into(imageView);
                price.setText(map.get("price")+" "+"\u20B9");
                desc.setText(map.get("desc"));
                cat.setText(map.get("cat"));
                name.setText(map.get("name"));
                rate.setText("Rated By "+map.get("rating")+" People");

                productModel.setName(map.get("name"));
                productModel.setCat(map.get("cat"));
                productModel.setSubcat(map.get("subcat"));
                productModel.setPrice(map.get("price"));
                productModel.setDesc(map.get("desc"));
                productModel.setQuantity("1");
                productModel.setRating(map.get("rating"));
                productModel.setUid(map.get("uid"));
                productModel.setUrl(map.get("url"));
                productModel.setTime(map.get("time"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoCartRef.child(mykey).setValue(productModel);
               Toast.makeText(ProductActivity.this,"Added",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
