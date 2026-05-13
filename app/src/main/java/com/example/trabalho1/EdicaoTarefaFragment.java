package com.example.trabalho1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho1.database.AppDatabase;
import com.example.trabalho1.database.Tarefa;

public class EdicaoTarefaFragment extends Fragment {

    private EditText etTitulo, etDescricao;
    private AppDatabase db;
    private int tarefaId;
    private Tarefa tarefa;

    public EdicaoTarefaFragment() {
        super(R.layout.fragment_edicao_tarefa);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(requireContext());
        etTitulo = view.findViewById(R.id.etEditTitulo);
        etDescricao = view.findViewById(R.id.etEditDescricao);
        Button btnSalvar = view.findViewById(R.id.btnSalvarEdicao);
        Button btnExcluir = view.findViewById(R.id.btnExcluirTarefa);

        if (getArguments() != null) {
            tarefaId = getArguments().getInt("tarefaId");
            tarefa = db.appDao().buscarTarefaPorId(tarefaId);

            if (tarefa != null) {
                etTitulo.setText(tarefa.titulo);
                etDescricao.setText(tarefa.descricao);
            }
        }

        btnSalvar.setOnClickListener(v -> {
            if (tarefa != null) {
                tarefa.titulo = etTitulo.getText().toString();
                tarefa.descricao = etDescricao.getText().toString();
                db.appDao().atualizarTarefa(tarefa);
                Toast.makeText(getContext(), "Tarefa atualizada!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        btnExcluir.setOnClickListener(v -> {
            if (tarefa != null) {
                db.appDao().deletarTarefa(tarefa);
                Toast.makeText(getContext(), "Tarefa excluída!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }
}
