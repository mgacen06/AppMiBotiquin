package com.example.bottonnavapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bottonnavapp.Modelo.Medicamento;
import com.example.bottonnavapp.R;
import java.util.List;

public class MiBotiquinAdapter extends RecyclerView.Adapter<MiBotiquinAdapter.MedicationViewHolder> {

    private final List<Medicamento> medicationList;
    private final OnItemClickListener listener;

    // Interfaz para manejar el clic en un elemento del RecyclerView
    public interface OnItemClickListener {
        void onItemClick(Medicamento medicamento);
    }

    public MiBotiquinAdapter(List<Medicamento> medicationList, OnItemClickListener listener) {
        this.medicationList = medicationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medicamento medicamento = medicationList.get(position);
        // Vincula los datos y el listener
        holder.bind(medicamento, listener);
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        private final TextView NombreMedicamentoMostrar;
        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreMedicamentoMostrar = itemView.findViewById(R.id.NombreMedicamentoMostrar);
        }

        public void bind(final Medicamento medicamento, final OnItemClickListener listener) {
            NombreMedicamentoMostrar.setText(medicamento.getNombreMedicamento());
            // Configura el listener para el clic en el elemento
            itemView.setOnClickListener(v -> listener.onItemClick(medicamento));
        }
    }
}
