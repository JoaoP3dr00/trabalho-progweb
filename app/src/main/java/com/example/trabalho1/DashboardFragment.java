package com.example.trabalho1;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.trabalho1.database.AppDatabase;
import android.app.AlertDialog;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvBoasVindas = view.findViewById(R.id.tvBoasVindas);
        ImageView ivUser = view.findViewById(R.id.ivUserDashboard);
        Button btnBaleias = view.findViewById(R.id.btnIrBaleias);
        Button btnTarefas = view.findViewById(R.id.btnIrTarefas);
        Button btnFavoritos = view.findViewById(R.id.btnFavoritos);
        Button btnSair = view.findViewById(R.id.btnSair);

        if (getArguments() != null) {
            String nome = getArguments().getString("userName");
            String fotoUriStr = getArguments().getString("userFoto");

            if (nome != null) {
                tvBoasVindas.setText("Bem-vindo, " + nome + "!");
            }

            if (fotoUriStr != null && !fotoUriStr.isEmpty()) {
                ivUser.setImageURI(Uri.parse(fotoUriStr));
            }
        }

        btnBaleias.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (getArguments() != null) {
                bundle.putInt("userId", getArguments().getInt("userId"));
            }
            NavHostFragment.findNavController(this).navigate(R.id.to_first_fragment, bundle);
        });

        btnTarefas.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (getArguments() != null) {
                bundle.putInt("userId", getArguments().getInt("userId"));
            }
            NavHostFragment.findNavController(this).navigate(R.id.to_tarefa_fragment, bundle);
        });

        btnFavoritos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (getArguments() != null) {
                bundle.putInt("userId", getArguments().getInt("userId"));
            }
            NavHostFragment.findNavController(this).navigate(R.id.to_favoritos, bundle);
        });

        btnSair.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
        });

        mostrarLembrete();
    }

    private void mostrarLembrete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Lembrete");
        builder.setMessage("Bem-vindo de volta! Não esqueça de conferir suas tarefas pendentes e seus favoritos.");
        builder.setPositiveButton("Entendi!", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
