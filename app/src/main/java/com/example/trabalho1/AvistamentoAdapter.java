package com.example.trabalho1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trabalho1.model.Avistamento;

import java.util.List;

public class AvistamentoAdapter extends ArrayAdapter<Avistamento> {

    public AvistamentoAdapter(@NonNull Context context, @NonNull List<Avistamento> avistamentos) {
        super(context, 0, avistamentos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_avistamento, parent, false);
        }

        Avistamento avistamento = getItem(position);
        if (avistamento != null) {
            TextView textEspecie = convertView.findViewById(R.id.textEspecie);
            TextView textLocalData = convertView.findViewById(R.id.textLocalData);
            TextView textDescricao = convertView.findViewById(R.id.textDescricao);

            textEspecie.setText(avistamento.especie);
            String localData = avistamento.local;
            if (avistamento.data != null && !avistamento.data.isEmpty()) {
                localData += " · " + avistamento.data;
            }
            textLocalData.setText(localData);
            textDescricao.setText(avistamento.descricao);
        }

        return convertView;
    }
}
