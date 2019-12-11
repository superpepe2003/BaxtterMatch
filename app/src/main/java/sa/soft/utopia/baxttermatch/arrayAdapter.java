package sa.soft.utopia.baxttermatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sa.soft.utopia.baxttermatch.Entidades.cards;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context,resourceId,items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        cards card_item = getItem(position);

        if(convertView==null)
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item, parent,false);

        TextView name = convertView.findViewById(R.id.txtname);
        ImageView image= convertView.findViewById(R.id.image);

        name.setText(card_item.getNombre());

        switch (card_item.getImgPerfil()){
            case "default":
                Glide.with(getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.with(getContext()).load(card_item.getImgPerfil()).into(image);
                break;
        }

        return convertView;
    }
}
