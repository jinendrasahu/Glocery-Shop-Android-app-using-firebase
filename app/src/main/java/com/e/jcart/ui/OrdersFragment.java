package com.e.jcart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.jcart.LoginActivity;
import com.e.jcart.Model.OrderModel;
import com.e.jcart.Model.ProductModel;
import com.e.jcart.OrderAdapter;
import com.e.jcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference orderRef;
    String currentUid;
    RecyclerView recyclerView;
    int total=0;
    List<OrderModel> orderitem=new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

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
            orderRef = database.getReference("All Order").child(currentUid);
        }
        recyclerView=getActivity().findViewById(R.id.order_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


        getOrders();

    }

    private void getOrders() {

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot items:snapshot.getChildren()){
                     final OrderModel orderModel=new OrderModel();
                     orderModel.setUid(currentUid);
                     orderModel.setOid(items.getKey());
                      total=0;
                     orderRef.child(items.getKey()).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             String s="";
                             for(DataSnapshot mydata:snapshot.getChildren()){
                    ProductModel pm=mydata.getValue(ProductModel.class);
                    int x=Integer.parseInt(pm.getPrice())*Integer.parseInt(pm.getQuantity());
                    total=total+x;
                    s=s+"<font color=\"green\">"+pm.getName()+"<br/></font>"+
//                            "\n"+
                            "<font><bold>Price: </bold></font>"+x+" \u20B9"+
//                            "\n"+
                            "<font><br/><bold>Quantity: </bold></font>"+pm.getQuantity()+"<br/><br/>"
//                            +"\n"
                    ;
                }
                             s=s+"<font color=\"red\"><br/><bold>Total: </bold></font>"+total+" \u20B9";
//                                             orderitem.add(s);
                                             orderModel.setOtext(s);
                                             orderitem.add(orderModel);
                             OrderAdapter orderAdapter=new OrderAdapter(orderitem);
                             recyclerView.setAdapter(orderAdapter);
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

    @Override
    public void onStart() {
        super.onStart();


    }
}