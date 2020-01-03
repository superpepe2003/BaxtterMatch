package sa.soft.utopia.baxttermatch.Perfil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import sa.soft.utopia.baxttermatch.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PerfilParticularActivity extends AppCompatActivity {

    private EditText txtNombre, txtTelefono;
    private Button btnBack, btnConfirm;
    private ImageView imgPerfilImagen;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId, nombre, telefono, imgPerfilImagenTexto, userSex;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_particular);

        txtNombre=findViewById(R.id.txtNombre);
        txtTelefono=findViewById(R.id.txtTelefono);

        btnBack=findViewById(R.id.btnAtras);
        btnConfirm=findViewById(R.id.btnConfirmar);

        imgPerfilImagen=findViewById(R.id.imgPerfil);

        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        imgPerfilImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("Nombre")!=null){
                        nombre=map.get("Nombre").toString();
                        txtNombre.setText(nombre);
                    }
                    if(map.get("Telefono")!=null){
                        telefono=map.get("Telefono").toString();
                        txtTelefono.setText(telefono);
                    }
                    if(map.get("Sexo")!=null){
                        userSex=map.get("Sexo").toString();
                    }
                    //Glide.clear(imgPerfilImagen);
                    if(map.get("ImagenPerfil")!=null){
                        imgPerfilImagenTexto=map.get("ImagenPerfil").toString();
                        switch (imgPerfilImagenTexto){
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(imgPerfilImagen);
                                break;
                            default:
                                Glide.with(getApplication()).load(imgPerfilImagenTexto).into(imgPerfilImagen);
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

    private void saveUserInformation() {
        nombre=txtNombre.getText().toString();
        telefono=txtTelefono.getText().toString();

        Map userInfo= new HashMap();
        userInfo.put("Nombre", nombre);
        userInfo.put("Telefono", telefono);
        mUserDatabase.updateChildren(userInfo);

        if(resultUri!=null){
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ImagenPerfil").child(userId);
            Bitmap bitmap=null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20 , baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map userInfo= new HashMap();
                            userInfo.put("ImagenPerfil", uri.toString());
                            mUserDatabase.updateChildren(userInfo);

                            finish();
                            return;
                        }
                    });
                }
            });
        }else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            imgPerfilImagen.setImageURI(resultUri);
        }
    }
}
