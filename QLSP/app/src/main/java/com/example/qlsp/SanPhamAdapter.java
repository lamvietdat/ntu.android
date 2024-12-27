package com.example.qlsp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SanPhamAdapter extends ArrayAdapter<SanPham> {
    private Context context;
    private int resource;
    private List<SanPham> sanPhamList;

    public SanPhamAdapter(Context context, int resource, List<SanPham> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.sanPhamList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        SanPham sanPham = sanPhamList.get(position);

        TextView tvTenSanPham = convertView.findViewById(R.id.tvTenSanPham);
        TextView tvSoLuong = convertView.findViewById(R.id.tvSoLuong);
        TextView tvDonGia = convertView.findViewById(R.id.tvDonGia);
        TextView tvThanhTien = convertView.findViewById(R.id.tvThanhTien);

        tvTenSanPham.setText(sanPham.getTenSanPham());
        tvSoLuong.setText("Số lượng: " + sanPham.getSoLuong());
        tvDonGia.setText("Đơn giá: " + sanPham.getDonGia());
        tvThanhTien.setText("Thành tiền: " + sanPham.getThanhTien());

        return convertView;
    }
}
