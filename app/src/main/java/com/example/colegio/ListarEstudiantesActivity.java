package com.example.colegio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListarEstudiantesActivity extends AppCompatActivity {

    RecyclerView rvestudiantes;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ArrayList<ClsEstudiantes> alestudiantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_estudiantes);
        //Ocultar la barra de titulo, asociar objeto Xml conJava, llamar un
        //metodo private para cargar los datos
        getSupportActionBar().hide();
        rvestudiantes=findViewById(R.id.rvlistarestudiantes);
        alestudiantes=new ArrayList<>();
        rvestudiantes.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rvestudiantes.setHasFixedSize(true);
        Cargar_datos();
    }//fin onCreate

    private void Cargar_datos(){
        db.collection("Estudiantes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                ClsEstudiantes objestudiante=new ClsEstudiantes();
                                objestudiante.setCarnet(document.getString("Carnet"));
                                objestudiante.setNombre(document.getString("Nombre"));
                                objestudiante.setCarrera(document.getString("Carrera"));
                                objestudiante.setSemestre(document.getString("Semestre"));
                                objestudiante.setActivo(document.getString("Activo"));
                                alestudiantes.add(objestudiante);
                            }
                            EstudianteAdapter adestudiante=new EstudianteAdapter(alestudiantes);
                            rvestudiantes.setAdapter(adestudiante);
                        } else {
                            Toast.makeText(ListarEstudiantesActivity.this, "Documentos no hallados", Toast.LENGTH_SHORT).show();
                            // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }
}