package com.example.bottonnavapp.ui.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bottonnavapp.R;
import java.util.Calendar;
import java.util.Objects;

public class NuevoMedicamentoFragment extends Fragment {
    private EditText Caducidad;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nuevo_medicamento, container, false);

        // Ocultar la flecha de retroceso
        if (getActivity() != null) {
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        }

        // Configurar el botón para cancelar
        Button cancelButton = root.findViewById(R.id.buttonCancelarMed);
        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Configurar el botón elegir fecha
        Caducidad = root.findViewById(R.id.Caducidad);
        Caducidad.setOnClickListener(v -> showDatePicker());

        return root;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    calendar.set(year1, month1, dayOfMonth);
                        Caducidad.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                },
                year, month, day
        );
        //Aqui se establece que la fecha minima que pueda selecionar sea la actual
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}