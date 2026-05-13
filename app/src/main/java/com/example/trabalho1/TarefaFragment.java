package com.example.trabalho1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho1.database.AppDatabase;
import com.example.trabalho1.database.Tarefa;
import com.example.trabalho1.database.Usuario;

import java.util.ArrayList;
import java.util.List;

public class TarefaFragment extends Fragment {

    private ListView lvTarefas;
    private EditText etBusca;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private List<String> titulosTarefas = new ArrayList<>();
    private AppDatabase db;

    public TarefaFragment() {
        super(R.layout.fragment_tarefas);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(requireContext());
        lvTarefas = view.findViewById(R.id.lvTarefas);
        etBusca = view.findViewById(R.id.etBuscaTarefa);
        Button btnBuscar = view.findViewById(R.id.btnBuscarTarefa);
        Button btnNova = view.findViewById(R.id.btnNovaTarefa);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, titulosTarefas);
        lvTarefas.setAdapter(adapter);

        registerForContextMenu(lvTarefas);

        btnBuscar.setOnClickListener(v -> atualizarLista(etBusca.getText().toString()));
        btnNova.setOnClickListener(v -> mostrarDialogoTarefa(null));

        atualizarLista("");

        if (getArguments() != null) {
            String nome = getArguments().getString("userName");
            if (nome != null) {
                Toast.makeText(getContext(), "Bem-vindo, " + nome, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void atualizarLista(String busca) {
        if (busca.isEmpty()) {
            listaTarefas = db.appDao().listarTarefas();
        } else {
            listaTarefas = db.appDao().buscarTarefa("%" + busca + "%");
        }

        titulosTarefas.clear();
        for (Tarefa t : listaTarefas) {
            titulosTarefas.add(t.titulo + "\n" + t.descricao);
        }
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoTarefa(@Nullable Tarefa tarefaExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_tarefa, null);
        builder.setView(dialogView);

        EditText etTitulo = dialogView.findViewById(R.id.etTituloTarefa);
        EditText etDesc = dialogView.findViewById(R.id.etDescricaoTarefa);

        if (tarefaExistente != null) {
            etTitulo.setText(tarefaExistente.titulo);
            etDesc.setText(tarefaExistente.descricao);
        }

        builder.setPositiveButton(R.string.salvar, (dialog, which) -> {
            Tarefa t = (tarefaExistente != null) ? tarefaExistente : new Tarefa();
            t.titulo = etTitulo.getText().toString();
            t.descricao = etDesc.getText().toString();

            if (tarefaExistente == null) {
                db.appDao().inserirTarefa(t);
            } else {
                db.appDao().atualizarTarefa(t);
            }
            atualizarLista("");
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_contexto_tarefa, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Tarefa tarefa = listaTarefas.get(info.position);

        if (item.getItemId() == R.id.menu_editar) {
            Bundle bundle = new Bundle();
            bundle.putInt("tarefaId", tarefa.id);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.to_edicao_tarefa, bundle);
            return true;
        } else if (item.getItemId() == R.id.menu_excluir) {
            db.appDao().deletarTarefa(tarefa);
            atualizarLista("");
            Toast.makeText(getContext(), "Tarefa excluída!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_principal, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_alterar_senha) {
            mostrarDialogoAlterarSenha();
            return true;
        } else if (item.getItemId() == R.id.menu_logout) {
            NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogoAlterarSenha() {
        int userId = -1;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.alterar_senha);
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_alterar_senha, null);
        builder.setView(view);

        EditText etEmail = view.findViewById(R.id.etEmailAlterar);
        EditText etNovaSenha = view.findViewById(R.id.etNovaSenha);

        builder.setPositiveButton(R.string.salvar, (dialog, which) -> {
            String email = etEmail.getText().toString();
            String novaSenha = etNovaSenha.getText().toString();
            
            Toast.makeText(getContext(), "Funcionalidade requerida: Buscar por email no DAO", Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
