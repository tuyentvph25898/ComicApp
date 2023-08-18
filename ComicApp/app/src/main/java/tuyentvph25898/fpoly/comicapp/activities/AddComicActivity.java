package tuyentvph25898.fpoly.comicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuyentvph25898.fpoly.comicapp.MainActivity;
import tuyentvph25898.fpoly.comicapp.R;
import tuyentvph25898.fpoly.comicapp.adapter.AnhAdapter;
import tuyentvph25898.fpoly.comicapp.apiservice.ApiService;
import tuyentvph25898.fpoly.comicapp.models.Comics;

public class AddComicActivity extends AppCompatActivity {
    private EditText etTenTruyen, etMoTaNgan, etTenTacGia, etNamXuatBan, etAnhbia, etAnhNoiDung;
    private Button btnThemAnh, btnThemTruyen;

    private ArrayList<String> imageUrlList = new ArrayList<>();
    private AnhAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView imganhBia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comic);
        etTenTruyen = findViewById(R.id.etTentruyen);
        etMoTaNgan = findViewById(R.id.etMoTaNgan);
        etTenTacGia = findViewById(R.id.etTenTacGia);
        etNamXuatBan = findViewById(R.id.etNamXuatBan);
        etAnhbia = findViewById(R.id.etAnhBia);
        etAnhNoiDung = findViewById(R.id.etAnhNoiDung);
        imganhBia = findViewById(R.id.imgAnhbia);
        btnThemAnh = findViewById(R.id.btnThemAnh);
        btnThemTruyen = findViewById(R.id.btnThemTruyen);
        adapter = new AnhAdapter(imageUrlList);
        recyclerView = findViewById(R.id.rcvanh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        btnThemAnh.setOnClickListener(view -> {
            String imageUrl = etAnhNoiDung.getText().toString().trim();

            if (!imageUrl.isEmpty()) {
                imageUrlList.add(imageUrl);
                adapter.notifyItemInserted(imageUrlList.size() - 1);
                // Xóa nội dung trong EditText để chuẩn bị cho ảnh tiếp theo
                etAnhNoiDung.setText("");
            }
            String anhBia = etAnhbia.getText().toString().trim();
            if (!anhBia.isEmpty()){
                Picasso.get().load(anhBia).into(imganhBia);
            }
        });
        btnThemTruyen.setOnClickListener(view -> {
            String anhBia = etAnhbia.getText().toString().trim();
            String tenTruyen = etTenTruyen.getText().toString();
            String moTaNgan = etMoTaNgan.getText().toString();
            String tenTacGia = etTenTacGia.getText().toString();
            Integer namXuatBan = Integer.parseInt(etNamXuatBan.getText().toString());

            Comics comic = new Comics();
            comic.setTentruyen(tenTruyen);
            comic.setMotangan(moTaNgan);
            comic.setTentacgia(tenTacGia);
            comic.setNamxuatban(namXuatBan);
            comic.setAnhbia(anhBia);
            comic.setAnhnoidung(imageUrlList);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.167:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            Call<Comics> call = apiService.addComic(comic);
            call.enqueue(new Callback<Comics>() {
                @Override
                public void onResponse(Call<Comics> call, Response<Comics> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddComicActivity.this, "Thêm truyện thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddComicActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddComicActivity.this, "Thêm truyện thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Comics> call, Throwable t) {
                    Toast.makeText(AddComicActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}