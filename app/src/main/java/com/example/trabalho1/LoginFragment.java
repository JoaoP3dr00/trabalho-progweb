package com.example.trabalho1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho1.db.AppDatabase;
import com.example.trabalho1.model.Usuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private EditText editEmail, editSenha;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editEmail = view.findViewById(R.id.editEmail);
        editSenha = view.findViewById(R.id.editSenha);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        Button btnIrCadastro = view.findViewById(R.id.btnIrCadastro);
        Button btnAlterarSenha = view.findViewById(R.id.btnAlterarSenha);

        btnLogin.setOnClickListener(v -> fazerLogin());
        btnIrCadastro.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.to_register));
        btnAlterarSenha.setOnClickListener(v -> mostrarDialogAlterarSenha());
    }

    private void fazerLogin() {
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(getContext(), R.string.msg_campos_obrigatorios, Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstancia(requireContext());
            Usuario usuario = db.usuarioDao().login(email, senha);

            mainHandler.post(() -> {
                if (usuario != null) {
                    Session.login(usuario.id, usuario.nome, usuario.foto);
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.to_perfil);
                } else {
                    Toast.makeText(getContext(), R.string.msg_login_erro, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void mostrarDialogAlterarSenha() {
        View dialogView = getLayoutInflater().inflate(android.R.layout.two_line_list_item, null);
        EditText editEmailDialog = new EditText(getContext());
        editEmailDialog.setHint(R.string.hint_email);
        EditText editNovaSenha = new EditText(getContext());
        editNovaSenha.setHint(R.string.hint_senha);
        editNovaSenha.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        android.widget.LinearLayout layout = new android.widget.LinearLayout(getContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(48, 16, 48, 0);
        layout.addView(editEmailDialog);
        layout.addView(editNovaSenha);

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.btn_alterar_senha)
                .setView(layout)
                .setPositiveButton(R.string.btn_salvar, (dialog, which) -> {
                    String email = editEmailDialog.getText().toString().trim();
                    String novaSenha = editNovaSenha.getText().toString().trim();
                    if (email.isEmpty() || novaSenha.isEmpty()) {
                        Toast.makeText(getContext(), R.string.msg_campos_obrigatorios, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    executor.execute(() -> {
                        AppDatabase db = AppDatabase.getInstancia(requireContext());
                        Usuario usuario = db.usuarioDao().buscarPorEmail(email);
                        mainHandler.post(() -> {
                            if (usuario == null) {
                                Toast.makeText(getContext(), R.string.msg_login_erro, Toast.LENGTH_SHORT).show();
                            } else {
                                executor.execute(() -> {
                                    db.usuarioDao().alterarSenha(email, novaSenha);
                                    mainHandler.post(() ->
                                            Toast.makeText(getContext(), R.string.msg_senha_alterada, Toast.LENGTH_SHORT).show());
                                });
                            }
                        });
                    });
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
