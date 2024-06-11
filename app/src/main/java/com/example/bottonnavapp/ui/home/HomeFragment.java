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

        Button addMedicationButton = root.findViewById(R.id.button2);
        addMedicationButton.setOnClickListener(v -> CambiarANuevoMedicamento());

        medicationRecyclerView = root.findViewById(R.id.medicationRecyclerView);
        medicationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        medicationList = new ArrayList<>();
        medicationAdapter = new MiBotiquinAdapter(medicationList, medicamento -> {
            // Handle click on medication item
            // Navigate to edit medication fragment or activity
        });
        medicationRecyclerView.setAdapter(medicationAdapter);

        loadMedications();

        return root;
    }

    private void loadMedications() {
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
                                Log.d("HomeFragment", "Received message: " + medicationList.size());

                            }
                            medicationAdapter.notifyDataSetChanged();
                        } else {
                            // Handle error
                        }
                    });
        }
    }

    public void CambiarANuevoMedicamento() {
        // Navigate to the fragment or activity to add a new medication
    }
}
