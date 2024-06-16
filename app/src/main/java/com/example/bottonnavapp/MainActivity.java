package com.example.bottonnavapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bottonnavapp.databinding.ActivityMainBinding;
import com.example.bottonnavapp.ui.notifications.NotificationsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        onStart();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Verifica si el usuario ha iniciado sesion y actualiza la UI de acuerdo a ello
        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println("User: " +currentUser);
        if(currentUser != null){
            //Si no es null ve a la pantalla principal directamente
            setContentView(binding.getRoot());
        } else{
            cambiarALogin(getCurrentFocus());
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
            Log.d("MainActicity", "UID: " + mAuth.getUid());
        }
    }

    /**
     * CAMBIOS DE VISTAS
     * */
    public void cambiarARegistro(View view){
        setContentView(R.layout.register);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Registro");
    }

    public void cambiarALogin(View view){
        setContentView(R.layout.login);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
    }

    //public void CambiarANuevoMedicamento(View view){setContentView(R.layout.nuevo_medicamento);}

    public void removeFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    public void Cancelar(View view){
        setContentView(binding.getRoot());
    }

    //CONSEJOS CON CHATGPT EN DASHBOARDFRAGMENT

    /**
     * CRUD DE MEDICAMENTO
     * */

    //CREATE
    public void CrearMedicamento(View view){
        System.out.println("Creando medicamento");

        EditText Nombre = findViewById(R.id.Nombre);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch Receta = findViewById(R.id.switchReceta);
        EditText Caducidad = findViewById(R.id.Caducidad);
        EditText Cantidad = findViewById(R.id.Cantidad);

        String nombreMedicamento = Nombre.getText().toString();
        boolean conReceta = Receta.isChecked();
        String fechaCaducidad = Caducidad.getText().toString();
        String cantidadStr = Cantidad.getText().toString();

        // Verificar si el nombre del medicamento está vacío
        if (nombreMedicamento.isEmpty()) {
            Toast.makeText(view.getContext(), "Por favor, introduce el nombre del medicamento.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si la fecha de caducidad está vacía
        if (fechaCaducidad.isEmpty()) {
            Toast.makeText(view.getContext(), "Por favor, introduce la fecha de caducidad.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica si el campo de cantidad está vacío o no es un número válido
        if (cantidadStr.isEmpty()) {
            Toast.makeText(view.getContext(), "Por favor, introduce una cantidad válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidadDosis;
        try {
            cantidadDosis = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(view.getContext(), "Por favor, introduce una cantidad válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Consigo el id asociado para crear el documento de botiquin
        String IdUsuario= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Map<String, Object> botiquin = new HashMap<>();
        botiquin.put("IdUsuario", IdUsuario);
        botiquin.put("NombreMedicamento", nombreMedicamento);
        botiquin.put("ConReceta", conReceta);
        botiquin.put("FechaCaducidad", fechaCaducidad);
        botiquin.put("CantidadDosis", cantidadDosis);

        // Añadir un nuevo documento
        db.collection("Botiquin").add(botiquin)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Documento añadido con ID: " + documentReference.getId());
                    // Navegar de vuelta a HomeFragment
                    getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error añadiendo documento", e));

        Toast.makeText(view.getContext(), "Nuevo Medicamento creado", Toast.LENGTH_SHORT).show();
    }

    //UPDATE
    public void EditarMedicamento(String docId, String idUsuario, String nombre, boolean conReceta, String fechaCaducidad, int cantidadDosis) {
        Map<String, Object> medicamento = new HashMap<>();
        medicamento.put("IdUsuario", idUsuario);
        medicamento.put("NombreMedicamento", nombre);
        medicamento.put("ConReceta", conReceta);
        medicamento.put("FechaCaducidad", fechaCaducidad);
        medicamento.put("CantidadDosis", cantidadDosis);

        db.collection("Botiquin").document(docId)
                .update(medicamento)
                .addOnSuccessListener(aVoid -> {
                    Log.d("MainActivity", "Documento actualizado con éxito!");
                    // Navegar de vuelta a HomeFragment
                    getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> Log.w("MainActivity", "Error actualizando el documento", e));
    }

    //DELETE
    public void BorrarMedicamento(String documentId, View view) {

        new AlertDialog.Builder(this)
                .setTitle("Borrar medicamento")
                .setMessage("¿Estás seguro de que quieres borrar el medicamento?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Borrar el documento con el ID proporcionado
                    db.collection("Botiquin").document(documentId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "Documento borrado exitosamente!");
                                // Navegar de vuelta a HomeFragment después de borrar
                                getSupportFragmentManager().popBackStack();
                                Toast.makeText(view.getContext(), "Medicamento borrado", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error borrando el documento", e);
                                Toast.makeText(view.getContext(), "Error al borrar el medicamento", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * CRUD DE USUARIO
     * */
    //CREATE
    public void Registrarse(View view){
        EditText Email = findViewById(R.id.EmailRegister);
        EditText Contrasenia = findViewById(R.id.PasswordRegister);
        EditText RepetirContrasenia = findViewById(R.id.RepetirPassword);

        if (emailValido(Email.getText().toString().trim())) {
            //Si el email es valido comprueba la contraseña
            if (contraseniaValida(Contrasenia.getText().toString().trim()) && Contrasenia.getText().toString().trim().equals(RepetirContrasenia.getText().toString().trim())) {
                //Si la contraseña es valida y coincide con la repeticion pasamos a registrar
                Toast.makeText(this, "Registrando usuario...", Toast.LENGTH_SHORT).show();
                register(Email.getText().toString(), Contrasenia.getText().toString(), MainActivity.this);
            } else {
                Toast.makeText(this, "Longitud (8, 24), MAYUS, MINUS, números y  simbolos @$!%*?&+/_-", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Email inválido. Por favor, introduce un email válido.", Toast.LENGTH_SHORT).show();
        }

    }

    private void register(String email, String password, Activity activity) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                // Login exitoso, proceso a crear un perfil con información extra del usuario
                Toast.makeText(getApplicationContext(), "Usuario creado correctamente " + Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).substring(0, mAuth.getCurrentUser().getEmail().indexOf('@')),
                        Toast.LENGTH_SHORT).show();

                String IdUsuario= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                String Nombre = Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).substring(0, mAuth.getCurrentUser().getEmail().indexOf('@'));

                //Hago esto para que al guardar dtos del Perfil ponga la primera letra en mayusculas
                String NombreMandar = Nombre.substring(0, 1).toUpperCase() + Nombre.substring(1);

                final Map<String, Object> perfil = new HashMap<>();
                perfil.put("IdUsuario", IdUsuario);
                perfil.put("Nombre", NombreMandar);
                perfil.put("Telefono", "");

                //Guardo en la coleccion de DatosUser la informacion del perfil del usuario
                db.collection("DatosUser").document(IdUsuario).set(perfil)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Documento creado con éxito."))
                        .addOnFailureListener(e -> Log.w(TAG, "Error al crear el documento", e));

                setContentView(R.layout.login);
            } else {
                // Si falla envia un mensaje al usuario
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(getApplicationContext(), "Este correo ya ha sido registrado", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
    }

    //READ
    public void IniciarSesion(View view){
        EditText Email = findViewById(R.id.EmailLogin);
        EditText Contrasenia = findViewById(R.id.PasswordLogin);

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
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");

                        //Para este toast voy a hacer una consulta sobre el nombre del usuario
                        CollectionReference collectionRef = db.collection("DatosUser");
                        FirebaseUser user = mAuth.getCurrentUser();

                        collectionRef.get()
                                .addOnSuccessListener(querySnapshot -> {
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        // Acceder a los datos del documento
                                        String IdUsuario = document.getString("IdUsuario");

                                        assert IdUsuario != null;
                                        assert user != null;
                                        if(IdUsuario.equals(user.getUid())){
                                            String Nombre = document.getString("Nombre");
                                            Toast.makeText(getApplicationContext(), "Bienvenido " + Nombre, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Manejar el error
                                });

                        //Recoger la lista de medicamentos del usuario
                        //LeerMedicamentos(getCurrentFocus());
                        updateUI(user);
                    } else {
                        // Si el Login falla manda un mensaje al usuario.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();

                        updateUI(null);
                    }
                });
    }

    //UPDATE
    public void EditarPerfil(View view){

        CollectionReference collectionRef = db.collection("DatosUser");
        FirebaseUser user = mAuth.getCurrentUser();
        setContentView(R.layout.editar_perfil);

        collectionRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // Acceder a los datos del documento
                        String IdUsuario = document.getString("IdUsuario");

                        assert IdUsuario != null;
                        assert user != null;
                        if(IdUsuario.equals(user.getUid())){
                            String Nombre = document.getString("Nombre");
                            String Telefono = document.getString("Telefono");

                            EditText editTextNombre = findViewById(R.id.EmailRegister);
                            EditText editTextPhone = findViewById(R.id.TelefonoUpdateProfile);

                            editTextNombre.setHint(Nombre);
                            editTextPhone.setHint(Telefono);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejar el error
                });
    }

    public void GuardarPerfil(View view) {
        EditText Phone = findViewById(R.id.TelefonoUpdateProfile);
        EditText Nombre = findViewById(R.id.EmailRegister);
        String IdUsuario = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String NombreUser;
        String PhoneUser;

        // Si el campo de teléfono no está vacío, validar su formato
        String phoneText = Phone.getText().toString();
        if (!phoneText.isEmpty() && !phoneText.matches("\\d{9}")) {
            Toast.makeText(view.getContext(), "El número de teléfono debe tener exactamente 9 dígitos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener valores de NombreUser y PhoneUser, utilizando hint si el campo está vacío
        NombreUser = Nombre.getText().toString().isEmpty() ? Nombre.getHint().toString() : Nombre.getText().toString();
        PhoneUser = Phone.getText().toString().isEmpty() ? Phone.getHint().toString() : Phone.getText().toString();

        final Map<String, Object> perfil = new HashMap<>();
        perfil.put("IdUsuario", IdUsuario);
        perfil.put("Nombre", NombreUser);
        perfil.put("Telefono", PhoneUser);

        // Verificar si el documento ya existe
        db.collection("DatosUser").document(IdUsuario).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // El documento existe, actualizarlo
                    db.collection("DatosUser").document(IdUsuario).update(perfil)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "Documento actualizado con éxito.");
                                Toast.makeText(view.getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                                NotificationsFragment fragment = (NotificationsFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_notifications);
                                if (fragment != null && fragment.isVisible()) {
                                    fragment.actualizarDatosPerfil(NombreUser, PhoneUser);
                                }
                                setContentView(binding.getRoot());
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error al actualizar el documento", e);
                                Toast.makeText(view.getContext(), "Error al actualizar perfil", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // El documento no existe, crearlo
                    db.collection("DatosUser").document(IdUsuario).set(perfil)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "Documento creado con éxito.");
                                Toast.makeText(view.getContext(), "Perfil creado", Toast.LENGTH_SHORT).show();
                                NotificationsFragment fragment = (NotificationsFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_notifications);
                                if (fragment != null && fragment.isVisible()) {
                                    fragment.actualizarDatosPerfil(NombreUser, PhoneUser);
                                }
                                setContentView(binding.getRoot());
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error al crear el documento", e);
                                Toast.makeText(view.getContext(), "Error al crear perfil", Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Log.d(TAG, "Error al obtener el documento: ", task.getException());
                Toast.makeText(view.getContext(), "Error al verificar perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //DELETE
    public void BorrarUsuario(View view){

        new AlertDialog.Builder(this)
                .setTitle("Borrar cuenta")
                .setMessage("¿Estás seguro de que quieres borrar tu cuenta?")
                .setPositiveButton("Sí", (dialog, which) -> borrarUsuario())
                .setNegativeButton("No", null)
                .show();
    }

    private void borrarUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Borrar datos de Firestore Database en la colección "DatosUser"
            db.collection("DatosUser").document(userId).delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Datos de usuario eliminados de Firestore Database");

                    // Borrar documentos en la colección "Medicamentos" donde el campo "userId" coincide
                    db.collection("Botiquin").whereEqualTo("IdUsuario", userId).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful() && task1.getResult() != null) {
                            for (DocumentSnapshot document : task1.getResult().getDocuments()) {
                                db.collection("Botiquin").document(document.getId()).delete().addOnCompleteListener(task11 -> {
                                    if (task11.isSuccessful()) {
                                        Log.d(TAG, "Documento de Botiquin eliminado.");

                                    } else {
                                        Log.w(TAG, "Error al eliminar el documento de Botiquin.", task11.getException());
                                    }
                                });
                            }
                        } else {
                            Log.w(TAG, "Error al obtener los documentos de Botiquin.", task1.getException());
                        }

                        // Borrar cuenta de usuario en Firebase Authentication
                        user.delete().addOnCompleteListener(task112 -> {
                            if (task112.isSuccessful()) {
                                Log.d(TAG, "Cuenta de usuario eliminada.");
                                Toast.makeText(MainActivity.this, "Cuenta eliminada.", Toast.LENGTH_SHORT).show();
                                //limpiar datos
                                //listamedicamentos.clear();
                                //Quitar el fragmento
                                removeFragment();

                                //Llamar a la funcion que reinicia la app
                                onStart();
                            } else {
                                Log.w(TAG, "Error al eliminar la cuenta de usuario.", task112.getException());
                                Toast.makeText(MainActivity.this, "Error al eliminar la cuenta.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else {
                    Log.w(TAG, "Error al eliminar los datos de usuario de Firestore Database.", task.getException());
                    Toast.makeText(MainActivity.this, "Error al eliminar los datos de usuario.", Toast.LENGTH_SHORT).show();
                }
            });
            onStart();
        } else {
            Toast.makeText(MainActivity.this, "No se ha encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    //SIGNOUT
    public void CerrarSesion(View view){
        System.out.println("Cerrando: " + Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println("User cerrado: " +currentUser);

        //Quitar el fragmento
        removeFragment();
        //Llamar a la funcion que reinicia la app
        onStart();
    }


    /**
     * VALIDACIONES DE REGISTRO
     **/

    public static boolean emailValido(String email) {
        //Función REGEX que exige que haya texto antes de la @, que haya @,
        // que haya texto despues de la arroba y que despues del punto haya 2 o 3 letras
        String patron = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";
        return email != null && email.matches(patron);
    }

    public static boolean contraseniaValida(String password) {
        // Funcion REGEX que exige que tiene que tener minimo 8 caracteres y máximo 24,
        // tiene que incluir Mayusuculas, minusculas, numeros y algun caracter especial
        String patron = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&+/_-])[A-Za-z\\d@$!%*?&+/_-]{8,24}$";

        // Comprobar si la contraseña coincide con la expresión regular y no contiene espacios
        return password != null && password.matches(patron) && !password.contains(" ");
    }

    }