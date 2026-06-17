package com.example.trabalho1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trabalho1.database.AppDatabase;

public class RelatorioFragment extends Fragment {

    private TextView tvTotalUsuarios, tvTotalTarefas, tvTotalFavoritos;
    private AppDatabase db;

    public RelatorioFragment() {
        super(R.layout.fragment_relatorio);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(requireContext());

        tvTotalUsuarios = view.findViewById(R.id.tvTotalUsuarios);
        tvTotalTarefas = view.findViewById(R.id.tvTotalTarefas);
        tvTotalFavoritos = view.findViewById(R.id.tvTotalFavoritos);

        Button btnExportar = view.findViewById(R.id.btnExportarRelatorio);
        Button btnCompartilhar = view.findViewById(R.id.btnCompartilharRelatorio);

        carregarDados();

        btnExportar.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Relatório exportado com sucesso (simulado)", Toast.LENGTH_SHORT).show();
        });

        btnCompartilhar.setOnClickListener(v -> {
            String relatorio = "Relatório SeaTask\n" +
                    tvTotalUsuarios.getText().toString() + "\n" +
                    tvTotalTarefas.getText().toString() + "\n" +
                    tvTotalFavoritos.getText().toString() + "\n" +
                    "Uso de CPU: 12%\n" +
                    "Status: Estável";

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Relatório de Sistema - SeaTask");
            intent.putExtra(Intent.EXTRA_TEXT, relatorio);
            startActivity(Intent.createChooser(intent, "Compartilhar relatório via:"));
        });
    }

    private void carregarDados() {
        int usuarios = db.appDao().contarUsuarios();
        int tarefas = db.appDao().contarTarefas();
        int favoritos = db.appDao().contarFavoritos();

        tvTotalUsuarios.setText("Total de Usuários: " + usuarios);
        tvTotalTarefas.setText("Total de Tarefas: " + tarefas);
        tvTotalFavoritos.setText("Total de Favoritos: " + favoritos);
    }
}
