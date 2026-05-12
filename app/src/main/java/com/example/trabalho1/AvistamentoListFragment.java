package com.example.trabalho1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho1.db.AppDatabase;
import com.example.trabalho1.model.Avistamento;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AvistamentoListFragment extends Fragment {

    private ListView listView;
    private AvistamentoAdapter adapter;
    private List<Avistamento> listaAtual = new ArrayList<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public AvistamentoListFragment() {
        super(R.layout.fragment_avistamento_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listViewAvistamentos);
        EditText editBusca = view.findViewById(R.id.editBusca);
        Button btnNovo = view.findViewById(R.id.btnNovoAvistamento);

        adapter = new AvistamentoAdapter(requireContext(), listaAtual);
        listView.setAdapter(adapter);

        btnNovo.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.to_form));

        listView.setOnItemClickListener((parent, v, position, id) -> {
            Avistamento avistamento = listaAtual.get(position);
            Bundle args = new Bundle();
            args.putInt("avistamentoId", avistamento.id);
            NavHostFragment.findNavController(this).navigate(R.id.to_form, args);
        });

        listView.setOnItemLongClickListener((parent, v, position, id) -> {
            Avistamento avistamento = listaAtual.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle(avistamento.especie)
                    .setMessage(avistamento.descricao)
                    .setPositiveButton(R.string.acao_editar, (dialog, which) -> {
                        Bundle args = new Bundle();
                        args.putInt("avistamentoId", avistamento.id);
                        NavHostFragment.findNavController(this).navigate(R.id.to_form, args);
                    })
                    .setNegativeButton(R.string.acao_excluir, (dialog, which) -> excluir(avistamento))
                    .setNeutralButton(android.R.string.cancel, null)
                    .show();
            return true;
        });

        editBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscar(s.toString().trim());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        carregarTodos();
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarTodos();
    }

    private void carregarTodos() {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstancia(requireContext());
            List<Avistamento> lista = db.avistamentoDao().listarTodos();
            mainHandler.post(() -> atualizarLista(lista));
        });
    }

    private void buscar(String texto) {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstancia(requireContext());
            List<Avistamento> lista;
            if (texto.isEmpty()) {
                lista = db.avistamentoDao().listarTodos();
            } else {
                lista = db.avistamentoDao().buscar(texto);
            }
            mainHandler.post(() -> atualizarLista(lista));
        });
    }

    private void excluir(Avistamento avistamento) {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstancia(requireContext());
            db.avistamentoDao().excluir(avistamento);
            mainHandler.post(() -> {
                Toast.makeText(getContext(), R.string.msg_avistamento_excluido, Toast.LENGTH_SHORT).show();
                carregarTodos();
            });
        });
    }

    private void atualizarLista(List<Avistamento> lista) {
        listaAtual.clear();
        listaAtual.addAll(lista);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
