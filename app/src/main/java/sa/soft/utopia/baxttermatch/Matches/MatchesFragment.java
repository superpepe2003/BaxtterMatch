package sa.soft.utopia.baxttermatch.Matches;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sa.soft.utopia.baxttermatch.Entidades.MatchesObject;
import sa.soft.utopia.baxttermatch.R;

public class MatchesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;

    @Override
    public void onResume() {
        super.onResume();

    }

    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.recyclerViewMatches);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        mMatchesLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mMatchesLayoutManager);

        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), getContext());
        recyclerView.setAdapter(mMatchesAdapter);

        getUserMatchId();

        return view;
    }

    private void getUserMatchId() {
        DatabaseReference matchDb= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Connections").child("Matches");
        matchDb.keepSynced(true);
        matchDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultsMatches.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        getMatchInfo(match.getKey());
                    }
                }
                else{
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMatchInfo(String key) {
        DatabaseReference userDb= FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userId=dataSnapshot.getKey();
                    String nombre="";
                    String imagenPerfil="";

                    if(dataSnapshot.child("Nombre").getValue()!=null)
                    {
                        nombre=dataSnapshot.child("Nombre").getValue().toString();
                    }

                    if(dataSnapshot.child("ImagenPerfil").getValue()!=null)
                    {
                        imagenPerfil=dataSnapshot.child("ImagenPerfil").getValue().toString();
                    }

                    MatchesObject obj = new MatchesObject(userId,nombre,imagenPerfil);
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<>();
    private List<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }
}
