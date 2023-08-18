package tuyentvph25898.fpoly.comicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuyentvph25898.fpoly.comicapp.MainActivity;
import tuyentvph25898.fpoly.comicapp.R;
import tuyentvph25898.fpoly.comicapp.apiservice.ApiService;
import tuyentvph25898.fpoly.comicapp.models.User;

public class LoginActivity extends AppCompatActivity {

    private TextView signupText;
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupText = findViewById(R.id.signupText);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.43.167:3000")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);

                Call<User> call = apiService.login(username, password);
                call.enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user != null) {
                                userId = user.getUserId();
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", userId);
                                editor.putString("role", user.getRole());
                                editor.putString("anhdaidien", user.getAnhdaidien());
                                editor.putString("tennguoidung", user.getFullname());
                                editor.putString("username", user.getUsername());
                                editor.putString("email", user.getEmail());
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Dữ liệu người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "loi roi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("TAG", "onFailure: ", t);
                    }
                });
            }
        });
    }
}