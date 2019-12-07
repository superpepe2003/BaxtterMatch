package sa.soft.utopia.baxttermatch;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class cambiarLoginRegistrationActivity extends AppCompatActivity {

    Button btnLogin, btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_login_registration);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent in = new Intent(cambiarLoginRegistrationActivity.this, MainActivity.class);
            startActivity(in);
            finish();
        }

        btnLogin=findViewById(R.id.btnLogin);
        btnRegistro=findViewById(R.id.btnRegistro);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(cambiarLoginRegistrationActivity.this, loginActivity.class);
                startActivity(in);
                finish();
                return;
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(cambiarLoginRegistrationActivity.this, registroActivity.class);
                startActivity(in);
                finish();
                return;
            }
        });
    }
}
