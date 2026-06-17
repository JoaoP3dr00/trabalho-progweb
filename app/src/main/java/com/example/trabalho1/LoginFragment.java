package com.example.trabalho1;

import android.media.MediaPlayer;
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

public class LoginFragment extends Fragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etSenha = view.findViewById(R.id.etSenha);
        Button btnEntrar = view.findViewById(R.id.btnEntrar);
        Button btnIrCadastro = view.findViewById(R.id.btnIrCadastro);

        AppDatabase db = AppDatabase.getDatabase(requireContext());

        btnEntrar.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();

            String senhaHash = SecurityUtils.hashSenha(senha);
            Usuario usuario = db.appDao().logarUsuario(email, senhaHash);

            if (usuario != null) {
                tocarSomSucesso();
                if (usuario.isAdmin) {
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.to_admin_dashboard);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", usuario.id);
                    bundle.putString("userName", usuario.nome);
                    bundle.putString("userFoto", usuario.fotoUri);

                    NavHostFragment.findNavController(this)
                            .navigate(R.id.to_dashboard, bundle);
                }
            } else {
                Toast.makeText(getContext(), R.string.login_erro, Toast.LENGTH_SHORT).show();
            }
        });

        btnIrCadastro.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.to_cadastro);
        });

        view.findViewById(R.id.tvEsqueciSenha).setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.to_recuperar_senha);
        });
    }

    private void tocarSomSucesso() {
        MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.singing_whales);
        mp.start();
        mp.setOnCompletionListener(MediaPlayer::release);
    }
}
