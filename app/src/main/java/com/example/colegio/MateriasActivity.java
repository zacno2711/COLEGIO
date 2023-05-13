package com.example.colegio;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
    String nombreM, creditos, profesor, codigo, id_materia;
    Button jetbtGuardar, jetbtActivar, jetbtnCancelar, jetbtnAnular;
    ImageButton jetbtnConsultar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //
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
        jetbtnCancelar = findViewById(R.id.btCancelar);
        jetbtnConsultar = findViewById(R.id.btConsultar);
        jetbtnAnular = findViewById(R.id.btnAnular);
    }

    public void Guardar(View view) {
        switch (jetbtGuardar.getText().toString()) {
            case "GUARDAR":
                save();
                break;
            case "ACTUALIZAR":
                update();
                break;
        }
    }

    public void Consultar(View view) {
        Buscar();
    }

    public void Anular(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de eliminar esta materia?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica para eliminar la materia
                db.collection("Materias").document(id_materia).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Hint(false);
                        VisivilityBtn(false);
                        Limpiar_campos();
                        jetcodigo.setEnabled(true);
                        jetcodigo.requestFocus();
                        jetbtnAnular.setVisibility(View.INVISIBLE);
                        Toast.makeText(MateriasActivity.this, "Materia eliminada ...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MateriasActivity.this, "Error eliminando documento...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void Cancelar(View view) {
        jetbtGuardar.setEnabled(false);
        jetcodigo.setEnabled(true);
        jetactivo.setEnabled(false);
        jetactivo.setChecked(false);
        jetnombreM.setEnabled(false);
        jetcreditos.setEnabled(false);
        jetprofesor.setEnabled(false);
        jetbtActivar.setEnabled(false);
        Limpiar_campos();
        Hint(false);
        jetbtnAnular.setVisibility(View.INVISIBLE);
        VisivilityBtn(false);
    }

    public void Activar(View view) {
        Cheked();
    }

    public void CheckActivar(View view) {
        Cheked();
    }

    public void Regresar(View view) {
        Intent int_main = new Intent(this, MainActivity.class);
        startActivity(int_main);
    }

    ///////////////////////////////////////////------------------------------/////////////////////////------------------------------------////////////////////////////////////////////////////////////////////
    public void Buscar() {
        codigo = jetcodigo.getText().toString();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "codigo es requerido", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        } else {
            db.collection("Materias").whereEqualTo("Codigo", codigo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // No se encontraron documentos con el código especificado
                            jetbtGuardar.setText("GUARDAR");
                            jetbtGuardar.setEnabled(true);
                            jetcodigo.setEnabled(false);
                            jetnombreM.setEnabled(true);
                            jetcreditos.setEnabled(true);
                            jetprofesor.setEnabled(true);
                            jetbtnAnular.setVisibility(View.INVISIBLE);
                            Hint(true);
                            VisivilityBtn(true);
                            Toast.makeText(MateriasActivity.this, "No se encontró el código", Toast.LENGTH_SHORT).show();
                        } else {
                            // Se encontraron documentos, mostrar los datos en pantalla
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                jetbtGuardar.setText("ACTUALIZAR");
                                id_materia = document.getId();
                                jetbtGuardar.setEnabled(true);
                                jetcodigo.setEnabled(false);
                                jetactivo.setEnabled(true);
                                jetnombreM.setEnabled(true);
                                jetcreditos.setEnabled(true);
                                jetprofesor.setEnabled(true);
                                jetbtActivar.setEnabled(true);
                                jetbtnAnular.setVisibility(View.VISIBLE);
                                Hint(true);
                                VisivilityBtn(true);

                                id_materia = document.getId();
                                jetprofesor.setText(document.getString("NombreProfesor"));
                                jetnombreM.setText(document.getString("NombreMateria"));
                                jetcreditos.setText(document.getString("Creditos"));
                                if (document.getString("Activo").equals("Si"))
                                    jetactivo.setChecked(true);
                                else jetactivo.setChecked(false);
                            }
                        }
                    } else {
                        Toast.makeText(MateriasActivity.this, "Error al buscar el código", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }//Fin consultar

    public void update() {
        codigo = jetcodigo.getText().toString();
        nombreM = jetnombreM.getText().toString();
        creditos = jetcreditos.getText().toString();
        profesor = jetprofesor.getText().toString();
        if (nombreM.isEmpty() || creditos.isEmpty() || profesor.isEmpty()) {
            Toast.makeText(this, "Datos requeridos", Toast.LENGTH_SHORT).show();
        } else {
            // Create a new student with a first and last name
            Map<String, Object> materia = new HashMap<>();
            materia.put("Codigo", codigo);
            materia.put("NombreMateria", nombreM);
            materia.put("Creditos", creditos);
            materia.put("NombreProfesor", profesor);
            if (jetactivo.isChecked()) {
                materia.put("Activo", "Si");
            } else {
                materia.put("Activo", "No");
            }
            db.collection("Materias").document(id_materia).set(materia).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MateriasActivity.this, "Materia actualizada...", Toast.LENGTH_SHORT).show();
                    Limpiar_campos();
                    jetbtGuardar.setText("GUARDAR");
                    jetbtGuardar.setEnabled(false);
                    jetcodigo.setEnabled(true);
                    jetactivo.setEnabled(false);
                    jetactivo.setChecked(false);
                    jetnombreM.setEnabled(false);
                    jetcreditos.setEnabled(false);
                    jetprofesor.setEnabled(false);
                    jetbtActivar.setEnabled(false);
                    jetbtnAnular.setVisibility(View.INVISIBLE);
                    Hint(false);
                    VisivilityBtn(false);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MateriasActivity.this, "Error actualizando la Materia...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }//fin update

    public void save() {
        codigo = jetcodigo.getText().toString();
        nombreM = jetnombreM.getText().toString();
        creditos = jetcreditos.getText().toString();
        profesor = jetprofesor.getText().toString();
        if (nombreM.isEmpty() || creditos.isEmpty() || profesor.isEmpty() || codigo.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        } else {
            // Create a new Materia
            Map<String, Object> materia = new HashMap<>();
            materia.put("Codigo", codigo);
            materia.put("NombreMateria", nombreM);
            materia.put("Creditos", creditos);
            materia.put("NombreProfesor", profesor);
            materia.put("Activo", "Si");
            db.collection("Materias").add(materia).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Limpiar_campos();
                    Toast.makeText(MateriasActivity.this, "Materia guardada", Toast.LENGTH_SHORT).show();
                    jetbtGuardar.setText("GUARDAR");
                    jetbtGuardar.setEnabled(false);
                    jetcodigo.setEnabled(true);
                    jetactivo.setEnabled(false);
                    jetnombreM.setEnabled(false);
                    jetcreditos.setEnabled(false);
                    jetprofesor.setEnabled(false);
                    jetbtnAnular.setVisibility(View.INVISIBLE);
                    Hint(false);
                    VisivilityBtn(false);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Log.w(TAG, "Error adding document", e);
                    Toast.makeText(MateriasActivity.this, "Error guardando documento", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }//Fin Save

    public void Limpiar_campos() {
        jetcodigo.setText("");
        jetprofesor.setText("");
        jetcreditos.setText("");
        jetnombreM.setText("");
        jetcodigo.requestFocus();
    }//fin Limpiar_campos

    public void Hint(boolean bol) {
        if (bol == false) {
            jetprofesor.setHint("");
            jetprofesor.setVisibility(View.INVISIBLE);
            jetcreditos.setHint("");
            jetcreditos.setVisibility(View.INVISIBLE);
            jetnombreM.setHint("");
            jetnombreM.setVisibility(View.INVISIBLE);
        } else {
            jetprofesor.setHint("Nombre del profesor");
            jetprofesor.setVisibility(View.VISIBLE);
            jetcreditos.setHint("Creditos de la materia");
            jetcreditos.setVisibility(View.VISIBLE);
            jetnombreM.setHint("Nombre de la materia");
            jetnombreM.setVisibility(View.VISIBLE);
        }
    }

    public void VisivilityBtn(boolean bol) {
        if (bol == true) {
            jetbtnCancelar.setVisibility(View.VISIBLE);
            jetbtnConsultar.setVisibility(View.VISIBLE);
            jetbtActivar.setVisibility(View.VISIBLE);
            jetactivo.setVisibility(View.VISIBLE);
            jetbtGuardar.setVisibility(View.VISIBLE);
            jetbtnConsultar.setVisibility(View.INVISIBLE);
        } else {
            jetbtnCancelar.setVisibility(View.INVISIBLE);
            jetbtActivar.setVisibility(View.INVISIBLE);
            jetactivo.setVisibility(View.INVISIBLE);
            jetbtGuardar.setVisibility(View.INVISIBLE);
            jetbtnConsultar.setVisibility(View.VISIBLE);
        }
    }

    public void Cheked() {
        if (jetactivo.isChecked()) {
            jetactivo.setChecked(true);
            jetbtActivar.setText("ACTIVAR");
        } else {
            jetbtActivar.setText("DESACTIVAR");
            jetactivo.setChecked(false);
        }
    }
}