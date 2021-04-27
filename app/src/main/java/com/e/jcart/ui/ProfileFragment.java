package com.e.jcart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.e.jcart.CreateProfileActivity;
import com.e.jcart.HomeActivity;
import com.e.jcart.R;
import com.e.jcart.UpdateProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    TextView name,address,zipcode,number,email;
    ImageView imageView;
    Button update;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        name=root.findViewById(R.id.profrag_name);
        email=root.findViewById(R.id.profrag_email);
        address=root.findViewById(R.id.profrag_address);
        zipcode=root.findViewById(R.id.profrag_zipcode);
        imageView=root.findViewById(R.id.profrag_image);
        number=root.findViewById(R.id.profrag_number);
        update=root.findViewById(R.id.profrag_updaateProfile);
       update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getActivity(), UpdateProfile.class);
               startActivity(intent);
           }
       });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String currentUid=user.getUid();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference documentReference;
        documentReference=db.collection("Users").document(currentUid);
            FirebaseDatabase database=FirebaseDatabase.getInstance();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                     String nameT=task.getResult().get("name").toString();
                    String emailT=task.getResult().get("email").toString();
                    String addressT=task.getResult().get("address").toString();
                    String zipT=task.getResult().get("zipcode").toString();
                    String phoneT=task.getResult().get("number").toString();
                    String urlT=task.getResult().get("url").toString();

                    try {
                        email.setText(emailT);
                        name.setText(nameT);
                        number.setText(phoneT);
                        zipcode.setText(zipT);
                        address.setText(addressT);

                        Picasso.get().load(urlT).into(imageView);
                    }catch (Exception e){
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Intent intent=new Intent(getActivity(), CreateProfileActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}