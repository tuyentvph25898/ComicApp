package tuyentvph25898.fpoly.comicapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tuyentvph25898.fpoly.comicapp.R;

public class AnhAdapter extends RecyclerView.Adapter<AnhAdapter.AnhViewHolder>{
    private List<String> listAnh;

    public AnhAdapter(List<String> listAnh) {
        this.listAnh = listAnh;
    }

    @NonNull
    @Override
    public AnhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anh, parent, false);
        return new AnhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnhViewHolder holder, int position) {
        String anhUrl = listAnh.get(position);
        Picasso.get().load(anhUrl).into(holder.img);
        holder.btnxoa.setOnClickListener(view -> {
            listAnh.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listAnh.size();
    }

    public class AnhViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        Button btnxoa;
        public AnhViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            btnxoa = itemView.findViewById(R.id.xoaanh);
        }
    }
}
