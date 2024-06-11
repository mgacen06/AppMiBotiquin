package com.example.bottonnavapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottonnavapp.Modelo.Medicamento;
import com.example.bottonnavapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView medicationRecyclerView;
    private MiBotiquinAdapter medicationAdapter;
    private List<Medicamento> medicationList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configurar el botón para añadir medicamentos
        Button addMedicationButton = root.findViewById(R.id.button2);
        addMedicationButton.setOnClickListener(v -> CambiarANuevoMedicamento());

        // Configurar RecyclerView
        medicationRecyclerView = root.findViewById(R.id.medicationRecyclerView);
        medicationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        medicationList = new ArrayList<>();
        medicationAdapter = new MiBotiquinAdapter(medicationList, medicamento -> {
            // Manejar clic en el elemento del medicamento
        });
        medicationRecyclerView.setAdapter(medicationAdapter);

        CargarMedicamentos();

        return root;
    }

    public void CargarMedicamentos() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("Botiquin")
                    .whereEqualTo("IdUsuario", currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            medicationList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Medicamento medicamento = document.toObject(Medicamento.class);
                                medicationList.add(medicamento);
                                Log.d("HomeFragment", "Lista de medicamentos: " + medicationList.get(medicationList.size()-1).getNombreMedicamento().toString());

                            }

                            medicationAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("HomeFragment", "Error reciviendo la lista de medicamentos: ", task.getException());
                        }
                    });
        }
    }

    public void cargarPrueba(){

        medicationList.add(new Medicamento("2oixIiv5KNRY8acPMIaItkoBwXC2","Jamon",true, "",1));
        medicationList.add(new Medicamento("2oixIiv5KNRY8acPMIaItkoBwXC2","Jamones",true, "",1));
        medicationList.add(new Medicamento("2oixIiv5KNRY8acPMIaItkoBwXC2","Jamoncitos",true, "",1));
        medicationAdapter.notifyDataSetChanged();

    }

    public void CambiarANuevoMedicamento() {
        // Navigate to the fragment or activity to add a new medication
    }


}
