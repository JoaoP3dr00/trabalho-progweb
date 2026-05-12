package com.example.trabalho1;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho1.db.AppDatabase;
import com.example.trabalho1.model.Usuario;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.widget.EditText;

public class RegisterFragment extends Fragment {

    private ImageView imageViewFoto;
    private EditText editNome, editEmail, editSenha, editConfirmarSenha;
    private Uri photoUri;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && photoUri != null && imageViewFoto != null) {
                        imageViewFoto.setImageURI(null);
                        imageViewFoto.setImageURI(photoUri);
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageViewFoto = view.findViewById(R.id.imageViewFoto);
        editNome = view.findViewById(R.id.editNome);
        editEmail = view.findViewById(R.id.editEmailCadastro);
        editSenha = view.findViewById(R.id.editSenhaCadastro);
        editConfirmarSenha = view.findViewById(R.id.editConfirmarSenha);
        Button btnFoto = view.findViewById(R.id.btnFoto);
        Button btnRegistrar = view.findViewById(R.id.btnRegistrar);

        btnFoto.setOnClickListener(v -> abrirCamera());
        btnRegistrar.setOnClickListener(v -> registrar());
    }

    private void abrirCamera() {
        File photoFile = criarArquivoFoto();
        photoUri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.trabalho1.fileprovider",
                photoFile);
        takePictureLauncher.launch(photoUri);
    }

    private File criarArquivoFoto() {
        String fileName = "foto_" + System.currentTimeMillis() + ".jpg";
        File storageDir = requireContext().getExternalFilesDir(null);
        return new File(storageDir, fileName);
    }

    private void registrar() {
        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        String confirmar = editConfirmarSenha.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmar.isEmpty()) {
            Toast.makeText(getContext(), R.string.msg_campos_obrigatorios, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!senha.equals(confirmar)) {
            Toast.makeText(getContext(), R.string.msg_senhas_diferentes, Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstancia(requireContext());
            Usuario existente = db.usuarioDao().buscarPorEmail(email);
            mainHandler.post(() -> {
                if (existente != null) {
                    Toast.makeText(getContext(), R.string.msg_email_existe, Toast.LENGTH_SHORT).show();
                } else {
                    Usuario usuario = new Usuario();
                    usuario.nome = nome;
                    usuario.email = email;
                    usuario.senha = senha;
                    usuario.foto = photoUri != null ? photoUri.toString() : null;

                    executor.execute(() -> {
                        db.usuarioDao().inserir(usuario);
                        tocarSomSucesso();
                        mainHandler.post(() -> {
                            Toast.makeText(getContext(), R.string.msg_cadastro_sucesso, Toast.LENGTH_LONG).show();
                            NavHostFragment.findNavController(RegisterFragment.this).navigateUp();
                        });
                    });
                }
            });
        });
    }

    private void tocarSomSucesso() {
        mainHandler.post(() -> {
            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.singing_whales);
            if (mp != null) {
                mp.setOnCompletionListener(MediaPlayer::release);
                mp.start();
            }
        });
    }
}
