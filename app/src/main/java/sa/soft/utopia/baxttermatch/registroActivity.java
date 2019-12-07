package sa.soft.utopia.baxttermatch;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registroActivity extends AppCompatActivity {

    EditText txtMail, txtPass, txtNombre;
    Button btnRegistro;

    RadioButton chkMale, chkFemale;
    RadioGroup radioGroup;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    Drawable drawableFemale, drawableMale, drawableFemaleSele, drawableMaleSele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent in = new Intent(registroActivity.this, MainActivity2.class);
                    startActivity(in);
                    finish();
                    return;
                }
            }
        };

        txtMail= findViewById(R.id.txtEmail);
        txtPass=findViewById(R.id.txtPassword);
        txtNombre= findViewById(R.id.txtNombre);

        chkFemale=findViewById(R.id.chkFemale);
        chkMale=findViewById(R.id.chkMale);
        radioGroup= findViewById(R.id.rdbGroup);

        CargarImagenesChecked();
        CargarCheckbok();

        btnRegistro= findViewById(R.id.btnRegistro);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioGroup.getCheckedRadioButtonId();

                final RadioButton radioButtonSele= findViewById(id);

                if(radioButtonSele.getTag().toString()=="")
                {
                    return;
                }


                final String email = txtMail.getText().toString();
                final String pass = txtPass.getText().toString();

                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(registroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(registroActivity.this, "Error en registro", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String userId= mAuth.getCurrentUser().getUid();
                            DatabaseReference UserDb= FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(radioButtonSele.getTag().toString()).child(userId).child("Nombre");
                            UserDb.setValue(txtNombre.getText().toString());
                        }
                    }
                });
            }
        });

        chkFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarCheckbok();
            }
        });

        chkMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarCheckbok();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }


    private void CargarCheckbok()
    {
        if(chkMale.isChecked())
        {
            chkMale.setCompoundDrawablesWithIntrinsicBounds(null,drawableMaleSele,null,null);
        }
        else
        {
            chkMale.setCompoundDrawablesWithIntrinsicBounds(null,drawableMale,null,null);
        }
        if(chkFemale.isChecked())
        {
            chkFemale.setCompoundDrawablesWithIntrinsicBounds(null,drawableFemaleSele,null,null);
        }
        else
        {
            chkFemale.setCompoundDrawablesWithIntrinsicBounds(null,drawableFemale,null,null);
        }
    }

    private void CargarImagenesChecked() {
        drawableMale = resizeImage(R.drawable.img_male, (int) Math.round(getResources()
                .getDimension(R.dimen.dimen_ic_femalemale)),(int) Math.round(getResources()
                .getDimension(R.dimen.dimen_ic_femalemale)));

        drawableFemale = resizeImage(R.drawable.img_female, (int) Math.round(getResources()
                .getDimension(R.dimen.dimen_ic_femalemale)), (int) Math.round(getResources()
                .getDimension(R.dimen.dimen_ic_femalemale)));

        drawableMaleSele= resizeImage(R.drawable.img_male_sele, (int) Math.round(getResources()
                .getDimension(R.dimen.dimen_ic_femalemale)), (int) Math.round(getResources()
                .getDimension(R.dimen.dimen_ic_femalemale)));

        drawableFemaleSele= resizeImage(R.drawable.img_female_sele, (int) Math.round(getResources()
                .getDimension(R.dimen.dimen_ic_femalemale)), (int) Math.round(getResources()
                .getDimension(R.dimen.dimen_ic_femalemale)));
    }

    public Drawable resizeImage(int resId, int w, int h) {

        // cargamos la imagen de origen
        Bitmap BitmapOrg = BitmapFactory.decodeResource(this.getResources(), resId);

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = (int)Math.round(w * getResources().getDisplayMetrics().density);
        int newHeight = (int)Math.round(h * getResources().getDisplayMetrics().density);

        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // para poder manipular la imagen
        // debemos crear una matriz

        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,
                width, height, matrix, true);

        // si queremos poder mostrar nuestra imagen tenemos que crear un
        // objeto drawable y así asignarlo a un botón, imageview...
        return new BitmapDrawable(resizedBitmap);

    }
}
