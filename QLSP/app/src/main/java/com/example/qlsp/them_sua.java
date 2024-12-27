package com.example.qlsp;



import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class them_sua extends AppCompatActivity {
    Intent intent; // Intent nhận dữ liệu
    EditText edtMa, edtTen, edtSoLuong, edtDonGia;
    Button btnThemSua, btnThoat;
    String trangthai; // Xác định trạng thái (Thêm hoặc Sửa)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua); // Thay đổi layout nếu cần
        addView();
        addEvent();
    }

    // Hàm khởi tạo và gán sự kiện cho các thành phần
    private void addEvent() {
        btnThemSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu từ các EditText
                String maSP = edtMa.getText().toString();
                String tenSP = edtTen.getText().toString();
                String soLuongStr = edtSoLuong.getText().toString();
                String donGiaStr = edtDonGia.getText().toString();

                // Kiểm tra dữ liệu nhập vào
                if (maSP.isEmpty() || tenSP.isEmpty() || soLuongStr.isEmpty() || donGiaStr.isEmpty()) {
                    Toast.makeText(them_sua.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đổi dữ liệu nhập vào
                int soLuong = Integer.parseInt(soLuongStr);
                double donGia = Double.parseDouble(donGiaStr);

                // Tạo đối tượng SanPham mới sử dụng constructor
                SanPham sp = new SanPham(maSP, tenSP, donGia, soLuong);

                // Nếu trạng thái là thêm, gửi dữ liệu về màn hình chính
                if (trangthai.equals("THEM")) {
                    intent.putExtra("SANPHAM", sp);
                    setResult(114, intent); // Kết quả thêm sản phẩm
                } else { // Nếu trạng thái là sửa, gửi dữ liệu về màn hình chính
                    intent.putExtra("SANPHAM", sp);
                    setResult(115, intent); // Kết quả sửa sản phẩm
                }
                finish(); // Đóng màn hình hiện tại
            }
        });

        // Thoát màn hình khi bấm nút Thoát
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Đóng màn hình
            }
        });
    }



    // Hàm khởi tạo các view
    private void addView() {
        intent = getIntent();
        trangthai = intent.getStringExtra("TRANGTHAI"); // Nhận trạng thái Thêm/Sửa

        edtMa = findViewById(R.id.edtMa);
        edtTen = findViewById(R.id.edtTen);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtDonGia = findViewById(R.id.edtDonGia);

        btnThemSua = findViewById(R.id.btnThemSua);
        btnThoat = findViewById(R.id.btnThoat);

        // Nếu trạng thái là "THÊM", nút "Thêm"
        if (trangthai.equals("THEM")) {
            btnThemSua.setText("Thêm");
        } else { // Nếu trạng thái là "SỬA", nút "Sửa"
            btnThemSua.setText("Sửa");

            // Lấy thông tin sản phẩm đã chọn và hiển thị lên các ô nhập
            SanPham sp = (SanPham) intent.getSerializableExtra("SANPHAM");
            edtMa.setText(sp.getMaSP());
            edtMa.setEnabled(false); // Không cho sửa mã sản phẩm
            edtTen.setText(sp.getTenSP());
            edtSoLuong.setText(String.valueOf(sp.getSoLuong()));
            edtDonGia.setText(String.valueOf(sp.getDonGia()));
        }
    }
}
