package com.e.jcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText email,password,cpassword;
    CheckBox showPassword;
    Button register,goToLogin;
    FirebaseAuth auth;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.register_email);
        password=findViewById(R.id.register_password);
        register=findViewById(R.id.register_btn);
        goToLogin=findViewById(R.id.register_to_login);
        showPassword=findViewById(R.id.show_register_password);
        cpassword=findViewById(R.id.register_confirmPassword);
        pb=findViewById(R.id.pb);

        auth=FirebaseAuth.getInstance();

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                String memail=email.getText().toString();
                String mpassword=password.getText().toString();
                String mcpassword=cpassword.getText().toString();
                if(!TextUtils.isEmpty(memail) && !TextUtils.isEmpty(mpassword) && !TextUtils.isEmpty(mcpassword)){
                    if(mpassword.equals(mcpassword)){
                          auth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                  pb.setVisibility(View.INVISIBLE);
                                  Toast.makeText(RegisterActivity.this,"Registered",Toast.LENGTH_SHORT).show();
                                  Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                  startActivity(intent);
                                  finish();
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  pb.setVisibility(View.INVISIBLE);
                                  Toast.makeText(RegisterActivity.this,"Failed To Register",Toast.LENGTH_SHORT).show();

                              }
                          });
                    }else{
                        pb.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this,"password and confirm password should be same",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this,"Input should not b empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
