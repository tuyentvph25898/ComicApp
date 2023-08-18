package tuyentvph25898.fpoly.comicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tuyentvph25898.fpoly.comicapp.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvTen, tvRole, tvUsername, tvEmail;
    private Button btnDangXuat;
    private ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvTen = findViewById(R.id.tennguoidung);
        tvEmail= findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);
        tvUsername = findViewById(R.id.tvUsername);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        imgProfile = findViewById(R.id.imgProfile);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String imgProfile1 = sharedPreferences.getString("anhdaidien", "");
        String tennguoidung = sharedPreferences.getString("tennguoidung", "");
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String role = sharedPreferences.getString("role", "");

        tvTen.setText(tennguoidung);
        tvEmail.setText(email);
        tvRole.setText(role);
        tvUsername.setText(username);
        Picasso.get().load(imgProfile1).into(imgProfile);
        btnDangXuat.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Xóa các trường dữ liệu cần thiết
            editor.remove("userId");
            editor.remove("role");
            editor.remove("anhdaidien");
            editor.remove("tennguoidung");
            editor.remove("email");

            // Áp dụng các thay đổi
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}