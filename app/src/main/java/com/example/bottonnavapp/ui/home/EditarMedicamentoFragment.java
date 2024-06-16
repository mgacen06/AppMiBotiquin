package com.example.bottonnavapp.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bottonnavapp.MainActivity;
import com.example.bottonnavapp.R;

import java.util.Calendar;

public class EditarMedicamentoFragment extends Fragment {

    private EditText editNombre, editCaducidad, editCantidad;
    private Switch switchReceta;
    private Button buttonCancelarEdicion, buttonGuardarEdicion,buttonBorrarMed;
    private String docId, idUsuario;

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
        buttonGuardarEdicion = view.findViewById(R.id.buttonNuevoMedEditar);
        buttonBorrarMed = view.findViewById(R.id.buttonBorrarMed);

        // Configurar el botón de cancelar
        buttonCancelarEdicion.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Configurar el campo de fecha de caducidad para mostrar un calendario
        editCaducidad.setOnClickListener(v -> showDatePicker());

        // Obtener los datos pasados al fragmento
        if (getArguments() != null) {
            // ID del documento
            docId = getArguments().getString("docId");
            idUsuario = getArguments().getString("idUsuario");
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

        // Configurar el botón de guardar
        buttonGuardarEdicion.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString();
            boolean conReceta = switchReceta.isChecked();
            String fechaCaducidad = editCaducidad.getText().toString();
            String cantidadStr = editCantidad.getText().toString();

            // Verificar que todos los campos sean obligatorios
            if (nombre.isEmpty() || fechaCaducidad.isEmpty() || cantidadStr.isEmpty()) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int cantidadDosis;
            try {
                cantidadDosis = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Cantidad debe ser un número válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamar al método EditarMedicamento en MainActivity
            ((MainActivity) requireActivity()).EditarMedicamento(docId, idUsuario, nombre, conReceta, fechaCaducidad, cantidadDosis);
        });

        // Configurar el botón de borrar
        buttonBorrarMed.setOnClickListener(v -> {
            if (docId != null) {
                // Llamar al método BorrarMedicamento en MainActivity
                ((MainActivity) requireActivity()).BorrarMedicamento(docId, v);
            }
        });

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

        // Establecer que la fecha mínima que se pueda seleccionar sea la actual
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}
