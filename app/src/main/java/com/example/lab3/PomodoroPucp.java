package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3.databinding.ActivityPomodoropucpBinding;
import com.example.lab3.viewmodel.ContadorViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

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
                startTimer25(contadorViewModel, executorService, userid, userFullname, userEmail, userGender);
            }

        });
        //****************************


    }
    //logica del contador
    private void startTimer25(ContadorViewModel contadorViewModel, ExecutorService executorService, String userid, String userFullname, String userEmail, String userGender) {
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
                runOnUiThread(() -> {
                    resetButtonEntreTiempos();
                    isRunning = true;
                    startTimer5(contadorViewModel, executorService, userid, userFullname, userEmail, userGender);
                    new MaterialAlertDialogBuilder(PomodoroPucp.this)
                            .setTitle("¡Felicidades!")
                            .setMessage("Empezó el tiempo de descanso!")
                            .setPositiveButton("Entendido", (dialog, which) -> {
                                Intent intent = new Intent(PomodoroPucp.this, CambiaEstados.class);
                                intent.putExtra("user_id", userid);
                                intent.putExtra("user_fullname", userFullname);
                                intent.putExtra("user_email", userEmail);
                                intent.putExtra("user_gender", userGender);

                                startActivity(intent);
                                dialog.dismiss();
                            })
                            .show();
                });
            }
        });
    }

    private void startTimer5(ContadorViewModel contadorViewModel, ExecutorService executorService, String userid, String userFullname, String userEmail, String userGender) {

        currentTask = executorService.submit(() -> {

            int totalTimeInSeconds = 5 * 60;

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
                runOnUiThread(() -> {
                    resetButton();
                    isRunning = true;
                    handler.post(() -> {
                        binding.textOtrotimer.setText("Fin del descanso");
                    });
                    new MaterialAlertDialogBuilder(PomodoroPucp.this)
                            .setTitle("Atencion")
                            .setMessage("Terminó el tiempo de descanso. Dale al boton de reinicio para empezar otro ciclo.")
                            .setPositiveButton("Entendido", (dialog, which) -> {
                                Intent intent = new Intent(PomodoroPucp.this, PomodoroPucp.class);
                                intent.putExtra("user_id", userid);
                                intent.putExtra("user_fullname", userFullname);
                                intent.putExtra("user_email", userEmail);
                                intent.putExtra("user_gender", userGender);

                                startActivity(intent);
                                dialog.dismiss();
                            })
                            .show();
                });
            }
        });
    }

    private void stopTimer() {
        isRunning = false;
        if (currentTask != null) {
            currentTask.cancel(true);
        }
        resetButton();  // Restaurar el botón
    }

    private void resetButton() {
        handler.post(() -> {
            binding.elevatedButton.setIcon(getDrawable(R.drawable.play));
            binding.textContador.setText("25:00");
        });
        isRunning = false;
    }

    private void resetButtonEntreTiempos() {
        handler.post(() -> {
            binding.elevatedButton.setIcon(getDrawable(R.drawable.restart));
            binding.textContador.setText("5:00");
            binding.textOtrotimer.setText("En descanso");
        });
        isRunning = true;
    }
    //**************************

    //boton logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.favorite) {
            ApplicationThreads application = (ApplicationThreads) getApplication();
            application.shutdownExecutor();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            finishAffinity();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //*************************

}
