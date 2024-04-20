package com.example.bottonnavapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bottonnavapp.Modelo.Medicamento;
import com.example.bottonnavapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private ActivityMainBinding binding;

    private ArrayList<Medicamento> listamedicamentos = new ArrayList<Medicamento>();
    private ListView lvDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //mio
        setContentView(R.layout.login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        onStart();

        /*
        //prueba
        lvDatos = (ListView) findViewById(R.id.lvDatos);
        ArrayAdapter<Medicamento> adapter = new ArrayAdapter<Medicamento>(MainActivity.this, android.R.layout.simple_list_item_1, listamedicamentos);
        lvDatos.setAdapter(adapter);
        //fin

         */
    /*
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

     */
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Si no es null ve a la pantalla principal directamente
        } else{
            Toast.makeText(getApplicationContext(), "Inicie Sesion", Toast.LENGTH_SHORT).show();

        }
    }

    public void cambiarARegistro(View view){
        setContentView(R.layout.register);
    }

    public void cambiarALogin(View view){
        setContentView(R.layout.login);
    }

    public void Registrarse(View view){
        EditText Email = findViewById(R.id.Email);
        EditText Contrasenia = findViewById(R.id.Password);
        EditText RepetirContrasenia = findViewById(R.id.RepetirPassword);

        if (Email.getText().toString().contains("@") && !Email.getText().toString().substring(Email.getText().toString().indexOf('@') + 1).contains("@")) {
            if (Email.getText().toString().contains(".") && !Email.getText().toString().substring(Email.getText().toString().indexOf('.') + 1).contains(".")) {
                if (Email.getText().toString().contains("@gmail.com")) { //&& Email.getText().toString().substring(Email.getText().toString().indexOf("@gmail.com") + 1).isEmpty()
                    if (Contrasenia.length() > 5) {
                        if (Contrasenia.getText().toString().equals(RepetirContrasenia.getText().toString())) {
                            //Toast.makeText(getApplicationContext(), "Condiciones correctas", Toast.LENGTH_SHORT).show();
                            register(Email.getText().toString(), Contrasenia.getText().toString(), MainActivity.this);
                        } else {
                            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "El email debe terminar en '@gmail.com'", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "El email debe tener un '.'", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "El email debe tener una '@'", Toast.LENGTH_SHORT).show();
        }
    }

    private void register(String email, String password, Activity activity) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) activity, (OnCompleteListener<AuthResult>) new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(getApplicationContext(), "Usuario creado correctamente " + mAuth.getCurrentUser().getEmail().substring(0, mAuth.getCurrentUser().getEmail().indexOf('@')),
                            Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    /*
                    //Crear lista de medicamentos asociada
                    //Consigo el id asociado para crear el documento de botiquin
                    String uid= mAuth.getCurrentUser().getUid();
                    Map<String, Object> botiquin = new HashMap<>();
                    botiquin.put("CantidadDosis", "");
                    botiquin.put("DosisTiempo", "");
                    botiquin.put("FechaCaducidad", "");
                    botiquin.put("NombreMedicamento", "");
                    botiquin.put("Uso", "");
                    botiquin.put("ConReceta", "");
                    botiquin.put("IdUsuario", uid);

                    // Add a new document with a generated ID
                    db.collection("Botiquin").add(botiquin)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    System.out.println("Se ha creado un botiquin");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    System.out.println("Ha fallado la creacion");
                                }
                            });
                     */


                    setContentView(R.layout.login);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Este correo ya ha sido registrado", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void IniciarSesion(View view){
        EditText Email = findViewById(R.id.Email);
        EditText Contrasenia = findViewById(R.id.Password);

        if(Email.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Completa el correo", Toast.LENGTH_SHORT).show();
        } else if(Contrasenia.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Completa la contraseña", Toast.LENGTH_SHORT).show();
        } else {
            signIn(Email.getText().toString(), Contrasenia.getText().toString());
        }

    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(getApplicationContext(), "Bienvenido " + user.getEmail().substring(0, user.getEmail().indexOf('@')), Toast.LENGTH_SHORT).show();

                            //Cargar la lista de medicamentos suyos
                            CollectionReference collectionRef = db.collection("Botiquin");

                            collectionRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot querySnapshot) {
                                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                                // Acceder a los datos del documento
                                                String IdUsuario = document.getString("IdUsuario");

                                                if(IdUsuario.equals(mAuth.getCurrentUser().getUid())){
                                                    //System.out.println(document.getId().toString());
                                                    String CantidadDosis = document.getString("CantidadDosis");
                                                    String FechaCaducidad = document.getString("FechaCaducidad");
                                                    String NombreMedicamento = document.getString("NombreMedicamento");
                                                    String Uso = document.getString("Uso");
                                                    String ConReceta = document.getString("ConReceta");

                                                    Medicamento medicamento = new Medicamento(CantidadDosis, FechaCaducidad,NombreMedicamento,Uso, ConReceta,IdUsuario);

                                                    listamedicamentos.add(medicamento);
                                                    lvDatos = (ListView) findViewById(R.id.lvDatos);
                                                    ArrayAdapter<Medicamento> adapter = new ArrayAdapter<Medicamento>(MainActivity.this, android.R.layout.simple_list_item_1, listamedicamentos);
                                                    lvDatos.setAdapter(adapter);
                                                }
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Manejar el error
                                        }
                                    });


                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();

                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Actualiza la interfaz de usuario con la información del usuario
            //setContentView(R.layout.activity_main);

            //nuevo
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);

            //prueba
            lvDatos = (ListView) findViewById(R.id.lvDatos);
            ArrayAdapter<Medicamento> adapter = new ArrayAdapter<Medicamento>(MainActivity.this, android.R.layout.simple_list_item_1, listamedicamentos);
            lvDatos.setAdapter(adapter);

            System.out.println("datos:" + lvDatos.toString());
        }
    }

    public void NuevoMedicamento(View view){
        System.out.println("Nuevo medicamento");
        setContentView(R.layout.nuevo_medicamento);
        //Toast.makeText(view.getContext(), "Nuevo Medicamento", Toast.LENGTH_SHORT);
    }

    public void CrearMedicamento(View view){
        System.out.println("Creando");

        EditText Cantidad = findViewById(R.id.Cantidad);
        EditText Receta = findViewById(R.id.Receta);
        EditText Caducidad = findViewById(R.id.Caducidad);
        EditText Uso = findViewById(R.id.Usos);
        EditText Nombre = findViewById(R.id.Nombre);
        String uid= mAuth.getCurrentUser().getUid();


        //System.out.println("Cantidad" + Cantidad.getText().toString());

                    //Consigo el id asociado para crear el documento de botiquin
                    String IdUsuario= mAuth.getCurrentUser().getUid();
                    Map<String, Object> botiquin = new HashMap<>();
                    botiquin.put("CantidadDosis", Cantidad.getText().toString());
                    botiquin.put("FechaCaducidad", Caducidad.getText().toString());
                    botiquin.put("NombreMedicamento", Nombre.getText().toString());
                    botiquin.put("Uso", Uso.getText().toString());
                    botiquin.put("ConReceta", Receta.getText().toString());
                    botiquin.put("IdUsuario", IdUsuario);

                    // Add a new document with a generated ID
                    db.collection("Botiquin").add(botiquin)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    System.out.println("Se ha creado un botiquin");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    System.out.println("Ha fallado la creacion");
                                }
                            });

        setContentView(binding.getRoot());
        //Toast.makeText(view.getContext(), "Nuevo Medicamento", Toast.LENGTH_SHORT);
    }

    public void CerrarSesion(View view){
        System.out.println("Cerrando: " + mAuth.getCurrentUser().getUid());
        mAuth.signOut();
        //setContentView(null);
        setContentView(R.layout.login);
    }

    public void BorrarUsuario(View view){
        System.out.println("Borrando: " + mAuth.getCurrentUser().getUid());

        mAuth.getCurrentUser().delete();

        //Falta borrar los datos asociados en botiquin :)
        setContentView(R.layout.login);
    }

    public void EditarPerfil(View view){
        System.out.println("Editando: " + mAuth.getCurrentUser().getUid());


        setContentView(R.layout.editar_perfil);
    }
    }