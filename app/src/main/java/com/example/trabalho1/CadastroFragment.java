package com.example.trabalho1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho1.database.AppDatabase;
import com.example.trabalho1.database.Usuario;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroFragment extends Fragment {

    private ImageView ivFoto;
    private Uri fotoUri;
    private String currentPhotoPath;

    private final ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    ivFoto.setImageURI(fotoUri);
                }
            }
    );

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    abrirCamera();
                } else {
                    Toast.makeText(getContext(), "Permissão de câmera negada", Toast.LENGTH_SHORT).show();
                }
            }
    );

    public CadastroFragment() {
        super(R.layout.fragment_cadastro);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivFoto = view.findViewById(R.id.ivFotoUsuario);
        Button btnFoto = view.findViewById(R.id.btnTirarFoto);
        EditText etNome = view.findViewById(R.id.etNome);
        EditText etEmail = view.findViewById(R.id.etEmailCadastro);
        EditText etSenha = view.findViewById(R.id.etSenhaCadastro);
        Button btnSalvar = view.findViewById(R.id.btnSalvarUsuario);

        btnFoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                abrirCamera();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        btnSalvar.setOnClickListener(v -> {
            Usuario u = new Usuario();
            u.nome = etNome.getText().toString();
            u.email = etEmail.getText().toString();
            u.senha = etSenha.getText().toString();
            u.fotoUri = (fotoUri != null) ? fotoUri.toString() : "";

            if (u.nome.isEmpty() || u.email.isEmpty() || u.senha.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.getDatabase(requireContext()).appDao().inserirUsuario(u);
            Toast.makeText(getContext(), R.string.usuario_cadastrado, Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void abrirCamera() {
        try {
            File photoFile = createImageFile();
            fotoUri = FileProvider.getUriForFile(requireContext(),
                    "com.example.trabalho1.fileprovider",
                    photoFile);
            takePictureLauncher.launch(fotoUri);
        } catch (IOException ex) {
            Toast.makeText(getContext(), "Erro ao criar arquivo de imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(requireContext().getFilesDir(), "Pictures");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
