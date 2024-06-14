package com.example.bottonnavapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bottonnavapp.R;

public class NuevoMedicamentoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nuevo_medicamento, container, false);

        // Configurar el botón para cancelar
        Button cancelButton = root.findViewById(R.id.buttonCancelarMed);
        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        return root;
    }


}
