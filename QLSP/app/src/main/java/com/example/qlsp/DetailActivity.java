package com.example.qlsp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private EditText edtTenSanPham, edtSoLuong, edtDonGia;
    private Button btnLuuSanPham, btnThoat;
    private SanPham sanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);

        edtTenSanPham = findViewById(R.id.edtTenSanPham);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtDonGia = findViewById(R.id.edtDonGia);
        btnLuuSanPham = findViewById(R.id.btnLuuSanPham);
        btnThoat = findViewById(R.id.btnThoat);

        Intent intent = getIntent();
        sanPham = (SanPham) intent.getSerializableExtra("sanPham");
        if (sanPham != null) {
            edtTenSanPham.setText(sanPham.getTenSanPham());
            edtSoLuong.setText(String.valueOf(sanPham.getSoLuong()));
            edtDonGia.setText(String.valueOf(sanPham.getDonGia()));
        }

        btnLuuSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenSanPham = edtTenSanPham.getText().toString();
                int soLuong = Integer.parseInt(edtSoLuong.getText().toString());
                double donGia = Double.parseDouble(edtDonGia.getText().toString());

                if (sanPham == null) {
                    sanPham = new SanPham(0, tenSanPham, soLuong, donGia);
                } else {
                    sanPham.setTenSanPham(tenSanPham);
                    sanPham.setSoLuong(soLuong);
                    sanPham.setDonGia(donGia);
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("sanPham", sanPham);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
