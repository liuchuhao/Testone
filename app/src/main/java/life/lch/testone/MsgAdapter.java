package life.lch.testone;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rigthLayout;
        TextView leftText;
        TextView rightText;
        public ViewHolder(View view){
            super(view);
            leftLayout=view.findViewById(R.id.left_layout);
            rigthLayout=view.findViewById(R.id.right_layout);
            leftText=view.findViewById(R.id.left_message);
            rightText=view.findViewById(R.id.right_message);
        }

    }
    public MsgAdapter(List<Msg> msgList){
        mMsgList=msgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msg_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Msg msg=mMsgList.get(i);
        if (msg.getType()==Msg.TYPE_RECEIVED){
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rigthLayout.setVisibility(View.GONE);
            viewHolder.leftText.setText(msg.getContent());
        }else if (msg.getType()==Msg.TYPE_SEND){
            viewHolder.rigthLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightText.setText(msg.getContent());
        }
    }
}
