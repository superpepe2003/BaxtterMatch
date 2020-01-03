package sa.soft.utopia.baxttermatch.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sa.soft.utopia.baxttermatch.Entidades.ChatObject;
import sa.soft.utopia.baxttermatch.Matches.MatchesAdapter;
import sa.soft.utopia.baxttermatch.Entidades.MatchesObject;
import sa.soft.utopia.baxttermatch.R;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private String currentUserId, matchId, chatId;

    private TextView txtmensaje, txtNombre;
    private CircularImageView imgPerfilchat;
    private Button btnEnviar;

    DatabaseReference mDBUser, mDBChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId = getIntent().getExtras().getString("matchId");

        txtmensaje = findViewById(R.id.txtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        imgPerfilchat = findViewById(R.id.imgPerfilChat);
        txtNombre = findViewById(R.id.txtNombreChat);

        cargarDatosChat();

        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDBUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId)
                .child("Connections").child("Matches").child(matchId).child("ChatId");
        mDBChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();

        recyclerView = findViewById(R.id.recyclerViewChat);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new ChatAdapter(getDataSetChat(), this);
        recyclerView.setAdapter(mChatAdapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
            }
        });
    }

    String nombre, imgPerfilImagenTexto;
    private void cargarDatosChat() {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(matchId);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("Nombre")!=null){
                        nombre=map.get("Nombre").toString();
                        txtNombre.setText(nombre);
                    }
                    if(map.get("ImagenPerfil")!=null){
                        imgPerfilImagenTexto=map.get("ImagenPerfil").toString();
                        switch (imgPerfilImagenTexto){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.ic_matches).into(imgPerfilchat);
                                break;
                            default:
                                Glide.with(getApplication()).load(imgPerfilImagenTexto).into(imgPerfilchat);
                                break;
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void enviarMensaje(){
        String enviarMensajeText= txtmensaje.getText().toString();

        if(!enviarMensajeText.isEmpty()){
            DatabaseReference nuevoMensajeDb = mDBChat.push();

            Map nuevoMensaje = new HashMap();
            nuevoMensaje.put("CreadoPor", currentUserId);
            nuevoMensaje.put("Text", enviarMensajeText);

            nuevoMensajeDb.setValue(nuevoMensaje);
        }
        txtmensaje.setText(null);
    }

    private void getChatId(){
        mDBUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    chatId = dataSnapshot.getValue().toString();
                    mDBChat = mDBChat.child(chatId);
                    getChatMensajes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatMensajes() {
        mDBChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message= null;
                    String createByUser=null;

                    if(dataSnapshot.child("Text").getValue()!=null){
                        message=dataSnapshot.child("Text").getValue().toString();
                    }

                    if(dataSnapshot.child("CreadoPor").getValue()!=null){
                        createByUser=dataSnapshot.child("CreadoPor").getValue().toString();
                    }

                    if(message!=null && createByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createByUser.equals(currentUserId)){
                            currentUserBoolean=true;
                        }
                        ChatObject newMessage= new ChatObject(message,currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<ChatObject> resultsChat = new ArrayList<>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}
