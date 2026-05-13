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

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            Toast.makeText(getContext(), "Você selecionou a imagem " + (position + 1), Toast.LENGTH_SHORT).show();
            android.media.MediaPlayer mp = android.media.MediaPlayer.create(getContext(), R.raw.singing_whales);
            mp.start();
        });
    }
}