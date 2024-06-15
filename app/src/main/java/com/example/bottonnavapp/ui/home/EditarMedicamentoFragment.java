package com.example.bottonnavapp.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bottonnavapp.R;

import java.util.Calendar;

public class EditarMedicamentoFragment extends Fragment {

    private EditText editNombre, editCaducidad, editCantidad;
    private Switch switchReceta;
    private Button buttonCancelarEdicion; // Declarar el botón de cancelar

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editar_medicamento, container, false);

        // Inicializar vistas
        editNombre = view.findViewById(R.id.NombreEditarMed);
        editCaducidad = view.findViewById(R.id.CaducidadEditar);
        editCantidad = view.findViewById(R.id.CantidadEditar);
        switchReceta = view.findViewById(R.id.switchRecetaEditar);
        buttonCancelarEdicion = view.findViewById(R.id.buttonCancelarEdicion);

        // Configurar el botón de cancelar
            buttonCancelarEdicion.setOnClickListener(v -> {Navigation.findNavController(v).navigateUp();});

        // Configurar el botón elegir fecha
        editCaducidad.setOnClickListener(v -> showDatePicker());

        // Obtener los datos pasados al fragmento
        if (getArguments() != null) {
            String nombre = getArguments().getString("nombre");
            boolean conReceta = getArguments().getBoolean("conReceta");
            String fechaCaducidad = getArguments().getString("fechaCaducidad");
            int cantidad = getArguments().getInt("cantidad");

            // Establecer los datos en los campos
            editNombre.setText(nombre);
            switchReceta.setChecked(conReceta);
            editCaducidad.setText(fechaCaducidad);
            editCantidad.setText(String.valueOf(cantidad));
        }

        return view;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    calendar.set(year1, month1, dayOfMonth);
                        editCaducidad.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                },
                year, month, day
        );
        //Aqui se establece que la fecha minima que pueda selecionar sea la actual
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}
