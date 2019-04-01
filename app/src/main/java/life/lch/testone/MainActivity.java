package life.lch.testone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private InitSoundPool initSoundPool;
    private RadioButton radioButtonboy;
    private RadioButton radioButtongirl;
    private SharedPreferences.Editor editor;
    private SharedPreferences checkSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSoundPool = new InitSoundPool(this);
        radioButtonboy=findViewById(R.id.chat_boy);
        radioButtongirl=findViewById(R.id.chat_girl);
        checkSex=PreferenceManager.getDefaultSharedPreferences(this);

        Boolean isGirl=checkSex.getBoolean("isGirl",false);
        if (isGirl){
            radioButtongirl.setChecked(true);
        }


        //设置随机语录
        Button button=findViewById(R.id.button1);
        Button button2=findViewById(R.id.luv_log);
        Button button3=findViewById(R.id.QQ_number_P);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSoundPool.playSound(1, 0);
                Intent intent=new Intent(MainActivity.this,QQnumber.class);
                startActivity(intent);
            }
        });
        Button startChatButton=findViewById(R.id.start_activity_chat);
        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSoundPool.playSound(1, 0);
                editor=checkSex.edit();

                if (radioButtonboy.isChecked()){
                    editor.putBoolean("isGirl",false);
                    editor.apply();
                    ChatActivity.actionStart(MainActivity.this,true);
                }
                if (radioButtongirl.isChecked()){
                    editor.putBoolean("isGirl",true);
                    editor.apply();
                    ChatActivity.actionStart(MainActivity.this,false);

                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initSoundPool.playSound(1, 0);
               Intent intent=new Intent(MainActivity.this,SecondActivity.class);
               startActivity(intent);
        }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSoundPool.playSound(1, 0);
                Intent intent=new Intent(MainActivity.this,LuvLog.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.give_money:
                Intent intent1=new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri uri=Uri.parse("https://qr.alipay.com/fkx08749jhfkdxflu4hovf2");
                intent1.setData(uri);
                startActivity(intent1);
                break;
            case R.id.remove_item:
                Intent intent=new Intent("life.lch.testone.FORCE_OFFLINE");
                sendBroadcast(intent);
                break;
            case R.id.exit:
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("提示：");
                dialog.setMessage("您真的要离开吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCollector.finishAll();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //模拟Home键
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    public String load(){
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder stringBuilder=new StringBuilder();
        try{
            in=openFileInput("data");
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while ((line=reader.readLine())!=null){
                stringBuilder.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onResume() {
        TextView target_name=findViewById(R.id.target_name);
        target_name.setText(load());
        TextView luvLetter=findViewById(R.id.talk_Something);
        luvLetter.setText(new RandomLuvLetter().getLuvLuvLetter());
        super.onResume();
    }
}
