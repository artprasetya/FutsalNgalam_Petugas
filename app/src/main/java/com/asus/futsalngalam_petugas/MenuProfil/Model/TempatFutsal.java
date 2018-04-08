package com.asus.futsalngalam_petugas.MenuProfil.Model;

/**
 * Created by ASUS on 04-Jan-18.
 */

public class TempatFutsal {

    private String idPetugas, namaTempatFutsal, alamat, deskripsi, noTelepon, fotoProfil;
    public double latitude;
    public double longitude;

    public TempatFutsal() {
    }

    public TempatFutsal(String idPetugas, String namaTempatFutsal, String alamat, String deskripsi, String noTelepon, String fotoProfil, double latitude, double longitude) {
        this.idPetugas = idPetugas;
        this.namaTempatFutsal = namaTempatFutsal;
        this.alamat = alamat;
        this.deskripsi = deskripsi;
        this.noTelepon = noTelepon;
        this.fotoProfil = fotoProfil;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIdPetugas() {
        return idPetugas;
    }

    public void setIdPetugas(String idPetugas) {
        this.idPetugas = idPetugas;
    }

    public String getNamaTempatFutsal() {
        return namaTempatFutsal;
    }

    public void setNamaTempatFutsal(String namaTempatFutsal) {
        this.namaTempatFutsal = namaTempatFutsal;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getFotoProfil() {
        return fotoProfil;
    }

    public void setFotoProfil(String fotoProfil) {
        this.fotoProfil = fotoProfil;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}