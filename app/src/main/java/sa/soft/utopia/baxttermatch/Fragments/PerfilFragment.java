package sa.soft.utopia.baxttermatch.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import sa.soft.utopia.baxttermatch.PerfilParticularActivity;
import sa.soft.utopia.baxttermatch.R;
import sa.soft.utopia.baxttermatch.cambiarLoginRegistrationActivity;
import sa.soft.utopia.baxttermatch.ui.main.PageViewModel;


public class PerfilFragment extends Fragment {

    Button btnLogout, btnPerfil;
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
