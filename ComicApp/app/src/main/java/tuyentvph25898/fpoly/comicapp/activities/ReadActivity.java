package tuyentvph25898.fpoly.comicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import tuyentvph25898.fpoly.comicapp.R;
import tuyentvph25898.fpoly.comicapp.adapter.AnhTruyenAdapter;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Intent intent = getIntent();
        ArrayList<String> anhNoiDungDoc = getIntent().getStringArrayListExtra("anhnoidungdoc");
        if (intent!=null){
            AnhTruyenAdapter adapter = new AnhTruyenAdapter(anhNoiDungDoc);
            RecyclerView recyclerView = findViewById(R.id.id_recycleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
        }
    }
}