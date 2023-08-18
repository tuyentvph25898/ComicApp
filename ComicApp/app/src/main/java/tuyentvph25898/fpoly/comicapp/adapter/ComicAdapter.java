package tuyentvph25898.fpoly.comicapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuyentvph25898.fpoly.comicapp.R;
import tuyentvph25898.fpoly.comicapp.activities.DetailActivity;
import tuyentvph25898.fpoly.comicapp.activities.EditComicActivity;
import tuyentvph25898.fpoly.comicapp.apiservice.ApiService;
import tuyentvph25898.fpoly.comicapp.models.Comics;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder>{
    private Context context;
    private ArrayList<Comics> arrayList;


    public ComicAdapter(Context context) {
        this.context = context;
    }
    public void setData(ArrayList<Comics> comics){
        this.arrayList = comics;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comics comics = arrayList.get(position);
        if (comics == null)return;
        holder.tvTenTruyen.setText(comics.getTentruyen());
        holder.tvTenTacGia.setText(comics.getTentacgia());
        String imageURL = comics.getAnhbia();
        Picasso.get().load(imageURL).into(holder.imgAnhBia);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");
        if ("user".equalsIgnoreCase(role)) {
            holder.btnSua.setVisibility(View.GONE);
            holder.btnXoa.setVisibility(View.GONE);
        } else {
            holder.btnSua.setVisibility(View.VISIBLE);
            holder.btnXoa.setVisibility(View.VISIBLE);
        }
        holder.imgAnhBia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comics comics = arrayList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("idtruyen", comics.getTruyenId());
                intent.putExtra("tentruyen", comics.getTentruyen());
                intent.putExtra("tentacgia", comics.getTentacgia());
                intent.putExtra("motangan", comics.getMotangan());
                intent.putExtra("namxuatban", comics.getNamxuatban());
                intent.putExtra("anhbia", comics.getAnhbia());
                intent.putStringArrayListExtra("anhnoidung",comics.getAnhnoidung());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.btnXoa.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc muốn xóa truyện này?");
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                deleteComic(comics.getTruyenId(), position);
                dialogInterface.dismiss();
            });
            builder.setNegativeButton("Không",(dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        holder.btnSua.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditComicActivity.class);
            intent.putExtra("idTruyen", comics.getTruyenId());
            intent.putExtra("tenTruyen", comics.getTentruyen());
            intent.putExtra("tenTacGia", comics.getTentacgia());
            intent.putExtra("moTaNgan", comics.getMotangan());
            intent.putExtra("namXuatBan", comics.getNamxuatban());
            intent.putExtra("anhBia", comics.getAnhbia());
            intent.putStringArrayListExtra("anhNoiDung", comics.getAnhnoidung());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    private void deleteComic(String truyenId, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.167:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Comics> call = apiService.deleteComic(truyenId);
        call.enqueue(new Callback<Comics>() {
            @Override
            public void onResponse(Call<Comics> call, Response<Comics> response) {
                if (response.isSuccessful()){
                    arrayList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa truyện thành công", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Xóa truyện lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comics> call, Throwable t) {
                Log.e("TAG", "onFailure: ", t);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList!=null)
            return arrayList.size();
        return 0;
    }

    public class ComicViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTenTruyen, tvTenTacGia;
        private ImageView imgAnhBia;
        private Button btnSua, btnXoa;
        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyen);
            tvTenTacGia = itemView.findViewById(R.id.tvTenTacGia);
            imgAnhBia = itemView.findViewById(R.id.imgAnhBia);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }
}
