package com.example.qlsp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listViewSanPham;
    private Button btnThemSanPham;
    private SanPhamAdapter adapter;
    private List<SanPham> sanPhamList;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewSanPham = findViewById(R.id.listViewSanPham);
        btnThemSanPham = findViewById(R.id.btnThemSanPham);

        sanPhamList = new ArrayList<>();
        adapter = new SanPhamAdapter(this, R.layout.item_sanpham, sanPhamList);
        listViewSanPham.setAdapter(adapter);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Chèn dữ liệu mẫu vào database nếu chưa có
        insertSampleData();

        // Load data from database
        loadSanPhamFromDatabase();

        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        registerForContextMenu(listViewSanPham);
    }

    private void loadSanPhamFromDatabase() {
        sanPhamList.clear();
        Cursor cursor = db.query(DatabaseHelper.TABLE_SANPHAM, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String tenSanPham = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEN));
                int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SOLUONG));
                double donGia = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DONGIA));
                SanPham sanPham = new SanPham(id, tenSanPham, soLuong, donGia);
                sanPhamList.add(sanPham);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    private void insertSampleData() {
        // Chỉ chèn dữ liệu mẫu nếu bảng chưa có dữ liệu
        Cursor cursor = db.query(DatabaseHelper.TABLE_SANPHAM, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            db.insert(DatabaseHelper.TABLE_SANPHAM, null, createSampleSanPhamContentValues("Sản phẩm A", 10, 100000));
            db.insert(DatabaseHelper.TABLE_SANPHAM, null, createSampleSanPhamContentValues("Sản phẩm B", 5, 200000));
        }
        cursor.close();
    }

    private ContentValues createSampleSanPhamContentValues(String ten, int soLuong, double donGia) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TEN, ten);
        values.put(DatabaseHelper.COLUMN_SOLUONG, soLuong);
        values.put(DatabaseHelper.COLUMN_DONGIA, donGia);
        return values;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        SanPham selectedSanPham = sanPhamList.get(position);

        int itemId = item.getItemId();

        if (itemId == R.id.menuSua) {
            Intent intentSua = new Intent(MainActivity.this, DetailActivity.class);
            intentSua.putExtra("sanPham", selectedSanPham);
            startActivityForResult(intentSua, 2);
            return true;
        } else if (itemId == R.id.menuXoa) {
            db.delete(DatabaseHelper.TABLE_SANPHAM, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(selectedSanPham.getMaSanPham())});
            sanPhamList.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        } else if (itemId == R.id.menuChiTiet) {
            showSanPhamDetailDialog(selectedSanPham);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void showSanPhamDetailDialog(SanPham sanPham) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chi tiết sản phẩm");

        String message = "Mã sản phẩm: " + sanPham.getMaSanPham() + "\n" +
                "Tên sản phẩm: " + sanPham.getTenSanPham() + "\n" +
                "Số lượng: " + sanPham.getSoLuong() + "\n" +
                "Đơn giá: " + sanPham.getDonGia() + "\n" +
                "Thành tiền: " + sanPham.getThanhTien();

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @NonNull
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            SanPham sanPham = (SanPham) data.getSerializableExtra("sanPham");
            if (requestCode == 1) {
                // Thêm sản phẩm mới vào database
                db.insert(DatabaseHelper.TABLE_SANPHAM, null, createContentValues(sanPham));
            } else if (requestCode == 2) {
                // Cập nhật sản phẩm trong database
                db.update(DatabaseHelper.TABLE_SANPHAM, createContentValues(sanPham),
                        DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(sanPham.getMaSanPham())});
            }
            loadSanPhamFromDatabase();
        }
    }

    private ContentValues createContentValues(SanPham sanPham) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TEN, sanPham.getTenSanPham());
        values.put(DatabaseHelper.COLUMN_SOLUONG, sanPham.getSoLuong());
        values.put(DatabaseHelper.COLUMN_DONGIA, sanPham.getDonGia());
        return values;
    }
}