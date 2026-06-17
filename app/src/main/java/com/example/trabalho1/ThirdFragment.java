package com.example.trabalho1;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.trabalho1.BaleiaAdapter;

public class ThirdFragment extends Fragment {

    public ThirdFragment() { super(R.layout.fragment_third); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView gridView = view.findViewById(R.id.gridViewGaleria);

        gridView.setAdapter(new BaleiaAdapter(getContext()));

        int[] mThumbIds = {
                R.drawable.baleia1, R.drawable.baleia4,
                R.drawable.baleia2, R.drawable.baleia3
        };

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            int resId = mThumbIds[position];
            int userId = -1;
            if (getArguments() != null) {
                userId = getArguments().getInt("userId", -1);
            }

            int finalUserId = userId;
            com.example.trabalho1.database.AppDatabase db = com.example.trabalho1.database.AppDatabase.getDatabase(requireContext());

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
            builder.setTitle("Baleia " + (position + 1));
            builder.setMessage("O que deseja fazer?");

            boolean jaFavorito = db.appDao().isBaleiaFavorita(finalUserId, resId);

            builder.setPositiveButton(jaFavorito ? "Remover Favorito" : "Favoritar", (dialog, which) -> {
                if (jaFavorito) {
                    db.appDao().removerBaleiaFavorita(finalUserId, resId);
                    Toast.makeText(getContext(), "Removido dos favoritos!", Toast.LENGTH_SHORT).show();
                } else {
                    com.example.trabalho1.database.BaleiaFavorita bf = new com.example.trabalho1.database.BaleiaFavorita();
                    bf.usuarioId = finalUserId;
                    bf.baleiaResId = resId;
                    db.appDao().inserirBaleiaFavorita(bf);
                    Toast.makeText(getContext(), "Adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNeutralButton("Ouvir Som", (dialog, which) -> {
                android.media.MediaPlayer mp = android.media.MediaPlayer.create(getContext(), R.raw.singing_whales);
                mp.start();
            });

            builder.setNegativeButton("Fechar", null);
            builder.show();
        });
    }
}