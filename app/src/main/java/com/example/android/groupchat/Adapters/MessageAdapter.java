package com.example.android.groupchat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.groupchat.Models.AllMethods;
import com.example.android.groupchat.Models.Message;
import com.example.android.groupchat.R;
import com.example.android.groupchat.groupChatActitvity;
import com.google.firebase.database.DatabaseReference;

import java.util.List;



public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdaterViewHolder>{
    Context context;
    List<Message> messages;
    DatabaseReference messageDb;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messageDb){
        this.context=context;
        this.messageDb=messageDb;
        this.messages=messages;


    }

    public MessageAdapter(groupChatActitvity context, List<Message> messages, DatabaseReference messagedb) {
    }

    @NonNull
    @Override
    public MessageAdaterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from ( context ).inflate ( R.layout.item_message,viewGroup,false );
        return new MessageAdaterViewHolder (  view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdaterViewHolder messageAdaterViewHolder, int position) {
        Message message =messages.get(position);
        if(message.getName ().equals ( AllMethods.name )){
            messageAdaterViewHolder.tvtitle.setText ( "You: " + message.getMessage ());
            messageAdaterViewHolder.tvtitle.setGravity ( Gravity.START );
            messageAdaterViewHolder.l1.setBackgroundColor ( Color.parseColor ("#EF9E73") );
        }
        else {
            messageAdaterViewHolder.tvtitle.setText ( message.getName ()+":"+message.getMessage () );
            messageAdaterViewHolder.ibdelete.setVisibility ( View.GONE );
        }

    }

    @Override
    public int getItemCount() {
        return messages.size ();
    }

    public class MessageAdaterViewHolder extends RecyclerView.ViewHolder {

        TextView tvtitle;
        ImageButton ibdelete;
        LinearLayout l1;


        public MessageAdaterViewHolder(@NonNull View itemView) {
            super ( itemView );
            tvtitle=(TextView)itemView.findViewById ( R.id.tvtitle );
            ibdelete=(ImageButton)itemView.findViewById ( R.id.ibdelete );
            l1 = (LinearLayout) itemView.findViewById ( R.id.l1message );
            ibdelete.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    messageDb.child ( messages.get (getAdapterPosition()).getKey()).removeValue();

                }
            } );
        }
    }

}
