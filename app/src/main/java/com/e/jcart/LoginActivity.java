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

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    CheckBox showPassword;
    Button login,goToRegister;
    FirebaseAuth auth;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        login=findViewById(R.id.login_btn);
        goToRegister=findViewById(R.id.login_to_register);
        showPassword=findViewById(R.id.show_login_password);
        pb=findViewById(R.id.pb);

        auth=FirebaseAuth.getInstance();

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                String memail=email.getText().toString();
                String mpassword=password.getText().toString();
                if(!TextUtils.isEmpty(memail) && !TextUtils.isEmpty(mpassword)){

                        auth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this,"Logged in",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this,"Failed To Login",Toast.LENGTH_SHORT).show();

                            }
                        });
                }else{
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this,"Input should not b empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
