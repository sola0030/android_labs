package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    ListView lv;
    EditText editMessage;
    List<MessageModel> messageModelList2 = new ArrayList<>();
    Button btnSend;
    Button btnReceive;
    DBAdapter db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        lv = (ListView)findViewById(R.id.listView);
        editMessage = (EditText)findViewById(R.id.editMessage);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnReceive = (Button)findViewById(R.id.btnReceive);
        db =  new DBAdapter(this);
        boolean isTable = findViewById(R.id.fragmentLocation) != null;
        viewMessage();
        lv.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString("item", messageModelList2.get(position).message);
            dataToPass.putInt("id", position);
            dataToPass.putLong("db_id", messageModelList2.get(position).messageID);


            if (isTable){
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }else {
                Intent emptyActivity = new Intent(this, EmptyActivity.class);
                emptyActivity.putExtras(dataToPass);
                startActivityForResult(emptyActivity, 345);
            }

        });
/*        btnSend.setOnClickListener(c -> {
            String message = editMessage.getText().toString();
//            MessageModel model = new MessageModel(message, true);
//            messageModelList2.add(model);
//            ChatAdapter adt = new ChatAdapter(messageModelList2, getApplicationContext());
//            lv.setAdapter(adt);
            db.insertMessage(message, true);
            editMessage.setText("");
            messageModelList2.clear();
            viewMessage();
        });*/

        btnReceive.setOnClickListener(c -> {
            String message = editMessage.getText().toString();
//            MessageModel model = new MessageModel(message, false);
//            messageModelList2.add(model);

//            ChatAdapter adt = new ChatAdapter(messageModelList2, getApplicationContext());
//            lv.setAdapter(adt);
            db.insertMessage(message, false);
            editMessage.setText("");
            messageModelList2.clear();

            viewMessage();

        });

        Log.e("ChatRoomActivity","onCreate");


    }

    private void viewMessage(){
        Cursor cursor = db.getAllMessages();
        //hien so message trong db
        Log.d("message",String.valueOf(cursor.getCount()));

        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                MessageModel model = new MessageModel(cursor.getString(1), cursor.getInt(2)==0?true:false, cursor.getLong(0));
                messageModelList2.add(model);
                ChatAdapter adt = new ChatAdapter(messageModelList2, getApplicationContext());
                lv.setAdapter(adt);

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 345) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra("db_id", 0);
                deleteMessageId((int) id);
            }
        }
    }

    public void deleteMessageId(int id)
    {

        db.deleteEntry(id);
        messageModelList2.clear();
        viewMessage();
    }


}

class MessageModel {
    public String message;
    public boolean isSend;
    public long messageID;


    public MessageModel(String message, boolean isSend, long messageID) {
        this.message = message;
        this.isSend = isSend;
        this.messageID = messageID;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }
}
//test folk
class ChatAdapter extends BaseAdapter {
    private List<MessageModel> messageModelList;
    private Context context;
    private LayoutInflater inflater;

    public ChatAdapter(List<MessageModel> messageModels, Context context) {
        messageModelList = messageModels;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return messageModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;


        if (messageModelList.get(position).isSend()){
            view = inflater.inflate(R.layout.activity_main_send, null);

        }else {
            view = inflater.inflate(R.layout.activity_main_receive, null);
        }
        TextView  messageText = (TextView)view.findViewById(R.id.messageText);
        messageText.setText(messageModelList.get(position).message);

        return view;
    }
}
    