package com.example.qlsp;

import java.io.Serializable;

public class SanPham implements Serializable{
    private int maSanPham;
    private String tenSanPham;
    private int soLuong;
    private double donGia;

    // Constructor, getter và setter methods

    public SanPham(int maSanPham, String tenSanPham, int soLuong, double donGia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        double thanhTien = soLuong * donGia;
        if (soLuong > 10) {
            thanhTien *= 0.9; // giảm 10%
        }
        return thanhTien;
    }
}
