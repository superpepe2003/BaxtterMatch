package sa.soft.utopia.baxttermatch.Perfil;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import sa.soft.utopia.baxttermatch.R;
import sa.soft.utopia.baxttermatch.cambiarLoginRegistrationActivity;


public class PerfilFragment extends Fragment {

    Button btnLogout, btnPerfil;
    TextView lblNombre;
    CircularImageView imgImagenPerfil;

    //private PageViewModel pageViewModel;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        btnLogout=view.findViewById(R.id.btnLogout);
        btnPerfil=view.findViewById(R.id.btnPerfil);
        lblNombre= view.findViewById(R.id.lblNombre);
        imgImagenPerfil=view.findViewById(R.id.imgImagenPerfil);

        FirebaseAuth oAuth = FirebaseAuth.getInstance();
        String id= oAuth.getCurrentUser().getUid().toString();

        DatabaseReference oDb = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        oDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("Nombre")!=null)
                    {
                        lblNombre.setText(dataSnapshot.child("Nombre").getValue().toString());
                    }
                    if(dataSnapshot.child("ImagenPerfil")!=null)
                    {
                        Glide.with(getContext()).load(dataSnapshot.child("ImagenPerfil").getValue().toString()).into(imgImagenPerfil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*pageViewModel = ViewModelProviders.of(requireActivity()).get(PageViewModel.class);
        pageViewModel.getUserSex().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userSex=s;
            }
        });*/



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent in = new Intent(getContext(), cambiarLoginRegistrationActivity.class);
                startActivity(in);
                getActivity().finish();
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in= new Intent(getActivity(), PerfilParticularActivity.class);
                startActivity(in);
            }
        });

        return view;
    }

}
