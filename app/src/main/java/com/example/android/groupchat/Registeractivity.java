package com.example.android.groupchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.groupchat.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class Registeractivity extends AppCompatActivity {
    TextInputEditText textEmail, textPassword, textName;
    ProgressBar progressBar;
    DatabaseReference reference;
    FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.register );
        textEmail = (TextInputEditText) findViewById ( R.id.email_id_register );
        textPassword = (TextInputEditText) findViewById ( R.id.password_id_register );
        textName = (TextInputEditText) findViewById ( R.id.name_id_register );
        progressBar = (ProgressBar) findViewById ( R.id.progressBarRegister );
        auth = FirebaseAuth.getInstance ();
        reference = FirebaseDatabase.getInstance ().getReference ().child ( "Users" );

    }

    public void Registeruser(View v) {
        progressBar.setVisibility ( View.VISIBLE );
        final String Email = textEmail.getText ().toString ();
        final String Password = textPassword.getText ().toString ();
        final String Name = textName.getText ().toString ();
        if (!Email.equals ( "" ) && !Password.equals ( "" ) && Password.length () > 6) ;
        {
            auth.createUserWithEmailAndPassword ( Email, Password ).addOnCompleteListener ( new OnCompleteListener <AuthResult> () {
                @Override
                public void onComplete(@NonNull Task <AuthResult> task) {
                    if (task.isSuccessful ()) {
                        //now we can insert values in database
                        FirebaseUser firebaseUser = auth.getCurrentUser ();
                        User u=new User ();
                        u.setName ( Name );
                        u.setEmail ( Email );

                        reference.child ( firebaseUser.getUid() ).setValue ( u ).addOnCompleteListener ( new OnCompleteListener <Void> () {
                            @Override
                            public void onComplete(@NonNull Task <Void> task) {
                                if (task.isSuccessful ()){
                                    Toast.makeText (getApplicationContext (),"User registered Successfully",Toast.LENGTH_SHORT).show ();
                                    progressBar.setVisibility ( View.GONE );
                                    finish ();
                                    Intent i = new Intent ( Registeractivity.this,groupChatActitvity.class );
                                    startActivity ( i );
                                }
                                else{
                                    progressBar.setVisibility ( View.GONE );
                                    Toast.makeText ( getApplicationContext (),"User is not Created",Toast.LENGTH_SHORT ).show ();
                                }
                            }
                        } );

                    }
                }
            } );


        }
    }
    public void gotoLogin(View view){
        Intent i =new Intent ( Registeractivity.this,MainActivity.class );
        startActivity ( i );
    }
}
