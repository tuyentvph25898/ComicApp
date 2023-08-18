package tuyentvph25898.fpoly.comicapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuyentvph25898.fpoly.comicapp.R;
import tuyentvph25898.fpoly.comicapp.apiservice.ApiService;
import tuyentvph25898.fpoly.comicapp.models.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private Context context;
    private ArrayList<Comment> arrayList;

    public CommentAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Comment> comments){
        this.arrayList = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comment comment = arrayList.get(position);
        if (comment == null)return;
        holder.tvNguoidung.setText(comment.getFullname());
        holder.tvNoidung.setText(comment.getNoidung());
        holder.tvThoiGian.setText(comment.getThoi_gian());

        holder.btnEditComment.setOnClickListener(v -> {
            if (!comment.getId_nguoidung().equals(getLoggedInUserId())) {
                Toast.makeText(context, "Bạn không được phép sửa bình luận này", Toast.LENGTH_SHORT).show();
                return;
            }
            updateComment(comment.get_id(), comment.getId_nguoidung(), holder.edtCommentUpdate.getText().toString());

            holder.tvNoidung.setText( holder.edtCommentUpdate.getText().toString());
            holder.edtCommentUpdate.setVisibility(View.GONE);
            holder.tvNoidung.setVisibility(View.VISIBLE);
            holder.btnEditComment.setVisibility(View.GONE);
            holder.btnHuy.setVisibility(View.GONE);
        });
        holder.btnHuy.setOnClickListener(view -> {
            holder.edtCommentUpdate.setVisibility(View.GONE);
            holder.tvNoidung.setVisibility(View.VISIBLE);
            holder.btnEditComment.setVisibility(View.GONE);
            holder.btnHuy.setVisibility(View.GONE);
        });

        holder.itemView.setOnLongClickListener(view -> {

                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.inflate(R.menu.menu_main);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item_edit:
                                if (!comment.getId_nguoidung().equals(getLoggedInUserId())) {
                                    Toast.makeText(context, "Bạn không được phép sửa bình luận này", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                holder.edtCommentUpdate.setText(holder.tvNoidung.getText().toString());
                                holder.edtCommentUpdate.setVisibility(View.VISIBLE);
                                holder.tvNoidung.setVisibility(View.GONE);
                                holder.btnEditComment.setVisibility(View.VISIBLE);
                                holder.btnHuy.setVisibility(View.VISIBLE);
                                return true;
                            case R.id.menu_item_delete:
                                if (!comment.getId_nguoidung().equals(getLoggedInUserId())) {
                                    Toast.makeText(context, "Bạn không được phép xóa bình luận này", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("Xác nhận xóa");
                                builder.setMessage("Bạn có chắc muốn xóa bình luận này?");
                                builder.setPositiveButton("Có", (dialogInterface, i) -> {
                                    deleteComment(comment.get_id(), comment.getId_nguoidung(), position);
                                    dialogInterface.dismiss();
                                });
                                builder.setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss());
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList!=null)
            return arrayList.size();
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNguoidung, tvNoidung, tvThoiGian;
        EditText edtCommentUpdate;
        Button btnEditComment, btnHuy;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNguoidung = itemView.findViewById(R.id.tvNguoiDung);
            tvNoidung = itemView.findViewById(R.id.tvComment);
            tvThoiGian = itemView.findViewById(R.id.tvThoigian);
            edtCommentUpdate = itemView.findViewById(R.id.edtCommentUpdate);
            btnEditComment = itemView.findViewById(R.id.btnEditComment);
            btnHuy = itemView.findViewById(R.id.btnHuy);
        }
    }
    private void deleteComment(String commentId, String userId, int position) {

        if (!userId.equals(getLoggedInUserId())) {
            Toast.makeText(context, "Bạn không được phép xóa bình luận này", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.167:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Comment> call = apiService.deleteComment(commentId);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    // Xóa bình luận khỏi danh sách và cập nhật lại RecyclerView
                    arrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, arrayList.size());
                    Toast.makeText(context, "Xóa bình luận thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xóa bình luận lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(context, "Xóa bình luận thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getLoggedInUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        return userId;
    }
    private void updateComment(String commentId, String userId, String noidung) {

        if (!userId.equals(getLoggedInUserId())) {
            Toast.makeText(context, "Bạn không được phép sửa bình luận này", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.167:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Map<String,String> noidungMap = new HashMap<>();
        noidungMap.put("noidung", noidung);
        Call<Comment> call = apiService.updateComment(commentId, noidungMap);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Sửa bình luận thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("TAG", "onResponse: " + response.errorBody());
                    Toast.makeText(context, "Sửa bình luận lỗi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(context, "Sửa bình luận thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
