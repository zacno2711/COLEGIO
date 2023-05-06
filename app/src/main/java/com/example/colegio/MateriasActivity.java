package com.example.colegio;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.HashMap;
import java.util.Map;

public class MateriasActivity extends AppCompatActivity {
    EditText jetnombreM, jetcreditos, jetprofesor, jetcodigo;
    CheckBox jetactivo;
    String nombreM,creditos,profesor,codigo,id_documento;
    Button jetbtGuardar, jetbtActivar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //
    // boolean kiss = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);
        getSupportActionBar().hide();
        jetcodigo = findViewById(R.id.etcodigo);
        jetnombreM = findViewById(R.id.etnombreM);
        jetcreditos = findViewById(R.id.etcreditos);
        jetprofesor = findViewById(R.id.etprofesor);
        jetactivo = findViewById(R.id.cbactivo);
        jetbtActivar = findViewById(R.id.btActivar);
        jetbtGuardar = findViewById(R.id.btGuardar);
    }
    public void Guardar(View view) {
        if(jetbtGuardar.getText().toString()=="Actualizar"){
            Toast.makeText(this, "Actualizacion pendiente", Toast.LENGTH_SHORT).show();
        }
        else {
            codigo = jetcodigo.getText().toString();
            nombreM = jetnombreM.getText().toString();
            creditos = jetcreditos.getText().toString();
            profesor = jetprofesor.getText().toString();
            if (nombreM.isEmpty() || creditos.isEmpty() || profesor.isEmpty() || codigo.isEmpty()) {
                Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
                jetcodigo.requestFocus();
            } else {
               // if (FirebaseFirestore.CollectionGroup()){}

                // Create a new Materia
                Map<String, Object> Materia = new HashMap<>();
                Materia.put("Codigo", codigo);
                Materia.put("NombreMateria", nombreM);
                Materia.put("Creditos", creditos);
                Materia.put("NombreProfesor", profesor);
                Materia.put("Activo", "Si");

                db.collection("Materias")
                        .add(Materia)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Limpiar_campos();
                                Toast.makeText(MateriasActivity.this, "Materia guardada", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Log.w(TAG, "Error adding document", e);
                                Toast.makeText(MateriasActivity.this, "Error guardando Materia", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } }

    public void Consultar(View view) {
        codigo = jetcodigo.getText().toString();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingresa el codigo para consultar", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
            //kiss = false;
        }else{
                    //kiss = true;
            db.collection("Materias")
                    .whereEqualTo("Codigo",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                jetbtGuardar.setEnabled(true);
                                jetbtGuardar.setText("ACTUALIZAR");
                                for (DocumentSnapshot document : task.getResult()) {
                                    jetcodigo.setEnabled(false);
                                    id_documento = document.getId();
                                    jetnombreM.setText(document.getString("NombreMateria"));
                                    jetnombreM.setEnabled(true);
                                    jetcreditos.setText(document.getString("Creditos"));
                                    jetcreditos.setEnabled(true);
                                    jetprofesor.setText(document.getString("NombreProfesor"));
                                    jetprofesor.setEnabled(true);
                                    jetactivo.setEnabled(true);
                                    if (document.getString("Activo").equals("Si")) {
                                        jetactivo.setChecked(true);
                                    } else {
                                        jetactivo.setChecked(false);
                                    }
                                    Toast.makeText(MateriasActivity.this, "Consulta Exitosa", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MateriasActivity.this, "Materia no existe ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        });
        }
    }
    public void Limpiar_campos(){
        jetcodigo.setText("");
        jetprofesor.setText("");
        jetcreditos.setText("");
        jetnombreM.setText("");
        jetcodigo.requestFocus();
    }
}