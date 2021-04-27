package com.e.jcart.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.jcart.BuyItemAdapter;
import com.e.jcart.CategoryAdaapter;
import com.e.jcart.ListProducts;
import com.e.jcart.LoginActivity;
import com.e.jcart.Model.ProductModel;
import com.e.jcart.MyCatAdapter;
import com.e.jcart.ProductActivity;
import com.e.jcart.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference  categoryProductRef;
    String currentUid;
    TextView catTitle1,catTitle2,catTitle3,catTitle4,catTitle5,catTitle6;
    RecyclerView recyclerView1,recyclerView2,recyclerView3,recyclerView4,recyclerView5,recyclerView6,catRecycleview;

    List<Integer> cats= Arrays.asList(R.drawable._fruits,R.drawable._bekery,R.drawable._vegetables,R.drawable._meat,R.drawable._icecream);
    boolean yes;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
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
            categoryProductRef = database.getReference("All Category");
        }
        catTitle1=getActivity().findViewById(R.id.cat_title1);
        catTitle2=getActivity().findViewById(R.id.cat_title2);
        catTitle3=getActivity().findViewById(R.id.cat_title3);
        catTitle4=getActivity().findViewById(R.id.cat_title4);
        catTitle5=getActivity().findViewById(R.id.cat_title5);
        catTitle6=getActivity().findViewById(R.id.cat_title6);

//        catTitle1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), ListProducts.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("cat","Fruits");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//        catTitle2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), ListProducts.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("cat","Bekery");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//        catTitle3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), ListProducts.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("cat","Vegetables");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//        catTitle4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), ListProducts.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("cat","Meat");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//        catTitle5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), ListProducts.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("cat","Icecreams");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//        catTitle6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), ListProducts.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("cat","Breakfast");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
        recyclerView1=getActivity().findViewById(R.id.hom_recy1);
        recyclerView2=getActivity().findViewById(R.id.hom_recy2);
        recyclerView3=getActivity().findViewById(R.id.hom_recy3);
        recyclerView4=getActivity().findViewById(R.id.hom_recy4);
        recyclerView5=getActivity().findViewById(R.id.hom_recy5);
//        recyclerView6=getActivity().findViewById(R.id.hom_recy6);
        catRecycleview=getActivity().findViewById(R.id.hom_cat);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView5.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
//        recyclerView6.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        catRecycleview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        getAllCategory();
    }

    @Override
    public void onStart() {
        super.onStart();

        AtachRecycleview(recyclerView1,catTitle1,"Fruits");
        AtachRecycleview(recyclerView2,catTitle2,"Bekery");
        AtachRecycleview(recyclerView3,catTitle3,"Vegetables");
        AtachRecycleview(recyclerView4,catTitle4,"Meat");
        AtachRecycleview(recyclerView5,catTitle5,"Icecreams");


    }

    private void getAllCategory() {
        MyCatAdapter myCatAdapter= new MyCatAdapter(cats);
        catRecycleview.setAdapter(myCatAdapter);
    }

    public void AtachRecycleview(RecyclerView myrecyclerView, final TextView mytext, final String mycat){
        yes=false;
        DatabaseReference newRef=categoryProductRef.child(mycat);
        FirebaseRecyclerOptions<ProductModel> options=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(newRef,ProductModel.class).build();

        FirebaseRecyclerAdapter<ProductModel,CategoryAdaapter> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<ProductModel, CategoryAdaapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategoryAdaapter holder, int position, @NonNull ProductModel model) {
//                        if(!yes){
                            mytext.setVisibility(View.VISIBLE);
                            mytext.setText(mycat);
//                            yes=true;
//                        }

                        final  String itemKey=getRef(position).getKey();
                        final String catTxt=getItem(position).getCat();


                        holder.setCategoryItem(getActivity(),model.getName(),model.getUrl(),model.getPrice());
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), ProductActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("key",itemKey);
                                bundle.putString("cat",catTxt);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public CategoryAdaapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items,parent,false);
                        return new CategoryAdaapter(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        myrecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}