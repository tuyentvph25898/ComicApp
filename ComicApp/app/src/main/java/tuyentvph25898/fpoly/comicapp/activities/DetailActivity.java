package tuyentvph25898.fpoly.comicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuyentvph25898.fpoly.comicapp.R;
import tuyentvph25898.fpoly.comicapp.adapter.CommentAdapter;
import tuyentvph25898.fpoly.comicapp.apiservice.ApiService;
import tuyentvph25898.fpoly.comicapp.models.Comment;

public class DetailActivity extends AppCompatActivity {
    private TextView tvTenTruyen, tvTenTacGia, tvMoTaNgan, tvNamXuatBan;
    private ImageView imgAnhBia;
    private String idTruyen;
    private Retrofit retrofit;
    private ApiService apiService;

    private ArrayList<Comment> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private Button btnRead, btnSend;
    private EditText etComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        etComment = findViewById(R.id.et_comment);
        tvTenTruyen = findViewById(R.id.tvTenTruyen);
        tvMoTaNgan = findViewById(R.id.tvMoTaNgan);
        tvTenTacGia = findViewById(R.id.tvTenTacGia);
        tvNamXuatBan = findViewById(R.id.tvNamXuatBan);
        imgAnhBia = findViewById(R.id.imgAnh);
        recyclerView = findViewById(R.id.id_recycleview);
        getData();
        adapter = new CommentAdapter(getApplicationContext());
        adapter.setData(arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        btnRead = findViewById(R.id.btnRead);
        btnRead.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, ReadActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ArrayList<String> anhNoiDung = getIntent().getStringArrayListExtra("anhnoidung");
            intent.putStringArrayListExtra("anhnoidungdoc", anhNoiDung);
            startActivity(intent);
        });
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String userId = sharedPreferences.getString("userId", "");
            String noiDung = etComment.getText().toString().trim();
            if (!noiDung.isEmpty()){
                Comment newComment = new Comment();
                newComment.setId_truyen(idTruyen);
                newComment.setId_nguoidung(userId);
                newComment.setNoidung(noiDung);

                Call<Comment> call = apiService.addComment(newComment);
                call.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.isSuccessful()){
                            getData();
                            etComment.setText("");
                        }else {
                            Toast.makeText(DetailActivity.this, "Không thể thêm bình luận", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Log.e("TAG", "onFailure: ", t);
                        Toast.makeText(DetailActivity.this, "Lỗi khi thêm bình luận", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(DetailActivity.this, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getData(){
        Intent intent = getIntent();
        if (intent!=null){
            String tenTruyen = intent.getStringExtra("tentruyen");
            String moTaNgan = intent.getStringExtra("motangan");
            String tenTacGia = intent.getStringExtra("tentacgia");
            int namXuatBan = intent.getIntExtra("namxuatban", 2020);
            String anhBia = intent.getStringExtra("anhbia");
            idTruyen = intent.getStringExtra("idtruyen");

            tvTenTruyen.setText(tenTruyen);
            tvNamXuatBan.setText("Năm xuất bản: "+namXuatBan);
            tvTenTacGia.setText(tenTacGia);
            tvMoTaNgan.setText(moTaNgan);
            Picasso.get().load(anhBia).into(imgAnhBia);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.167:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        Call<List<Comment>> call = apiService.getComment(idTruyen);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                layDuLieu(response.body());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("TAG", "onFailure: ",t );
            }
        });
    }
    private void layDuLieu(List<Comment> cmt){
        arrayList.clear();
        arrayList.addAll(cmt);
        Log.e( "layDuLieu: ", String.valueOf(cmt.size()));
        adapter.notifyDataSetChanged();
    }
}