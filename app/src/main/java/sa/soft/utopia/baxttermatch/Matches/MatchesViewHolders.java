package sa.soft.utopia.baxttermatch.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sa.soft.utopia.baxttermatch.Chat.ChatActivity;
import sa.soft.utopia.baxttermatch.R;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMatchId, mMatchNombre;
    public ImageView imgMatch;

    public MatchesViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = itemView.findViewById(R.id.txtMachId);
        mMatchNombre = itemView.findViewById(R.id.txtMachNombre);
        imgMatch=itemView.findViewById(R.id.imgMatch);
    }

    @Override
    public void onClick(View v) {
        Intent in =  new Intent(v.getContext(), ChatActivity.class);
        Bundle b =  new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        //b.putString("cardsId", );
        in.putExtras(b);
        v.getContext().startActivity(in);
    }
}
