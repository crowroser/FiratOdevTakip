package com.gulcusoftware.fratdevtakip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DuyuruAdapter extends RecyclerView.Adapter<DuyuruAdapter.ViewHolder> {
    private List<Duyuru> duyurular;

    public DuyuruAdapter(List<Duyuru> duyurular) {
        this.duyurular = duyurular;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_duyuru, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Duyuru duyuru = duyurular.get(position);
        holder.txtBaslik.setText(duyuru.getBaslik());
        holder.txtIcerik.setText(duyuru.getIcerik());
    }

    @Override
    public int getItemCount() {
        return duyurular.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBaslik;
        TextView txtIcerik;

        ViewHolder(View itemView) {
            super(itemView);
            txtBaslik = itemView.findViewById(R.id.txtBaslik);
            txtIcerik = itemView.findViewById(R.id.txtIcerik);
        }
    }
}

