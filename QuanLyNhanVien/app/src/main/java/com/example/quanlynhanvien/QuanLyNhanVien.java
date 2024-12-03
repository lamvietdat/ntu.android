package com.example.quanlynhanvien;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnv.NhanVien;
import com.example.quanlynhanvien.R;

import java.util.ArrayList;

public class QuanLyNhanVien extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    ListView lstNhanVien;
    EditText edtMaSo, edtHoTen, edtSoDienThoai;
    Button btnThemMoi;

    // Danh sách nhân viên
    ArrayList<NhanVien> arrNhanVien = new ArrayList<>();

    // Adapter để hiển thị danh sách
    ArrayAdapter<NhanVien> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_nhan_vien);

        // Khởi tạo các thành phần giao diện
        addView();

        // Thêm các sự kiện cho giao diện
        addEvent();
    }

    private void addView() {
        // Khởi tạo các thành phần giao diện
        lstNhanVien = findViewById(R.id.lstNhanVien);
        edtMaSo = findViewById(R.id.edtMaSo);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        btnThemMoi = findViewById(R.id.btnThemMoi);

        // Tạo ArrayAdapter và liên kết với ListView
        adapter = new ArrayAdapter<>(QuanLyNhanVien.this, android.R.layout.simple_list_item_1, arrNhanVien);
        lstNhanVien.setAdapter(adapter);

        // Thêm nhân viên mặc định vào danh sách
        arrNhanVien.add(new NhanVien("1", "Nguyễn Văn A", "0123456789"));
        arrNhanVien.add(new NhanVien("2", "Trần Thị B", "0987654321"));
    }

    private void addEvent() {
        // Sự kiện khi click vào một nhân viên trong ListView
        lstNhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                NhanVien nhanvien = adapter.getItem(i);
                // Hiển thị thông tin nhân viên (Mã số, Họ tên, Số điện thoại) vào các EditText
               Toast.makeText(QuanLyNhanVien.this,nhanvien.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện khi long-click vào một nhân viên trong ListView để xóa
        lstNhanVien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                NhanVien nhanvien = adapter.getItem(i);
                // Xóa nhân viên khỏi danh sách
                adapter.remove(nhanvien);


                return true;
            }
        });

        // Sự kiện khi nhấn nút "Thêm mới"
        btnThemMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maSo = edtMaSo.getText().toString();
                String hoTen = edtHoTen.getText().toString();
                String soDienThoai = edtSoDienThoai.getText().toString();

                // Kiểm tra các trường không được để trống
                if (!maSo.isEmpty() && !hoTen.isEmpty() && !soDienThoai.isEmpty()) {
                    NhanVien newEmployee = new NhanVien(maSo, hoTen, soDienThoai);
                    arrNhanVien.add(newEmployee);
                    adapter.notifyDataSetChanged();  // Cập nhật ListView
                    Toast.makeText(QuanLyNhanVien.this, "Đã thêm nhân viên", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuanLyNhanVien.this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
