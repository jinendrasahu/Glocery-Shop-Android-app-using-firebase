package com.e.jcart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e.jcart.Model.UserModel;
import com.e.jcart.ui.ProfileFragment;
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

import java.util.HashMap;

public class CreateProfileActivity extends AppCompatActivity {

    ImageView imageView;
    EditText name,email,address,zipcode,number;
    Button upload;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    StorageReference storageReference;
    final static int PICK_IMAGE=1;
    UploadTask uploadTask;
    Uri imageUri;
    String currentUid;
    UserModel userModel;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        imageView=findViewById(R.id.create_image);
        name=findViewById(R.id.create_name);
        address=findViewById(R.id.create_address);
        email=findViewById(R.id.create_email);
        zipcode=findViewById(R.id.create_zipcode);
        number=findViewById(R.id.create_number);
        pb=findViewById(R.id.cp_pb);
        upload=findViewById(R.id.create_btn);
        userModel=new UserModel();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(CreateProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }else {
            currentUid = user.getUid();


            storageReference = FirebaseStorage.getInstance().getReference("Profile Image");
            documentReference = db.collection("Users").document(currentUid);
            databaseReference = database.getReference("All Users").child(currentUid);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            try{
                Picasso.get().load(imageUri).into(imageView);
                Toast.makeText(CreateProfileActivity.this,"Fetched",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(CreateProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(CreateProfileActivity.this,"Some Error Occure",Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadData() {
        pb.setVisibility(View.VISIBLE);
        final String mname=name.getText().toString();
        final String memail=email.getText().toString();
        final String mnumber=number.getText().toString();
        final String maddress=address.getText().toString();
        final String mzipcode=zipcode.getText().toString();

        final StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+GetImageUri(imageUri));
        uploadTask=reference.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
               if(!task.isSuccessful()){
                   pb.setVisibility(View.INVISIBLE);
                   throw task.getException();
               }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri=task.getResult();

                HashMap<String,String> userdetails=new HashMap<>();
                userdetails.put("name",mname);
                userdetails.put("uid",currentUid);
                userdetails.put("email",memail);
                userdetails.put("address",maddress);
                userdetails.put("zipcode",mzipcode);
                userdetails.put("number",mnumber);
                userdetails.put("url",downloadUri.toString());

                userModel.setEmail(memail);
                userModel.setName(mname);
                userModel.setNumber(mnumber);
                userModel.setUid(currentUid);
                userModel.setUrl(downloadUri.toString());

                databaseReference.setValue(userModel);
                documentReference.set(userdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateProfileActivity.this,"Created",Toast.LENGTH_SHORT).show();
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pb.setVisibility(View.INVISIBLE);
                                Intent intent=new Intent(CreateProfileActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        },1000);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(CreateProfileActivity.this,"Some Error Occure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String GetImageUri(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

        @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(CreateProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
