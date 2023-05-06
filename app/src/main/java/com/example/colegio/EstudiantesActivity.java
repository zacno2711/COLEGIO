package com.example.colegio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class EstudiantesActivity extends AppCompatActivity {

    EditText jetcarnet,jetnombre,jetcarrera,jetsemestre;
    CheckBox jcbactivo;
    String carnet,nombre,carrera,semestre,id_documento;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar objetos Java con objetos Xml
        jetcarnet=findViewById(R.id.etcarnet);
        jetnombre=findViewById(R.id.etnombre);
        jetcarrera=findViewById(R.id.etcarrera);
        jetsemestre=findViewById(R.id.etsemestre);
        jcbactivo=findViewById(R.id.cbactivo);
    }

    public void Adicionar(View view){
        carnet=jetcarnet.getText().toString();
        nombre=jetnombre.getText().toString();
        carrera=jetcarrera.getText().toString();
        semestre=jetsemestre.getText().toString();
        if (carnet.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetcarnet.requestFocus();
        }else{
            // Create a new student
            Map<String, Object> estudiante = new HashMap<>();
            estudiante.put("Carnet", carnet);
            estudiante.put("Nombre", nombre);
            estudiante.put("Carrera", carrera);
            estudiante.put("Semestre", semestre);
            estudiante.put("Activo", "Si");

// Add a new document with a generated ID
            db.collection("Estudiantes")
                    .add(estudiante)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Limpiar_campos();
                            Toast.makeText(EstudiantesActivity.this, "Documento guardado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error adding document", e);
                            Toast.makeText(EstudiantesActivity.this, "Error guardando documento", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }//fin adicionar

    public void Consultar(View view){
        carnet=jetcarnet.getText().toString();
        if (carnet.isEmpty()){
            Toast.makeText(this, "numero de carnet es requerido", Toast.LENGTH_SHORT).show();
            jetcarnet.requestFocus();
        }else{
            db.collection("Estudiantes")
                    .whereEqualTo("Carnet",carnet)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    id_documento=document.getId();
                                    jetnombre.setText(document.getString("Nombre"));
                                    jetcarrera.setText(document.getString("Carrera"));
                                    jetsemestre.setText(document.getString("Semestre"));
                                    if (document.getString("Activo").equals("Si"))
                                        jcbactivo.setChecked(true);
                                    else
                                        jcbactivo.setChecked(false);
                                    //  Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Toast.makeText(EstudiantesActivity.this, "Estudiante no existe", Toast.LENGTH_SHORT).show();
                                // Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }//Fin consultar

    public void Modificar(View view){
        carnet=jetcarnet.getText().toString();
        nombre=jetnombre.getText().toString();
        carrera=jetcarrera.getText().toString();
        semestre=jetsemestre.getText().toString();
        if (carnet.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()){
            Toast.makeText(this, "Datos requeridos", Toast.LENGTH_SHORT).show();
            jetcarnet.requestFocus();
        }else{
            // Create a new student with a first and last name
            Map<String, Object> estudiante = new HashMap<>();
            estudiante.put("Carnet", carnet);
            estudiante.put("Nombre", nombre);
            estudiante.put("Carrera",carrera);
            estudiante.put("Semestre", semestre);
            estudiante.put("Activo", "Si");
            db.collection("Estudiantes").document(id_documento)
                    .set(estudiante)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EstudiantesActivity.this,"Documento actualizado...",Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EstudiantesActivity.this,"Error actualizando documento...",Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    private void Limpiar_campos(){
        jetcarnet.setText("");
        jetsemestre.setText("");
        jetcarrera.setText("");
        jetnombre.setText("");
        jcbactivo.setChecked(false);
        jetcarnet.requestFocus();
    }
}