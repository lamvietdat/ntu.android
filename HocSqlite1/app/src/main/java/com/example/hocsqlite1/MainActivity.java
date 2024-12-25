package com.example.hocsqlite1;

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

public class MainActivity extends AppCompatActivity {
    String dbName = "Contact.db";
    String dbPath = "/databases/";
    SQLiteDatabase db = null;
    ArrayAdapter<Contact> adapter;
    ListView lvContact;
    Button btnThem;

    Contact ct;
    int posUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        xuLyCopy();
        addView();
        hienthiContact();
        addEvent();
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context, menu);
    }


    private void addView() {
        lvContact = findViewById(R.id.lvContact);
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);
        lvContact.setAdapter(adapter);
        btnThem = findViewById(R.id.btnThem);
        registerForContextMenu(lvContact);


    }

    private void xuLyCopy() {
        try {
            File dbFile = getDatabasePath(dbName);
            if (!dbFile.exists()) {
                copyDataFromAsset();
                Toast.makeText(MainActivity.this, "Copy thanh cong", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(MainActivity.this, "file đã tồn tại", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("Lỗi", e.toString());
        }
    }

    private void copyDataFromAsset() {
        try {
            InputStream myInput = getAssets().open(dbName);
            String outFileName = getApplicationInfo().dataDir + dbPath + dbName;
            File f = new File(getApplicationInfo().dataDir + dbPath);
            if (!f.exists())
                f.mkdir();
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

    private void hienthiContact() {
        db = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("Select * from Contact", null);
        while (cursor.moveToNext()) {
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            String dienthoai = cursor.getString(2);
            adapter.add(new Contact(ma, ten, dienthoai));
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
        lvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ct = adapter.getItem(i);
                posUpdate = i;
                return false;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Kiểm tra item được chọn
        if (item.getItemId() == R.id.mnuSua) {
            // Xử lý chức năng sửa
            if (ct != null) {
                Toast.makeText(MainActivity.this, "Sửa: " + ct.getMa(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, them_sua.class);
                intent.putExtra("TRANGTHAI", "SUA");
                intent.putExtra("CONTACT", ct);
                startActivityForResult(intent, 113);
            } else {
                Toast.makeText(MainActivity.this, "Không có dữ liệu để sửa", Toast.LENGTH_SHORT).show();
            }
            return true; // Dừng xử lý tại đây nếu là mnuSua
        }

        if (item.getItemId() == R.id.mnuXoa) {
            // Xử lý chức năng xóa
            if (ct != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn thật sự muốn xóa liên hệ này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            // Xóa trong cơ sở dữ liệu
                            int rowsDeleted = db.delete("Contact", "Ma=?", new String[]{String.valueOf(ct.getMa())});
                            if (rowsDeleted > 0) {
                                // Xóa khỏi adapter
                                adapter.remove(ct);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Không tìm thấy liên hệ để xóa", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("Error", "Lỗi khi xóa liên hệ: " + e.toString());
                            Toast.makeText(MainActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Không", null);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } else {
                Toast.makeText(MainActivity.this, "Không có dữ liệu để xóa", Toast.LENGTH_SHORT).show();
            }
            return true; // Dừng xử lý tại đây nếu là mnuXoa
        }

        // Xử lý các trường hợp khác (nếu có)
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Log.e("onActivityResult", "Dữ liệu trả về null");
            return; // Thoát nếu không có dữ liệu trả về
        }

        Contact ctNew = (Contact) data.getSerializableExtra("CONTACT");
        if (ctNew == null) {
            Log.e("onActivityResult", "Không thể lấy đối tượng Contact từ Intent");
            return; // Thoát nếu không lấy được đối tượng
        }

        // Xử lý thêm mới
        if (requestCode == 113 && resultCode == 114) {
            try {
                adapter.add(ctNew); // Cập nhật ListView
                ContentValues values = new ContentValues();
                values.put("Ma", ctNew.getMa());
                values.put("Ten", ctNew.getTen());
                values.put("Dienthoai", ctNew.getDienthoai());
                if (db.insert("Contact", null, values) > 0) {
                    Toast.makeText(MainActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Thêm mới thất bại", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("Error", "Lỗi khi thêm mới: " + e.toString());
            }
        }

        // Xử lý cập nhật
        if (requestCode == 113 && resultCode == 115) {
            try {
                ContentValues values = new ContentValues();
                values.put("Ten", ctNew.getTen());
                values.put("Dienthoai", ctNew.getDienthoai());
                int rowsUpdated = db.update("Contact", values, "Ma=?", new String[]{String.valueOf(ctNew.getMa())});
                if (rowsUpdated > 0) {
                    // Cập nhật giao diện
                    adapter.getItem(posUpdate).setTen(ctNew.getTen());
                    adapter.getItem(posUpdate).setDienthoai(ctNew.getDienthoai());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("Error", "Lỗi khi cập nhật: " + e.toString());
            }
        }
    }


}
