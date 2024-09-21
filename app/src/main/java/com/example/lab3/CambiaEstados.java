package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CambiaEstados extends AppCompatActivity {

    private Spinner taskSpinner;
    private Button changeStatusButton;
    private String userid;
    private List<String> taskTitles = new ArrayList<>();
    private List<String> taskIds = new ArrayList<>();
    private List<Boolean> taskCompletedStates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_estados);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.arrow_back_24dp_000000_fill0_wght400_grad0_opsz24);

        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        Intent intent = getIntent();
        String userFullname = intent.getStringExtra("user_fullname");
        userid = intent.getStringExtra("user_id");
        TextView userNameTextView = findViewById(R.id.textNombre);
        userNameTextView.setText("Ver tareas de " + userFullname + ":");

        taskSpinner = findViewById(R.id.task_spinner);
        changeStatusButton = findViewById(R.id.change_status_button);

        obtenerTareasUsuario(userid);

        changeStatusButton.setOnClickListener(view -> {
            int selectedPosition = taskSpinner.getSelectedItemPosition();
            String selectedTaskId = taskIds.get(selectedPosition);

            if (selectedTaskId != null) {
                boolean currentCompletedState = taskCompletedStates.get(selectedPosition);
                cambiarEstadoTareaLocalmente(selectedPosition, selectedTaskId, !currentCompletedState);
            }
        });

    }

    private void obtenerTareasUsuario(String userId) {
        String url = "https://dummyjson.com/todos/user/" + userId;

        taskTitles.clear();
        taskIds.clear();
        taskCompletedStates.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray todosArray = response.getJSONArray("todos");

                        for (int i = 0; i < todosArray.length(); i++) {
                            JSONObject task = todosArray.getJSONObject(i);
                            String taskid = task.getString("id");
                            String taskname = task.getString("todo");
                            boolean  taskcompleted = task.getBoolean("completed");

                            if(!taskcompleted){
                                String taskTitle = taskname + " - No completado";
                                taskTitles.add(taskTitle);
                                taskIds.add(taskid);
                                taskCompletedStates.add(taskcompleted);
                            }else {
                                String taskTitle = taskname + " - Completado";
                                taskTitles.add(taskTitle);
                                taskIds.add(taskid);
                                taskCompletedStates.add(taskcompleted);
                            }

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CambiaEstados.this, android.R.layout.simple_spinner_item, taskTitles);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskSpinner.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(CambiaEstados.this, "Error al obtener las tareas", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void cambiarEstadoTareaLocalmente(int position, String taskId, boolean nuevoEstado) {
        String url = "https://dummyjson.com/todos/" + taskId;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("completed", nuevoEstado);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest putTaskRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                response -> {

                    taskCompletedStates.set(position, nuevoEstado);
                    String taskName = taskTitles.get(position).split(" - ")[0];
                    String updatedTaskTitle = taskName + (nuevoEstado ? " - Completado" : " - No completado");

                    taskTitles.set(position, updatedTaskTitle);

                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) taskSpinner.getAdapter();
                    adapter.notifyDataSetChanged();

                    Toast.makeText(CambiaEstados.this, "Estado de la tarea cambiado exitosamente", Toast.LENGTH_SHORT).show();

                },
                error -> {
                    Toast.makeText(CambiaEstados.this, "Error al cambiar el estado de la tarea", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(putTaskRequest);
    }
}

