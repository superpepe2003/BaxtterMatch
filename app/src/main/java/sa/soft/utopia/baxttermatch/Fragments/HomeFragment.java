package sa.soft.utopia.baxttermatch.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import sa.soft.utopia.baxttermatch.Entidades.cards;
import sa.soft.utopia.baxttermatch.R;
import sa.soft.utopia.baxttermatch.arrayAdapter;
import sa.soft.utopia.baxttermatch.ui.main.PageViewModel;


public class HomeFragment extends Fragment {

    private cards cards_data[];
    private ArrayAdapter arrayAdapter;
    private int i;

    //PageViewModel pageViewModel;

    private FirebaseAuth mAuth;
    String currentUID;


    ListView listView;
    List<cards> rowItems;

    DatabaseReference usersDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        checkSex();

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(getContext(), R.layout.item, rowItems );

        final SwipeFlingAdapterView flingContainer= view.findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards currentCards = (cards)dataObject;
                String userId = currentCards.getUserId();
                usersDb.child(userId).child("Connections").child("No").child(currentUID).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards currentCards = (cards)dataObject;
                String userId = currentCards.getUserId();
                usersDb.child(userId).child("Connections").child("Yes").child(currentUID).setValue(true);
                isConnectionMatch(userId);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }

        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getContext(), "Clicked!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void isConnectionMatch(final String userId) {
        DatabaseReference currentUserConnectionDB = usersDb.child(currentUID).child("Connections").child("Yes").child(userId);
        currentUserConnectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getContext(), "Nueva coincidencia", Toast.LENGTH_LONG).show();
                    usersDb.child(dataSnapshot.getKey()).child("Connections").child("Matches").child(currentUID).setValue(true);
                    usersDb.child(currentUID).child("Connections").child("Matches").child(dataSnapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String userSex;
    private String oppositeUserSex;

    //DETERMINA EL SEXO DEL USUARIO Y SU PREFERENCIA SEXUAL

    public void checkSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb= usersDb.child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("Sexo").getValue()!=null){
                        userSex=dataSnapshot.child("Sexo").getValue().toString();
                        switch (userSex){
                            case "Hombre":
                                oppositeUserSex="Mujer";
                                break;
                            case "Mujer":
                                oppositeUserSex="Hombre";
                                break;
                        }
                        /*pageViewModel = ViewModelProviders.of(requireActivity()).get(PageViewModel.class);
                        pageViewModel.setUserSex(userSex);*/
                        getOppositeSexUsers();
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    //TRAE TODAS LAS TARJETAS VERIFICANDO QUE NO TENGA MATCHES O DISMATCHES DEL USUARIO Y QUE SEA DEL SEXO PREFERIDO
    private void getOppositeSexUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists() && !dataSnapshot.child("Connections").child("No").hasChild(currentUID)
                        && !dataSnapshot.child("Connections").child("Yes").hasChild(currentUID)
                        && dataSnapshot.child("Sexo").getValue().toString().equals(oppositeUserSex)) {
                    String imgUrl = "default";

                    if(!dataSnapshot.child("ImagenPerfil").getValue().equals("default")){
                        imgUrl = dataSnapshot.child("ImagenPerfil").getValue().toString();
                    }
                    cards item= new cards(dataSnapshot.getKey(), dataSnapshot.child("Nombre").getValue().toString(),
                            imgUrl);
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
