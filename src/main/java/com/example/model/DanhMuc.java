package com.example.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DanhMuc {
    private String ma;
    private String ten;
    private ArrayList<sanpham> sanphams = new ArrayList<sanpham>();

    public  DanhMuc(){}
    public DanhMuc(String ma, String ten) {
        this.ma = ma;
        this.ten = ten;
    }

    public String getMa() {
        return ma;
    }

    public String getTen() {
        return ten;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public ArrayList<sanpham> getSanphams() {
        return sanphams;
    }

    public void setSanphams(ArrayList<sanpham> sanphams) {
        this.sanphams = sanphams;
    }

    @NonNull
    @Override
    public String toString() {
        return ma + " " + ten;
    }

}
