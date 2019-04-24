package com.example.android.groupchat;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.groupchat.Adapters.MessageAdapter;
import com.example.android.groupchat.Models.AllMethods;
import com.example.android.groupchat.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;




public class groupChatActitvity extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference  messagedb;
    MessageAdapter messageAdapter;
    User u;
    List <com.example.android.groupchat.Models.Message> messages;

    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_group_chat_actitvity );

        init();
    }

    private void init() {
        auth= FirebaseAuth.getInstance ();
        database=FirebaseDatabase.getInstance ();
        u= new User ();
        rvMessage =( RecyclerView)findViewById ( R.id.rvMessage );
        etMessage= (EditText)findViewById ( R.id.message);
        imgButton=(ImageButton)findViewById ( R.id.btnsend );
        imgButton.setOnClickListener ( this );
        messages= new ArrayList <com.example.android.groupchat.Models.Message> (  );

    }

    @Override
    public void onClick(View v) {
 if(TextUtils.isEmpty ( etMessage.getText ().toString () )){
     com.example.android.groupchat.Models.Message message = new com.example.android.groupchat.Models.Message (etMessage.getText ().toString (),u.getName ());
     etMessage.setText ( "" );
     messagedb.push ().setValue ( message );


 }
 else {
     Toast.makeText ( getApplicationContext (),"you cannot send blank message",Toast.LENGTH_SHORT ).show ();
 }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater ().inflate ( R.menu.menu,menu );
        return true;
    }
    public boolean onOptionsItemsSelected(MenuItem item){
        if(item.getItemId ()==R.id.menulogout){
            auth.signOut ();
            finish ();
            startActivity(new Intent (groupChatActitvity.this,MainActivity.class) );
        }
        return super.onOptionsItemSelected ( item );

    }
    protected void onStart(){
        super.onStart ();
        final FirebaseUser currentUser=auth.getCurrentUser ();
        u.setUid ( currentUser.getUid () );
        u.setEmail ( currentUser.getEmail () );

        database.getReference ("Users").child ( currentUser.getUid ()).addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u=dataSnapshot.getValue (User.class);
                u.setUid ( currentUser.getUid() );
                AllMethods.name = u.getName ();


            }
            public void onCancelled(@NonNull DatabaseError databaseError){

            }


        } );
        messagedb = database.getReference ("messages");
        messagedb.addChildEventListener ( new ChildEventListener () {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                com.example.android.groupchat.Models.Message message= dataSnapshot.getValue ( com.example.android.groupchat.Models.Message.class);
                message.setKey(dataSnapshot.getKey ());
                messages.add(message);
                displayMessages(messages);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                com.example.android.groupchat.Models.Message message=dataSnapshot.getValue ( com.example.android.groupchat.Models.Message.class );

                message.setKey(dataSnapshot.getKey ());

                List <com.example.android.groupchat.Models.Message> newMessages= new ArrayList <> (  );
                for (com.example.android.groupchat.Models.Message m: messages){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add ( message );

                    }
                    else
                    {
                        newMessages.add(m);

                    }
                }
                messages = newMessages;
                displayMessages ( messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                com.example.android.groupchat.Models.Message message =dataSnapshot.getValue ( com.example.android.groupchat.Models.Message.class);
                message.setKey(dataSnapshot.getKey ());
                ArrayList <com.example.android.groupchat.Models.Message> newMessages= new ArrayList <> (  );
                for (com.example.android.groupchat.Models.Message m: messages)
                {
                    if(!m.getKey ().equals(message.getKey())){
                        newMessages.add ( m );

                    }
                }
                messages = newMessages;
                displayMessages ( messages );

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
    @Override
    protected  void onResume(){
        super.onResume ();
        messages= new ArrayList <com.example.android.groupchat.Models.Message> (  );

    }
    private void displayMessages(List <com.example.android.groupchat.Models.Message> messages){

        rvMessage.setLayoutManager ( new LinearLayoutManager ( groupChatActitvity.this ) );

        messageAdapter= new MessageAdapter ( groupChatActitvity.this,messages,messagedb );
        rvMessage.setAdapter ( messageAdapter );

    };

}
