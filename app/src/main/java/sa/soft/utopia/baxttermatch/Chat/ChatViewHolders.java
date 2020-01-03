package sa.soft.utopia.baxttermatch.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sa.soft.utopia.baxttermatch.R;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMessage;
    public LinearLayout mContainer;

    public ChatViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        txtMessage = itemView.findViewById(R.id.txtMessage);
        mContainer = itemView.findViewById(R.id.idContainer);
    }

    @Override
    public void onClick(View v) {

    }
}
