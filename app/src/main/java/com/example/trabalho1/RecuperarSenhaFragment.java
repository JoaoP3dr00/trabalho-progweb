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
import com.example.trabalho1.database.Usuario;

public class RecuperarSenhaFragment extends Fragment {

    public RecuperarSenhaFragment() {
        super(R.layout.fragment_recuperar_senha);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etEmail = view.findViewById(R.id.etEmailRecuperar);
        EditText etPalavraChave = view.findViewById(R.id.etPalavraChaveRecuperar);
        EditText etNovaSenha = view.findViewById(R.id.etNovaSenha);
        Button btnAlterar = view.findViewById(R.id.btnAlterarSenha);

        AppDatabase db = AppDatabase.getDatabase(requireContext());

        btnAlterar.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String palavraChave = etPalavraChave.getText().toString().trim();
            String novaSenha = etNovaSenha.getText().toString().trim();

            if (email.isEmpty() || palavraChave.isEmpty() || novaSenha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (novaSenha.length() < 6 || !novaSenha.matches(".*[0-9].*") || !novaSenha.matches(".*[a-zA-Z].*")) {
                Toast.makeText(getContext(), "A nova senha deve ter pelo menos 6 caracteres, incluindo letras e números", Toast.LENGTH_LONG).show();
                return;
            }

            Usuario usuario = db.appDao().buscarPorEmail(email);

            if (usuario != null && usuario.palavraChave.equals(palavraChave)) {
                usuario.senha = SecurityUtils.hashSenha(novaSenha);
                db.appDao().atualizarUsuario(usuario);
                Toast.makeText(getContext(), "Senha alterada com sucesso", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                Toast.makeText(getContext(), "E-mail ou palavra-chave incorretos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
