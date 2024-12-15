package com.example.quanlysanpham;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.model.sanpham;

public class ProductDetailActivity extends AppCompatActivity {
    EditText edtMaSP, edtTenSP, edtGia;
    Button btnSua, btnXoa, btnTroVe;
    sanpham sp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        edtMaSP = findViewById(R.id.edtMaSP);
        edtTenSP = findViewById(R.id.edtTenSP);
        edtGia = findViewById(R.id.edtGia);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnTroVe = findViewById(R.id.btnTroVe);

        Intent intent = getIntent();
        sp = (sanpham) intent.getSerializableExtra("sanpham");

        if (sp != null) {
            edtMaSP.setText(sp.getMa());
            edtTenSP.setText(sp.getTen());
            edtGia.setText(String.valueOf(sp.getGia()));
        }

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setMa(edtMaSP.getText().toString());
                sp.setTen(edtTenSP.getText().toString());
                sp.setGia(Integer.parseInt(edtGia.getText().toString()));
                Intent resultIntent = new Intent();
                resultIntent.putExtra("sanpham", sp);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("delete", true);
                resultIntent.putExtra("sanpham", sp);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
