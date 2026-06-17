package com.example.trabalho1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.trabalho1.database.AppDatabase;
import com.example.trabalho1.database.BaleiaFavorita;
import com.example.trabalho1.database.Tarefa;
import java.util.ArrayList;
import java.util.List;

public class FavoritosFragment extends Fragment {

    private ListView lvFavoritos;
    private AppDatabase db;
    private int userId = -1;
    private List<String> itensExibicao = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public FavoritosFragment() {
        super(R.layout.fragment_favoritos);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(requireContext());
        lvFavoritos = view.findViewById(R.id.lvFavoritos);
        Button btnBaleias = view.findViewById(R.id.btnFiltroBaleias);
        Button btnTarefas = view.findViewById(R.id.btnFiltroTarefas);

        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
        }

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, itensExibicao);
        lvFavoritos.setAdapter(adapter);

        btnBaleias.setOnClickListener(v -> mostrarBaleias());
        btnTarefas.setOnClickListener(v -> mostrarTarefas());

        mostrarBaleias();
    }

    private void mostrarBaleias() {
        itensExibicao.clear();
        List<BaleiaFavorita> favoritas = db.appDao().listarBaleiasFavoritas(userId);
        for (BaleiaFavorita bf : favoritas) {
            itensExibicao.add("Baleia ID: " + bf.baleiaResId);
        }
        if (itensExibicao.isEmpty()) itensExibicao.add("Nenhuma baleia favoritada.");
        adapter.notifyDataSetChanged();
    }

    private void mostrarTarefas() {
        itensExibicao.clear();
        List<Tarefa> tarefas = db.appDao().listarTarefasFavoritas(userId);
        for (Tarefa t : tarefas) {
            itensExibicao.add("Tarefa: " + t.titulo);
        }
        if (itensExibicao.isEmpty()) itensExibicao.add("Nenhuma tarefa favoritada.");
        adapter.notifyDataSetChanged();
    }
}
