package com.example.lab3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3.others.LoginRequest;
import com.example.lab3.others.RetrofitClient;
import com.example.lab3.others.Usuario;
import com.example.lab3.services.LogueoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText usuarioIngresado, contrasenaIngresado;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioIngresado = findViewById(R.id.editTextUsuario);
        contrasenaIngresado = findViewById(R.id.editTextContrasena);
        btnLogin = findViewById(R.id.buttonIniciarSesion);

        btnLogin.setOnClickListener(v -> {
            String username = usuarioIngresado.getText().toString();
            String password = contrasenaIngresado.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                loginUser(username, password);
            } else {
                Toast.makeText(MainActivity.this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loginUser(String username, String password) {
        LogueoService logueoService = RetrofitClient.getRetrofitInstance().create(LogueoService.class);
        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<Usuario> call = logueoService.login(loginRequest);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Usuario usuario = response.body();
                    Toast.makeText(MainActivity.this, "Bienvenido, " + usuario.getFirstName(), Toast.LENGTH_SHORT).show();
                    // Aquí puedes lanzar el TimerActivity u otra actividad.
                } else {
                    Toast.makeText(MainActivity.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}