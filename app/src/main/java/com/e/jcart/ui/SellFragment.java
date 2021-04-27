package com.e.jcart.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.e.jcart.CreateProfileActivity;
import com.e.jcart.HomeActivity;
import com.e.jcart.LoginActivity;
import com.e.jcart.Model.ProductModel;
import com.e.jcart.Model.UserModel;
import com.e.jcart.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class SellFragment extends Fragment {

    EditText pname,pdesc,pprice,quantity;
    ImageView pImage;
    Spinner category,subcat;
    String[] cat={"Choose Type","Fruits","Bekery","Vegetables","Meat","Icecreams"};
    String[] Fruits={"Choose Type","Lemon","Orange","Pineapple","Raspberry","Redcherry"};
    int[] fruitsI={R.drawable._lemon,R.drawable._orange,R.drawable._pineapple,R.drawable._raspberry,R.drawable._redcherry};
    String[] Bekery={"Choose Type","Bread","Brown Bread","Whute Bread","Donut","Cookies"};
    int[] bekeryI={R.drawable._bread,R.drawable._brownbread,R.drawable._whutebread,R.drawable._donut,R.drawable._cookies};
    String[] Vegetables={"Choose Type","Tomato","Cabbage","Corn","Eggplant","Potato"};
    int[] vegetableI={R.drawable._tomato,R.drawable._cabbage,R.drawable._corn,R.drawable._eggplant,R.drawable._potato};
    String[] Meat={"Choose Type","Beef","Fish","Turkey","Chicken","Hot Dogs"};
    int[] meatI={R.drawable._beef,R.drawable._fish,R.drawable._turcky,R.drawable._chicken,R.drawable._hotdogs};
    String[] Icecreams={"Choose Type","Softy","Chocobar","Corn IceCream","Cup IceCream","CornFlack"};
    int[] icecreamI={R.drawable._softy,R.drawable._chocobar,R.drawable._cornicecream,R.drawable._cupicecream,R.drawable._cornflack};
    int[] imageArray;


    Button upload;
    CardView card;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference userProductRef,categoryProductRef,allProjectRef;
    StorageReference storageReference;
    final static int PICK_IMAGE=1;
    UploadTask uploadTask;
    Uri imageUri;
    String currentUid;
    ProductModel productModel;
    ArrayAdapter subadapter;
    ProgressBar pb;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sell, container, false);


        category=root.findViewById(R.id.sell_category);
        pname=root.findViewById(R.id.sell_prodname);
        pdesc=root.findViewById(R.id.sell_desc);
        pprice=root.findViewById(R.id.sell_price);
        quantity=root.findViewById(R.id.sell_quantity);
        category=root.findViewById(R.id.sell_category);
        subcat=root.findViewById(R.id.sell_subcat);
        upload=root.findViewById(R.id.sell_btn);
        pImage=root.findViewById(R.id.sell_image);
        card=root.findViewById(R.id.sell_card);
        pb=root.findViewById(R.id.sell_pb);
        productModel=new ProductModel();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }else {
            currentUid = user.getUid();
            storageReference = FirebaseStorage.getInstance().getReference("Product Image");
            userProductRef = database.getReference("User Product").child(currentUid);
            categoryProductRef = database.getReference("All Category");
            allProjectRef = database.getReference("All Product");
        }
        ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,cat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
         category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 switch (position){
                     case 0:
                         Toast.makeText(getActivity(),"Choose proper category",Toast.LENGTH_SHORT).show();
                         break;
                     case 1:
                         subadapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,Fruits);
                         subadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                         subcat.setAdapter(subadapter);
                         imageArray=fruitsI;
                         break;
                     case 2:
                         subadapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,Bekery);
                         subadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                         subcat.setAdapter(subadapter);
                         imageArray=bekeryI;
                         break;
                     case 3:
                         subadapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,Vegetables);
                         subadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                         subcat.setAdapter(subadapter);
                         imageArray=vegetableI;
                         break;
                     case 4:
                         subadapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,Meat);
                         subadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                         subcat.setAdapter(subadapter);
                         imageArray=meatI;
                         break;
                     case 5:
                         subadapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,Icecreams);
                         subadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                         subcat.setAdapter(subadapter);
                         imageArray=icecreamI;
                         break;

                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

         subcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 switch (position) {
                     case 0:
                         Toast.makeText(getActivity(), "Choose proper category", Toast.LENGTH_SHORT).show();
                         break;
                     case 1:
                         imageUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getResources().getResourcePackageName(imageArray[0])+
                                 "/"+getResources().getResourceTypeName(imageArray[0])+"/"+getResources().getResourceEntryName(imageArray[0]));
                         Picasso.get().load(imageUri).into(pImage);
                         break;
                     case 2:
                         imageUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getResources().getResourcePackageName(imageArray[1])+
                                 "/"+getResources().getResourceTypeName(imageArray[1])+"/"+getResources().getResourceEntryName(imageArray[1]));
                         Picasso.get().load(imageUri).into(pImage);
                         break;
                     case 3:
                         imageUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getResources().getResourcePackageName(imageArray[2])+
                                 "/"+getResources().getResourceTypeName(imageArray[2])+"/"+getResources().getResourceEntryName(imageArray[2]));
                         Picasso.get().load(imageUri).into(pImage);
                         break;
                     case 4:
                         imageUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getResources().getResourcePackageName(imageArray[3])+
                                 "/"+getResources().getResourceTypeName(imageArray[3])+"/"+getResources().getResourceEntryName(imageArray[3]));
                         Picasso.get().load(imageUri).into(pImage);
                         break;
                     case 5:
                         imageUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getResources().getResourcePackageName(imageArray[4])+
                                 "/"+getResources().getResourceTypeName(imageArray[4])+"/"+getResources().getResourceEntryName(imageArray[4]));
                         Picasso.get().load(imageUri).into(pImage);
                         break;
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            try{
                Picasso.get().load(imageUri).into(pImage);
                Toast.makeText(getActivity(),"Fetched",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(),"Some Error Occure",Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadData() {
        pb.setVisibility(View.VISIBLE);
        final String mname=pname.getText().toString();
        final String mprice=pprice.getText().toString();
        final String mdesc=pdesc.getText().toString();
        final String mquantity=quantity.getText().toString();
        final String msub=subcat.getSelectedItem().toString();
        final String mcat=category.getSelectedItem().toString();
        if(mcat!="Choose Type" && msub!="Choose Type") {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + GetImageUri(imageUri));
            uploadTask = reference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        pb.setVisibility(View.INVISIBLE);
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();

                    Calendar cdate=Calendar.getInstance();
                    SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
                    final String savedate=currentDate.format(cdate.getTime());

                    Calendar ctime=Calendar.getInstance();
                    SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
                    final String savetime=currentTime.format(ctime.getTime());

                    final String time=savedate+":"+savetime;

                    productModel.setName(mname);
                    productModel.setCat(mcat);
                    productModel.setSubcat(msub);
                    productModel.setPrice(mprice);
                    productModel.setDesc(mdesc);
                    productModel.setQuantity(mquantity);
                    productModel.setRating("0");
                    productModel.setUid(currentUid);
                    productModel.setUrl(downloadUri.toString());
                    productModel.setTime(time);

                    String id1=userProductRef.push().getKey();
                    productModel.setKey(id1);
                    userProductRef.child(id1).setValue(productModel);
                    allProjectRef.child(id1).setValue(productModel);
                    categoryProductRef.child(mcat).child(id1).setValue(productModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Created", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.INVISIBLE);
                                    Fragment fragment=new HomeFragment();
                                    FragmentManager fragmentManager=getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
                                }
                            }, 1000);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Some Error Occure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String GetImageUri(Uri uri) {
        ContentResolver contentResolver=getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }
}