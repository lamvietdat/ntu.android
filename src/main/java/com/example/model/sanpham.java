package com.example.model;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class sanpham implements Serializable { // Implement Serializable
    private String ma;
    private String ten;
    private int gia;

    public sanpham(){}

    public sanpham(String ma, String ten, int gia) {
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
    }

    public String getMa() {
        return ma;
    }

    public String getTen() {
        return ten;
    }

    public int getGia() {
        return gia;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mã sp: " + ma + " Tên SP: " + ten + " Giá: " + gia;
    }
}
