package life.lch.testone;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QQnumber extends BaseActivity {
    private static final String TAG = "QQnumber";
    EditText editText1;
    EditText editText2;
    Button button;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqnumber);
        editText1=findViewById(R.id.qq_number1);
        editText2=findViewById(R.id.qq_number2);
        button=findViewById(R.id.submit_qq);
        textView=findViewById(R.id.qq_result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=editText1.getText().toString();
                String s2=editText2.getText().toString();
                if (s.length()>=5&&s2.length()>=5){
                    char one=s.charAt(s.length()-2);
                    char two=s2.charAt(s2.length()-2);
                    int number1=one-'0';
                    int number2=two-'0';//char数字转换为int数字
                    Log.d(TAG, "one=="+one);

                    Log.d(TAG, "number1=="+number1);
                    int sum=number1+number2;
                    Log.d(TAG, "sum=="+sum);
                    String result=P(sum);
                    textView.setText(result);
                }else {
                    textView.setText("请输入正确的QQ号");
                }

            }
        });
    }
    public String P(int sum){
        String result=null;
        if (0<=sum&&sum<19){
            switch (sum){
                case 0:
                    result="两人有默契";break;
                case 1:
                    result="他/她非常关心你";break;
                case 2:
                    result="你和他/她只能做朋友";break;
                case 3:
                    result="兴趣不合";
                    break;
                case 4:
                    result="男生主动";
                    break;
                case 5:
                    result="第三者介入";
                    break;
                case 6:
                    result="他/她非常讨厌你";
                    break;
                case 7:
                    result="早点分手比较好";
                    break;
                case 8:
                    result="他/她有一堆异性朋友";
                    break;
                    case 9:
                        result="两情相悦";
                break;
                case 10:
                    result="他/她对你献真情";
                    break;
                case 11:
                    result="他/她已有心上人";
                    break;
                case 12:
                    result="此情不渝";
                    break;
                case 13:
                    result="他/她时常暗中注意你";
                    break;
                case 14:
                    result="他/她有企图要小心";
                    break;
                case 15:
                    result="他/她会爱你";
                    break;
                case 16:
                    result="他/她非常在乎你";
                    break;
                case 17:
                    result="女方主动";
                    break;
                case 18:
                    result="也有人暗中注意你";
                    break;
                    default:
                        break;

            }
        }else {
            result="输入不合法（如果是Bug，请联系QQ8843326，谢谢！）";
        }

        return result;
    }
}
