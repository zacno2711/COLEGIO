package com.example.colegio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class EstudianteAdapter extends RecyclerView.Adapter<EstudianteAdapter.estudianteViewHolder> {

    ArrayList<ClsEstudiantes> listarestudiante;

    public EstudianteAdapter(ArrayList<ClsEstudiantes> listarestudiante) {
        this.listarestudiante = listarestudiante;
    }

    @NonNull
    @Override
    public EstudianteAdapter.estudianteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estudiantesresource,null,false);
        return new EstudianteAdapter.estudianteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstudianteAdapter.estudianteViewHolder holder, int position) {
        holder.tvcarnet.setText(listarestudiante.get(position).getCarnet().toString());
        holder.tvnombre.setText(listarestudiante.get(position).getNombre().toString());
        holder.tvcarrera.setText(listarestudiante.get(position).getCarrera().toString());
        holder.tvsemestre.setText(listarestudiante.get(position).getSemestre().toString());
        if (listarestudiante.get(position).getActivo().equals("Si"))
            holder.cbactivo.setChecked(true);
        else
            holder.cbactivo.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return listarestudiante.size();
    }

    public static class estudianteViewHolder extends RecyclerView.ViewHolder {
        TextView tvcarnet, tvnombre, tvcarrera, tvsemestre;
        CheckBox cbactivo;
        public estudianteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcarnet = itemView.findViewById(R.id.tvcarnet);
            tvnombre = itemView.findViewById(R.id.tvnombre);
            tvcarrera = itemView.findViewById(R.id.tvcarrera);
            tvsemestre = itemView.findViewById(R.id.tvsemestre);
            cbactivo= itemView.findViewById(R.id.cbactivo);
        }
    }
}