package tuyentvph25898.fpoly.comicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

public class EditComicActivity extends AppCompatActivity {
    private EditText etTenTruyen, etMoTaNgan, etTenTacGia, etNamXuatBan, etAnhbia, etAnhNoiDung;
    private Button btnThemAnh, btnThemTruyen;

    private AnhAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView imganhBia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comic);
        etTenTruyen = findViewById(R.id.etTentruyen);
        etMoTaNgan = findViewById(R.id.etMoTaNgan);
        etTenTacGia = findViewById(R.id.etTenTacGia);
        etNamXuatBan = findViewById(R.id.etNamXuatBan);
        etAnhbia = findViewById(R.id.etAnhBia);
        etAnhNoiDung = findViewById(R.id.etAnhNoiDung);
        imganhBia = findViewById(R.id.imgAnhbia);
        btnThemAnh = findViewById(R.id.btnThemAnh);
        btnThemTruyen = findViewById(R.id.btnThemTruyen);
        Intent intent = getIntent();
        if (intent != null) {
            String idTruyen = intent.getStringExtra("idTruyen");
            String tenTruyen = intent.getStringExtra("tenTruyen");
            String tenTacGia = intent.getStringExtra("tenTacGia");
            String moTaNgan = intent.getStringExtra("moTaNgan");
            int namXuatBan = intent.getIntExtra("namXuatBan", 0);
            String anhBia = intent.getStringExtra("anhBia");
            ArrayList<String> anhNoiDung = intent.getStringArrayListExtra("anhNoiDung");

            // Hiển thị thông tin truyện trong EditText và ImageView
            etTenTruyen.setText(tenTruyen);
            etTenTacGia.setText(tenTacGia);
            etMoTaNgan.setText(moTaNgan);
            etNamXuatBan.setText(String.valueOf(namXuatBan));
            etAnhbia.setText(anhBia);
            Picasso.get().load(anhBia).into(imganhBia);


            adapter = new AnhAdapter(anhNoiDung);
            recyclerView = findViewById(R.id.rcvanh);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);

            btnThemAnh.setOnClickListener(view -> {
                String imageUrl = etAnhNoiDung.getText().toString().trim();

                if (!imageUrl.isEmpty()) {
                    anhNoiDung.add(imageUrl);
                    adapter.notifyItemInserted(anhNoiDung.size() - 1);
                    // Xóa nội dung trong EditText để chuẩn bị cho ảnh tiếp theo
                    etAnhNoiDung.setText("");
                }
                String anhBia1 = etAnhbia.getText().toString().trim();
                if (!anhBia1.isEmpty()) {
                    Picasso.get().load(anhBia1).into(imganhBia);
                }
            });
            btnThemTruyen.setOnClickListener(view -> {
                String anhBia1 = etAnhbia.getText().toString().trim();
                String tenTruyen1 = etTenTruyen.getText().toString();
                String moTaNgan1 = etMoTaNgan.getText().toString();
                String tenTacGia1 = etTenTacGia.getText().toString();
                Integer namXuatBan1 = Integer.parseInt(etNamXuatBan.getText().toString());

                Comics comic = new Comics();
                comic.setTentruyen(tenTruyen1);
                comic.setMotangan(moTaNgan1);
                comic.setTentacgia(tenTacGia1);
                comic.setNamxuatban(namXuatBan1);
                comic.setAnhbia(anhBia1);
                comic.setAnhnoidung(anhNoiDung);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.43.167:3000")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                Call<Comics> call = apiService.updateComic(idTruyen, comic);
                call.enqueue(new Callback<Comics>() {
                    @Override
                    public void onResponse(Call<Comics> call, Response<Comics> response) {
                        if (response.isSuccessful()) {
                            Comics updatedComic = response.body();
                            if (updatedComic != null) {
                                Toast.makeText(EditComicActivity.this, "Sửa truyện thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditComicActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(EditComicActivity.this, "Không thể cập nhật thông tin truyện tranh", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditComicActivity.this, "Lỗi khi cập nhật thông tin truyện tranh", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comics> call, Throwable t) {
                        Log.e("TAG", "onFailure: ", t);
                        Toast.makeText(EditComicActivity.this, "Lỗi khi cập nhật thông tin truyện tranh", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}