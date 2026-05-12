package com.example.trabalho1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho1.db.AppDatabase;
import com.example.trabalho1.model.Avistamento;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AvistamentoFormFragment extends Fragment {

    private Spinner spinnerEspecie;
    private EditText editLocal, editData, editDescricao;
    private Button btnSalvar, btnExcluir;
    private TextView textViewTitulo;

    private int avistamentoId = 0;
    private Avistamento avistamentoEditando = null;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public AvistamentoFormFragment() {
        super(R.layout.fragment_avistamento_form);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerEspecie = view.findViewById(R.id.spinnerEspecie);
        editLocal = view.findViewById(R.id.editLocal);
        editData = view.findViewById(R.id.editData);
        editDescricao = view.findViewById(R.id.editDescricao);
        btnSalvar = view.findViewById(R.id.btnSalvar);
        btnExcluir = view.findViewById(R.id.btnExcluir);
        textViewTitulo = view.findViewById(R.id.textViewTituloForm);

        if (getArguments() != null) {
            avistamentoId = getArguments().getInt("avistamentoId", 0);
        }

        if (avistamentoId != 0) {
            textViewTitulo.setText(R.string.titulo_editar_avistamento);
            btnExcluir.setVisibility(View.VISIBLE);
            carregarAvistamento();
        }

        btnSalvar.setOnClickListener(v -> salvar());
        btnExcluir.setOnClickListener(v -> excluir());
    }

    private void carregarAvistamento() {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstancia(requireContext());
            avistamentoEditando = db.avistamentoDao().buscarPorId(avistamentoId);
            mainHandler.post(() -> {
                if (avistamentoEditando != null) {
                    preencherFormulario(avistamentoEditando);
                }
            });
        });
    }

    private void preencherFormulario(Avistamento a) {
        String[] especies = getResources().getStringArray(R.array.especies_baleias_form);
        for (int i = 0; i < especies.length; i++) {
            if (especies[i].equals(a.especie)) {
                spinnerEspecie.setSelection(i);
                break;
            }
        }
        editLocal.setText(a.local);
        editData.setText(a.data);
        editDescricao.setText(a.descricao);
    }

    private void salvar() {
        String especie = spinnerEspecie.getSelectedItem().toString();
        String local = editLocal.getText().toString().trim();
        String data = editData.getText().toString().trim();
        String descricao = editDescricao.getText().toString().trim();

        if (spinnerEspecie.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), R.string.msg_especie_obrigatoria, Toast.LENGTH_SHORT).show();
            return;
        }
        if (local.isEmpty()) {
            Toast.makeText(getContext(), R.string.msg_campos_obrigatorios, Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstancia(requireContext());
            if (avistamentoId == 0) {
                Avistamento novo = new Avistamento();
                novo.especie = especie;
                novo.local = local;
                novo.data = data;
                novo.descricao = descricao;
                db.avistamentoDao().inserir(novo);
            } else {
                avistamentoEditando.especie = especie;
                avistamentoEditando.local = local;
                avistamentoEditando.data = data;
                avistamentoEditando.descricao = descricao;
                db.avistamentoDao().atualizar(avistamentoEditando);
            }
            mainHandler.post(() -> {
                Toast.makeText(getContext(), R.string.msg_avistamento_salvo, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(AvistamentoFormFragment.this).navigateUp();
            });
        });
    }

    private void excluir() {
        if (avistamentoEditando == null) return;
        new android.app.AlertDialog.Builder(getContext())
                .setTitle(R.string.btn_excluir)
                .setMessage(R.string.msg_confirmar_exclusao)
                .setPositiveButton(R.string.btn_excluir, (dialog, which) ->
                        executor.execute(() -> {
                            AppDatabase db = AppDatabase.getInstancia(requireContext());
                            db.avistamentoDao().excluir(avistamentoEditando);
                            mainHandler.post(() -> {
                                Toast.makeText(getContext(), R.string.msg_avistamento_excluido, Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(AvistamentoFormFragment.this).navigateUp();
                            });
                        }))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
