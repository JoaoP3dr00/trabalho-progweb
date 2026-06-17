package com.example.trabalho1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class AdminDashboardFragment extends Fragment {

    public AdminDashboardFragment() {
        super(R.layout.fragment_admin_dashboard);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnUsuarios = view.findViewById(R.id.btnGerenciarUsuarios);
        Button btnRelatorios = view.findViewById(R.id.btnRelatorios);
        Button btnSair = view.findViewById(R.id.btnSairAdmin);

        btnUsuarios.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.to_usuarios);
        });

        btnRelatorios.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.to_relatorio);
        });

        btnSair.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
        });
    }
}
