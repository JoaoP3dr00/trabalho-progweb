package com.example.trabalho1;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trabalho1.database.AppDatabase;
import com.example.trabalho1.database.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuariosFragment extends Fragment {

    private ListView lvUsuarios;
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private List<String> nomesUsuarios = new ArrayList<>();
    private AppDatabase db;

    public UsuariosFragment() {
        super(R.layout.fragment_usuarios);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(requireContext());
        lvUsuarios = view.findViewById(R.id.lvUsuarios);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, nomesUsuarios);
        lvUsuarios.setAdapter(adapter);

        registerForContextMenu(lvUsuarios);

        atualizarLista();
    }

    private void atualizarLista() {
        listaUsuarios = db.appDao().listarTodosUsuarios();
        nomesUsuarios.clear();
        for (Usuario u : listaUsuarios) {
            nomesUsuarios.add(u.nome + " (" + u.email + ")");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_contexto_usuario, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Usuario usuario = listaUsuarios.get(info.position);

        if (item.getItemId() == R.id.menu_excluir_usuario) {
            if (usuario.isAdmin) {
                Toast.makeText(getContext(), "Não é possível excluir um administrador!", Toast.LENGTH_SHORT).show();
            } else {
                db.appDao().deletarUsuario(usuario);
                atualizarLista();
                Toast.makeText(getContext(), "Usuário excluído!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
