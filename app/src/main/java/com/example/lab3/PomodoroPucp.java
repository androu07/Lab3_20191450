package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3.databinding.ActivityPomodoropucpBinding;
import com.example.lab3.viewmodel.ContadorViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class PomodoroPucp extends AppCompatActivity {

    private ActivityPomodoropucpBinding binding;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRunning = false;
    private Future<?> currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPomodoropucpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService= application.executorService;

        //Info del usuario en el card
        Intent intent = getIntent();
        String userid = intent.getStringExtra("user_id");
        String userFullname = intent.getStringExtra("user_fullname");
        String userEmail = intent.getStringExtra("user_email");
        String userGender = intent.getStringExtra("user_gender");

        TextView userNameTextView = findViewById(R.id.user_name);
        TextView userEmailTextView = findViewById(R.id.user_email);
        ImageView userIconImageView = findViewById(R.id.user_icon);

        userNameTextView.setText(userFullname);
        userEmailTextView.setText(userEmail);
        if (userGender.equalsIgnoreCase("male")) {
            userIconImageView.setImageResource(R.drawable.hombre);
        } else if (userGender.equalsIgnoreCase("female")) {
            userIconImageView.setImageResource(R.drawable.mujer);
        }
        //****************************

        //Contador
        ContadorViewModel contadorViewModel =
                new ViewModelProvider(PomodoroPucp.this).get(ContadorViewModel.class);

        contadorViewModel.getContador().observe(this, contador -> {
            binding.textContador.setText(String.valueOf(contador));
        });
        binding.elevatedButton.setOnClickListener(view -> {

            if (isRunning) {
                stopTimer();
            } else {
                startTimer25(contadorViewModel, executorService);
            }



        });
        //****************************


    }
    private void startTimer25(ContadorViewModel contadorViewModel, ExecutorService executorService) {
        isRunning = true;
        binding.elevatedButton.setIcon(getDrawable(R.drawable.restart));

        currentTask = executorService.submit(() -> {

            int totalTimeInSeconds = 25 * 60;

            for (int i = totalTimeInSeconds; i >= 0; i--) {

                int minutes = i / 60;
                int seconds = i % 60;

                String timeFormatted = String.format("%02d:%02d", minutes, seconds);

                contadorViewModel.getContador().postValue(timeFormatted);

                Log.d("msg-test", "Time remaining: " + timeFormatted);

                handler.post(() -> binding.textContador.setText(timeFormatted));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (isRunning) {
                handler.post(() -> resetButton());
            }
        });
    }

    private void stopTimer() {
        isRunning = false;  // Detener el cronómetro
        if (currentTask != null) {
            currentTask.cancel(true);  // Cancelar la tarea del cronómetro
        }
        resetButton();  // Restaurar el botón
    }

    private void resetButton() {
        handler.post(() -> {
            binding.elevatedButton.setIcon(getDrawable(R.drawable.play));  // Restaurar el icono a 'play'
            binding.textContador.setText("25:00");  // Reiniciar el tiempo mostrado
        });
        isRunning = false;  // Cambiar el estado del cronómetro
    }

}
