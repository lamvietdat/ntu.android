package com.example.qlsp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String dbName = "QLSanPham.db";  // CSDL mới cho quản lý sản phẩm
    String dbPath = "/databases/";
    SQLiteDatabase db = null;
    ArrayAdapter<SanPham> adapter; // Thay đổi thành adapter tùy chỉnh
    ListView lvSanPham;
    Button btnThem;

    SanPham sp;
    int posUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xuLyCopy(); // Sao chép cơ sở dữ liệu từ assets
        addView();   // Khởi tạo giao diện
        hienThiSanPham(); // Hiển thị danh sách sản phẩm
        addEvent();   // Thêm sự kiện
    }

    private void addView() {
        lvSanPham = findViewById(R.id.lvSanPham);
        adapter = new ArrayAdapter<SanPham>(MainActivity.this, android.R.layout.simple_list_item_1, new ArrayList<SanPham>());

        lvSanPham.setAdapter(adapter);  // Sử dụng adapter tùy chỉnh
        btnThem = findViewById(R.id.btnThem);
    }

    private void xuLyCopy() {
        try {
            File dbFile = getDatabasePath(dbName);
            if (!dbFile.exists()) {
                copyDataFromAsset();  // Sao chép cơ sở dữ liệu từ assets vào thư mục app
                Toast.makeText(MainActivity.this, "Copy thành công", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "File đã tồn tại", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Lỗi", e.toString());
        }
    }

    private void copyDataFromAsset() {
        try {
            InputStream myInput = getAssets().open(dbName);
            String outFileName = getApplicationInfo().dataDir + dbPath + dbName;
            File f = new File(getApplicationInfo().dataDir + dbPath);
            if (!f.exists()) f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception ex) {
            Log.e("Lỗi", ex.toString());
        }
    }

    private void hienThiSanPham() {
        db = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SanPham", null);

        if (cursor != null && cursor.getCount() > 0) {
            adapter.clear(); // Xóa dữ liệu cũ trong adapter

            while (cursor.moveToNext()) {
                String maSP = cursor.getString(0);
                String tenSP = cursor.getString(1);
                int soLuong = cursor.getInt(2);
                double donGia = cursor.getDouble(3);

                SanPham sp = new SanPham(maSP, tenSP, donGia, soLuong);
                adapter.add(sp); // Thêm sản phẩm vào adapter
            }

            adapter.notifyDataSetChanged();  // Cập nhật ListView
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void addEvent() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, them_sua.class);
                intent.putExtra("TRANGTHAI", "THEM");
                startActivityForResult(intent, 113);
            }
        });

        lvSanPham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                sp = adapter.getItem(i);
                posUpdate = i;
                return false;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnuSua) {
            if (sp != null) {
                Intent intent = new Intent(MainActivity.this, them_sua.class);
                intent.putExtra("TRANGTHAI", "SUA");
                intent.putExtra("SANPHAM", sp);
                startActivityForResult(intent, 113);
            }
            return true;
        }

        if (item.getItemId() == R.id.mnuXoa) {
            if (sp != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn thật sự muốn xóa sản phẩm này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int rowsDeleted = db.delete("SanPham", "MaSP=?", new String[]{sp.getMaSP()});
                        if (rowsDeleted > 0) {
                            adapter.remove(sp);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Không tìm thấy sản phẩm để xóa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Không", null);
                builder.show();
            }
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Log.e("onActivityResult", "Dữ liệu trả về null");
            return;
        }

        SanPham spNew = (SanPham) data.getSerializableExtra("SANPHAM");
        if (spNew == null) {
            Log.e("onActivityResult", "Không thể lấy đối tượng SanPham từ Intent");
            return;
        }

        // Xử lý thêm mới
        if (requestCode == 113 && resultCode == 114) {
            adapter.add(spNew);
            ContentValues values = new ContentValues();
            values.put("MaSP", spNew.getMaSP());
            values.put("TenSP", spNew.getTenSP());
            values.put("SoLuong", spNew.getSoLuong());
            values.put("DonGia", spNew.getDonGia());
            db.insert("SanPham", null, values);
            Toast.makeText(MainActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
        }

        // Xử lý cập nhật
        if (requestCode == 113 && resultCode == 115) {
            ContentValues values = new ContentValues();
            values.put("TenSP", spNew.getTenSP());
            values.put("SoLuong", spNew.getSoLuong());
            values.put("DonGia", spNew.getDonGia());
            db.update("SanPham", values, "MaSP=?", new String[]{spNew.getMaSP()});
            adapter.getItem(posUpdate).setTenSP(spNew.getTenSP());
            adapter.getItem(posUpdate).setSoLuong(spNew.getSoLuong());
            adapter.getItem(posUpdate).setDonGia(spNew.getDonGia());
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        }
    }
}