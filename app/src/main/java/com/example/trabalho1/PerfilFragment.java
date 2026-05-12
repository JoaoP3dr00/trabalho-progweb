package com.example.trabalho1;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho1.db.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PerfilFragment extends Fragment {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public PerfilFragment() {
        super(R.layout.fragment_perfil);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageViewFoto = view.findViewById(R.id.imageViewPerfilFoto);
        TextView textViewNome = view.findViewById(R.id.textViewNomeUsuario);
        Button btnVerBaleias = view.findViewById(R.id.btnVerBaleias);
        Button btnVerAvistamentos = view.findViewById(R.id.btnVerAvistamentos);
        Button btnAlterarSenha = view.findViewById(R.id.btnAlterarSenhaPerfil);
        Button btnSair = view.findViewById(R.id.btnSair);

        textViewNome.setText(Session.getUserName());

        String foto = Session.getUserFoto();
        if (foto != null && !foto.isEmpty()) {
            imageViewFoto.setImageURI(Uri.parse(foto));
        }

        btnVerBaleias.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.to_baleias));

        btnVerAvistamentos.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.to_avistamentos));

        btnAlterarSenha.setOnClickListener(v -> mostrarDialogAlterarSenha());

        btnSair.setOnClickListener(v -> {
            Session.logout();
            NavHostFragment.findNavController(this).navigate(R.id.to_logout);
        });
    }

    private void mostrarDialogAlterarSenha() {
        EditText editNovaSenha = new EditText(getContext());
        editNovaSenha.setHint(R.string.hint_senha);
        editNovaSenha.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        android.widget.LinearLayout layout = new android.widget.LinearLayout(getContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(48, 16, 48, 0);
        layout.addView(editNovaSenha);

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.btn_alterar_senha)
                .setView(layout)
                .setPositiveButton(R.string.btn_salvar, (dialog, which) -> {
                    String novaSenha = editNovaSenha.getText().toString().trim();
                    if (novaSenha.isEmpty()) {
                        Toast.makeText(getContext(), R.string.msg_campos_obrigatorios, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    executor.execute(() -> {
                        AppDatabase db = AppDatabase.getInstancia(requireContext());
                        com.example.trabalho1.model.Usuario u = db.usuarioDao().buscarPorId(Session.getUserId());
                        if (u != null) {
                            db.usuarioDao().alterarSenha(u.email, novaSenha);
                            mainHandler.post(() ->
                                    Toast.makeText(getContext(), R.string.msg_senha_alterada, Toast.LENGTH_SHORT).show());
                        }
                    });
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
