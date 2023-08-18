package tuyentvph25898.fpoly.comicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuyentvph25898.fpoly.comicapp.activities.AddComicActivity;
import tuyentvph25898.fpoly.comicapp.activities.ProfileActivity;
import tuyentvph25898.fpoly.comicapp.adapter.ComicAdapter;
import tuyentvph25898.fpoly.comicapp.apiservice.ApiService;
import tuyentvph25898.fpoly.comicapp.models.Comics;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private ApiService apiService;
    private RecyclerView recyclerView;
    private ArrayList<Comics> arrayList = new ArrayList<>();
    private ComicAdapter comicAdapter;
    private FloatingActionButton button;
    private EditText etSearch;
    private ImageView userImg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.id_recycleview);
        getData();
        comicAdapter = new ComicAdapter(getApplicationContext());
        comicAdapter.setData(arrayList);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(comicAdapter);
        button = findViewById(R.id.btnAdd);
        checkUserRole();
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddComicActivity.class);
            startActivity(intent);
        });
        userImg = findViewById(R.id.userImg);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userImgUrl = sharedPreferences.getString("anhdaidien", "");
        Picasso.get().load(userImgUrl).into(userImg);
        userImg.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Gọi hàm tìm kiếm và truyền từ khóa tìm kiếm khi người dùng nhập
                searchComic(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }
    private void getData(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.167:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        Call<List<Comics>> call = apiService.getAllComic();
        call.enqueue(new Callback<List<Comics>>() {
            @Override
            public void onResponse(Call<List<Comics>> call, Response<List<Comics>> response) {
                layDuLieu(response.body());
            }

            @Override
            public void onFailure(Call<List<Comics>> call, Throwable t) {

            }
        });
    }

    private void layDuLieu(List<Comics> cm) {
        arrayList.clear();
        arrayList.addAll(cm);
        comicAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
    private void searchComic(String query) {
        ArrayList<Comics> searchResults = new ArrayList<>();

        for (Comics comic : arrayList) {
            // Kiểm tra xem tên truyện có chứa từ khóa tìm kiếm không (không phân biệt hoa thường)
            if (comic.getTentruyen().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(comic);
            }
        }

        // Cập nhật dữ liệu cho adapter với kết quả tìm kiếm
        comicAdapter.setData(searchResults);
        comicAdapter.notifyDataSetChanged();
    }
    private void checkUserRole() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");

        if ("user".equalsIgnoreCase(role)) {
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
        }
    }

}