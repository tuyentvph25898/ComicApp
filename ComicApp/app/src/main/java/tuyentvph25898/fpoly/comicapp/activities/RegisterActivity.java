package tuyentvph25898.fpoly.comicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuyentvph25898.fpoly.comicapp.R;
import tuyentvph25898.fpoly.comicapp.apiservice.ApiService;
import tuyentvph25898.fpoly.comicapp.models.User;

public class RegisterActivity extends AppCompatActivity {

    private TextView signinText;
    private EditText et_username, et_password, et_email, et_fullname;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signinText = findViewById(R.id.signinText);
        signinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        et_fullname = findViewById(R.id.et_fullname);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String email = et_email.getText().toString();
                String fullname = et_fullname.getText().toString();

                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setEmail(email);
                newUser.setFullname(fullname);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.43.167:3000") // Replace with your API base URL
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);

                Call<User> call = apiService.addUser(newUser);
                call.enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Thêm người dùng thành công", Toast.LENGTH_SHORT).show();
                            et_username.setText("");
                            et_password.setText("");
                            et_email.setText("");
                            et_fullname.setText("");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            String errorMessage = "Lỗi khi đăng ký: " + response.code();
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
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