package life.lch.testone;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity{
    private List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initMsgs();//初始化
        inputText=findViewById(R.id.input_text);
        send=findViewById(R.id.send_message);
        msgRecyclerView=findViewById(R.id.msg_recycle_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=inputText.getText().toString();
                if (!"".equals(content)){
                    Chat chat=new Chat();
                    chat.setMessage(content);
                    chat.setType(Msg.TYPE_SEND);
                    chat.save();
                    Msg msg=new Msg(content,Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear_chat:
                DataSupport.deleteAll(Chat.class);
                finish();
                break;
                default:
                    break;
        }
        return true;
    }

    private void initMsgs(){
        if (!DataSupport.isExist(Chat.class)){
        Chat chat=new Chat();
        chat.setType(Msg.TYPE_RECEIVED);
       chat.setMessage("小哥哥，在吗？");
       chat.save();
        }
        List<Chat> chats= DataSupport.findAll(Chat.class);
        if (chats.size()>=50){ //当聊天记录大于50条，取倒数50条数据
            int j=chats.size()-50;
            for (int i=0;i<50;i++){
                Chat c=chats.get(j);
                Msg m=new Msg(c.getString(),c.getType());
                msgList.add(m);
                j++;
            }
        }else{
        for (Chat c:chats){
            Msg m=new Msg(c.getString(),c.getType());
            msgList.add(m);
        }
        }
    }
}
