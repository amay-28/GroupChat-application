package com.example.android.groupchat;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextInputEditText textEmail, textPassword;
    ProgressBar progressBar;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

       auth=FirebaseAuth.getInstance ();
       if(auth.getCurrentUser ()!=null){
           Intent i= new Intent (MainActivity.this,groupChatActitvity.class );
           startActivity ( i );
       }
       else
       {
           setContentView ( R.layout.activity_main );
           textEmail =(TextInputEditText  ) findViewById ( R.id.email_id_login );
           textPassword=(TextInputEditText)findViewById ( R.id.password_id_login );
           progressBar=(ProgressBar)findViewById ( R.id.progressBarLogin );

       }


    }
    public void LoginUser(View v){
     progressBar.setVisibility ( View.VISIBLE );
     String email= textEmail.getText ().toString ();
     String password= textPassword.getText ().toString ();
     if(!email.equals ( "" )&& !password.equals ( "")){
         auth.signInWithEmailAndPassword ( email,password ).
                 addOnCompleteListener ( new OnCompleteListener <AuthResult> () {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful ()){
                             progressBar.setVisibility (View.GONE );
                             Toast.makeText (getApplicationContext (), "Logged In", Toast.LENGTH_SHORT ).show ();
                             Intent i= new Intent (MainActivity.this,groupChatActitvity.class );
                             startActivity ( i );
                         }
                         else {
                             Toast.makeText (getApplicationContext (), "Wrong Email/Password.Try Again", Toast.LENGTH_SHORT ).show ();
                             progressBar.setVisibility ( View.GONE );
                         }
                     }
                 } );
     }

        }
    public void gotoRegister(View v){
        Intent x= new Intent ( MainActivity.this,Registeractivity.class );
        startActivity(x);
    }
    public void forgotPassword(View view){
        AlertDialog.Builder alert= new AlertDialog.Builder ( MainActivity.this );
        LinearLayout container= new LinearLayout ( MainActivity.this );
        container.setOrientation (LinearLayout.VERTICAL );
        LinearLayout.LayoutParams  ip= new LinearLayout.LayoutParams ( LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT );
        ip.setMargins ( 50,0,0,100 );
        final  EditText input= new EditText ( MainActivity.this );
        input.setLayoutParams ( ip );
        input.setGravity ( Gravity.TOP|Gravity.START );
        input.setInputType ( InputType.TYPE_TEXT_FLAG_CAP_SENTENCES );
        input.setLines ( 1 );
        input.setMaxLines ( 1 );
        container.addView ( input,ip );
        alert.setMessage ( "Enter your registered email id" );
        alert.setTitle ( "Forgot Password" );
        alert.setView ( container );
        alert.setPositiveButton ( "submit", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                String entered_email= input.getText ().toString ();
                auth.sendPasswordResetEmail ( entered_email ).addOnCompleteListener ( new OnCompleteListener <Void> () {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if(task.isSuccessful ()){
                            dialogInterface.dismiss ();
                             Toast.makeText ( getApplicationContext (),"Email Sent. Please Check your Email",Toast.LENGTH_SHORT).show ();


                        }
                    }
                } );
            }
        } );
    }

}
