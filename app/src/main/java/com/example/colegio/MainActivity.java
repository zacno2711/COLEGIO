package com.example.colegio;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
    }

    public void Estudiante(View view){
        Intent int_estudiante=new Intent(this,EstudiantesActivity.class);
        startActivity(int_estudiante);
    }

    public void Matricula(View view){
        Intent int_matricula=new Intent(this,MatriculasActivity.class);
        startActivity(int_matricula);
    }

    public void Materias(View view){
        Intent int_materia=new Intent(this,MateriasActivity.class);
        startActivity(int_materia);
    }
}