package com.example.bottonnavapp.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bottonnavapp.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView nombrePerfilDatos;
    private TextView telefonoPerfilDatos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar los campos TextView usando binding
        nombrePerfilDatos = binding.nombrePerfilDatos;
        telefonoPerfilDatos = binding.telefonoPerfilDatos;

        // Cargar datos del usuario
        cargarDatosUsuario();

        return root;
    }

    // Método para cargar datos del usuario desde Firestore
    @SuppressLint("SetTextI18n")
    private void cargarDatosUsuario() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("DatosUser").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("Nombre");
                            String telefono = documentSnapshot.getString("Telefono");

                            // Log para depuración
                            Log.d("NotificationsFragment", "Nombre: " + nombre);
                            Log.d("NotificationsFragment", "Telefono: " + telefono);

                            // Establecer los datos en los campos TextView
                            if (nombre != null && !nombre.isEmpty()) {
                                nombrePerfilDatos.setText(nombre);
                            } else {
                                nombrePerfilDatos.setText("No disponible");
                            }
                            if (telefono != null && !telefono.isEmpty()) {
                                telefonoPerfilDatos.setText(telefono);
                            } else {
                                telefonoPerfilDatos.setText("No disponible");
                            }
                            Toast.makeText(getContext(), "Datos del usuario cargados", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                            Log.d("NotificationsFragment", "No se encontraron datos del usuario");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al cargar datos del usuario", Toast.LENGTH_SHORT).show();
                        Log.e("NotificationsFragment", "Error al cargar datos del usuario", e);
                    });
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            Log.d("NotificationsFragment", "Usuario no autenticado");
        }
        actualizarDatosPerfil(nombrePerfilDatos.getText().toString(), telefonoPerfilDatos.getText().toString());
    }

    // Método para actualizar el datos del perfil
    public void actualizarDatosPerfil(String nombre, String telefono) {
        if (nombrePerfilDatos != null) {
            nombrePerfilDatos.setText(nombre);
        }
        if (telefonoPerfilDatos != null) {
            telefonoPerfilDatos.setText(telefono);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}