package sa.soft.utopia.baxttermatch.Chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import sa.soft.utopia.baxttermatch.Entidades.ChatObject;
import sa.soft.utopia.baxttermatch.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {
    private List<ChatObject> chatList;
    private Context context;

    public ChatAdapter(List<ChatObject> chatList, Context context)
    {
        this.chatList=chatList;
        this.context=context;
    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {
        holder.txtMessage.setText(chatList.get(position).getMessage());
        Drawable back = holder.txtMessage.getBackground();
        int col;

        if(chatList.get(position).getCurrentUser()){
            //holder.txtMessage.setGravity(Gravity.END);
            holder.mContainer.setGravity(Gravity.END);
            col = Color.parseColor("#DBFB9F");
            //shapeDrawable.getPaint().setColor(Color.parseColor("#DBFB9F"));
            //holder.txtMessage.setBackgroundColor(Color.parseColor("#DBFB9F"));
            //holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }
        else
        {
            //holder.mContainer.setGravity(Gravity.START);
            holder.mContainer.setGravity(Gravity.START);
            //shapeDrawable.getPaint().setColor(Color.parseColor("#FFFFFF"));
            col = Color.parseColor("#FFFFFF");
            //holder.txtMessage.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }
        if(back instanceof ShapeDrawable){
            ShapeDrawable shapeDrawable = (ShapeDrawable)back;
            shapeDrawable.getPaint().setColor(col);
        }else if (back instanceof GradientDrawable){
            GradientDrawable gradientDrawable = (GradientDrawable)back;
            gradientDrawable.setColor(col);
        }else if(back instanceof ColorDrawable){
            ColorDrawable colorDrawable=(ColorDrawable)back;
            colorDrawable.setColor(col);
        }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
