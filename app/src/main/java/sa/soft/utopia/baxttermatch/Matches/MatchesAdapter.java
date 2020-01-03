package sa.soft.utopia.baxttermatch.Matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sa.soft.utopia.baxttermatch.Entidades.MatchesObject;
import sa.soft.utopia.baxttermatch.R;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders> {
    private List<MatchesObject> matchesList;
    private Context context;

    public MatchesAdapter(List<MatchesObject> matchesList, Context context)
    {
        this.matchesList=matchesList;
        this.context=context;
    }

    @NonNull
    @Override
    public MatchesViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);

        /*RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutView.setLayoutParams(lp);*/
        MatchesViewHolders rcv = new MatchesViewHolders((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolders holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchNombre.setText(matchesList.get(position).getNombre());
        if(!matchesList.get(position).getImagenPerfil().equals("default"))
            Glide.with(context).load(matchesList.get(position).getImagenPerfil()).apply(RequestOptions.circleCropTransform()).into(holder.imgMatch);
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
