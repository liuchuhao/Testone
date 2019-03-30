package life.lch.testone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity{
    private static final String TAG = "ChatActivity";
    private List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private Boolean isBoy;
    private Boolean before;
    private SharedPreferences sRead;
    private SharedPreferences.Editor editor;
    private String sendUrl;
    //处理数据
    private Handler handler
            =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String content=parseJSONObject(msg.obj.toString());
                    Msg mes=new Msg(content,Msg.TYPE_RECEIVED);
                    msgList.add(mes);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    };
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
        msgRecyclerView.scrollToPosition(msgList.size()-1);//定位最新消息
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
                    if (isBoy){
                        sendUrl="cee5465d1d324c6d8c26784e8e4085bb";
                    }else {
                        sendUrl="bef2471198a542118a5f0b282d166a80";
                    }
                    //请求数据
                    HttpUtil.sendHttpRequest("http://www.tuling123.com/openapi/" +
                                    "api?key="+sendUrl+"&info="+content,
                            new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            //根据返回内容执行相应逻辑
                            Message message=new Message();
                            message.obj=response;
                            message.what=0;

                            handler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {
                            Message message=new Message();
                            message.obj=e;
                            message.what=1;
                            handler.sendMessage(message);
                        }
                    });
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
        //判断所选机器人性别
        Intent intent=getIntent();
        isBoy=intent.getBooleanExtra("isboy",true);
        Log.d(TAG, "是男孩吗"+isBoy);
        sRead= PreferenceManager.getDefaultSharedPreferences(this);
        before=sRead.getBoolean("isBoy",true);

        if (!isBoy==before){
            DataSupport.deleteAll(Chat.class);
        }
        editor=sRead.edit();
        editor.putBoolean("isBoy",isBoy);
        editor.apply();
        ActionBar actionBar=getSupportActionBar();
        if (isBoy){
            actionBar.setTitle("猫九  在线 √");
        }else {
            actionBar.setTitle("鱼七  在线 √");
        }
        //判断是否需要初始话聊天记录
        if (!DataSupport.isExist(Chat.class)){

            if (isBoy) {
                Chat chat = new Chat();
                chat.setType(Msg.TYPE_RECEIVED);
                chat.setMessage("你好哇！我是猫九");
                chat.save();
            }else{
                Chat chat = new Chat();
                chat.setType(Msg.TYPE_RECEIVED);
                chat.setMessage("我是鱼七，很高兴认识你！");
                chat.save();
            }
        }
        //导入聊天记录
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
    //解析json
    private String parseJSONObject(String jsonData){
        String content;
        try {
                JSONObject jsonObject=new JSONObject(jsonData);
                content=jsonObject.getString("text");
                Chat chat=new Chat();
                chat.setMessage(content);
                chat.setType(Msg.TYPE_RECEIVED);
                chat.save();

        }catch (Exception e){
            content=e.toString();
        }
        return content;
    }
    //启动ChatActivity
    public static  void actionStart(Context context,Boolean isboy){
        Intent intent=new Intent(context,ChatActivity.class);
        intent.putExtra("isboy",isboy);
        context.startActivity(intent);

    }
}
