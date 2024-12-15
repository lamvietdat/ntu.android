package com.example.quanlysanpham;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.model.DanhMuc;
import com.example.model.sanpham;

public class MainActivity extends AppCompatActivity {
    Spinner spDanhmuc;
    ArrayAdapter<DanhMuc> danhMucApter;
    ArrayAdapter<sanpham> sanphamApter;
    EditText edtMaSP, edtTenSP, edtGia;
    ListView lstSP;
    Button btnThem;
    DanhMuc selectDanhMuc = null;
    GestureDetector gestureDetector;
    private ActivityResultLauncher<Intent> productDetailLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addView();
        addEvent();

        productDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        sanpham sp = (sanpham) data.getSerializableExtra("sanpham");
                        boolean delete = data.getBooleanExtra("delete", false);

                        if (delete) {
                            for (int i = 0; i < selectDanhMuc.getSanphams().size(); i++) {
                                sanpham currentSP = selectDanhMuc.getSanphams().get(i);
                                if (currentSP.getMa().equals(sp.getMa())) {
                                    selectDanhMuc.getSanphams().remove(i);
                                    break;
                                }
                            }
                        } else {
                            for (int i = 0; i < selectDanhMuc.getSanphams().size(); i++) {
                                sanpham currentSP = selectDanhMuc.getSanphams().get(i);
                                if (currentSP.getMa().equals(sp.getMa())) {
                                    selectDanhMuc.getSanphams().set(i, sp);
                                    break;
                                }
                            }
                        }

                        sanphamApter.clear();
                        sanphamApter.addAll(selectDanhMuc.getSanphams());
                        sanphamApter.notifyDataSetChanged();
                    }
                }
        );

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                int position = lstSP.pointToPosition((int) e.getX(), (int) e.getY());
                if (position != AdapterView.INVALID_POSITION) {
                    sanpham sp = sanphamApter.getItem(position);
                    if (sp != null) {
                        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                        intent.putExtra("sanpham", sp);
                        productDetailLauncher.launch(intent);
                    }
                }
                return true;
            }
        });
    }

    private void addEvent() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulynhapSP();
            }
        });

        spDanhmuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDanhMuc = danhMucApter.getItem(position);
                sanphamApter.clear();
                sanphamApter.addAll(selectDanhMuc.getSanphams());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        lstSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sanpham sp = sanphamApter.getItem(position);
                if (sp != null) {
                    edtGia.setText(String.valueOf(sp.getGia()));
                    edtMaSP.setText(sp.getMa());
                    edtTenSP.setText(sp.getTen());
                }
            }
        });

        lstSP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    private void xulynhapSP() {
        sanpham sp = new sanpham(edtMaSP.getText().toString(), edtTenSP.getText().toString(), Integer.parseInt(edtGia.getText().toString()));
        selectDanhMuc.getSanphams().add(sp);
        sanphamApter.clear();
        sanphamApter.addAll(selectDanhMuc.getSanphams());
    }

    private void addView() {
        spDanhmuc = findViewById(R.id.danhmucSP);
        danhMucApter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item);
        spDanhmuc.setAdapter(danhMucApter);
        lstSP = findViewById(R.id.lstSP);
        sanphamApter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item);
        lstSP.setAdapter(sanphamApter);

        edtMaSP = findViewById(R.id.edtMaSP);
        edtGia = findViewById(R.id.edtGia);
        edtTenSP = findViewById(R.id.edtTenSP);
        btnThem = findViewById(R.id.btnThem);
        danhMucApter.add(new DanhMuc("1", "Bia"));
        danhMucApter.add(new DanhMuc("2", "Rượu"));
        danhMucApter.add(new DanhMuc("3", "Thuốc lá"));
        danhMucApter.add(new DanhMuc("4", "Nước Ngọt"));
    }
}
