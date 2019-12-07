package sa.soft.utopia.baxttermatch;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import sa.soft.utopia.baxttermatch.Entidades.cards;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class MainActivity2 extends AppCompatActivity {
    private cards cards_data[];
    private ArrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    String currentUID;

    Button btnLogout;

    ListView listView;
    List<cards> rowItems;

    DatabaseReference usersDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnLogout=findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent in = new Intent(MainActivity2.this, cambiarLoginRegistrationActivity.class);
                startActivity(in);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        checkSex();

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        final SwipeFlingAdapterView flingContainer= findViewById(R.id.frame);

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
                usersDb.child(oppositeUserSex).child(userId).child("Connections").child("No").child(currentUID).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards currentCards = (cards)dataObject;
                String userId = currentCards.getUserId();
                usersDb.child(oppositeUserSex).child(userId).child("Connections").child("Yes").child(currentUID).setValue(true);
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
                Toast.makeText(MainActivity2.this, "Clicked!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void isConnectionMatch(final String userId) {
        DatabaseReference currentUserConnectionDB = usersDb.child(userSex).child(currentUID).child("Connections").child("Yes").child(userId);
        currentUserConnectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(MainActivity2.this, "Nueva coincidencia", Toast.LENGTH_LONG).show();
                    usersDb.child(oppositeUserSex).child(dataSnapshot.getKey()).child("Connections").child("Matches").child(currentUID).setValue(true);
                    usersDb.child(userSex).child(currentUID).child("Connections").child("Matches").child(dataSnapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String userSex;
    private String oppositeUserSex;

    public void checkSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference maleDb= FirebaseDatabase.getInstance().getReference().child("Users").child("Hombre");

        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(user.getUid())){
                    userSex="Hombre";
                    oppositeUserSex="Mujer";
                    getOppositeSexUsers();
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

        DatabaseReference femaleDb= FirebaseDatabase.getInstance().getReference().child("Users").child("Mujer");

        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(user.getUid())){
                    userSex="Mujer";
                    oppositeUserSex="Hombre";
                    getOppositeSexUsers();
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

    private void getOppositeSexUsers(){
        DatabaseReference oppositeSexDb= FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeUserSex);

        oppositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists() && !dataSnapshot.child("Connections").child("No").hasChild(currentUID)
                        && !dataSnapshot.child("Connections").child("Yes").hasChild(currentUID)) {
                    cards item= new cards(dataSnapshot.getKey(), dataSnapshot.child("Nombre").getValue().toString());
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
