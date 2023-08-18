package tuyentvph25898.fpoly.comicapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tuyentvph25898.fpoly.comicapp.R;

public class AnhTruyenAdapter extends RecyclerView.Adapter<AnhTruyenAdapter.AnhTruyenViewHolder>{
    private ArrayList<String> listAnh;

    public AnhTruyenAdapter(ArrayList<String> listAnh) {
        this.listAnh = listAnh;
    }

    @NonNull
    @Override
    public AnhTruyenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anhtruyen, parent, false);
        return new AnhTruyenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnhTruyenViewHolder holder, int position) {
        String anhtruyenUrl = listAnh.get(position);
        Picasso.get().load(anhtruyenUrl).into(holder.anhtruyen);
    }

    @Override
    public int getItemCount() {
        return listAnh.size();
    }

    public class AnhTruyenViewHolder extends RecyclerView.ViewHolder{
        private ImageView anhtruyen;
        public AnhTruyenViewHolder(@NonNull View itemView) {
            super(itemView);
            anhtruyen = itemView.findViewById(R.id.anhtruyen);
        }
    }
}
