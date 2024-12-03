package com.example.qlnv;

public class NhanVien {
    private String maSo;
    private String hoTen;
    private String soDienThoai;

    // Constructor
    public NhanVien(String maSo, String hoTen, String soDienThoai) {
        this.maSo = maSo;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
    }

    // Getter methods
    public String getMaSo() {
        return maSo;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    // Override toString method for displaying the employee
    @Override
    public String toString() {
        return maSo + " - " + hoTen + " - " + soDienThoai;
    }
}

