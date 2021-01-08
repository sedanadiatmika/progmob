package com.noplayer.rumahsakit;

public class Pendaftaran {
    //add string here
    private String noAntre;
    private String namaPasien;
    private String poliTujuan;
    private String tanggalPeriksa;
    private String idPendaftaran;

    private String status;

    public Pendaftaran(){}

    public String getStatusPendaftaran() {return status; }

    public void setStatusPendaftaran (String status) {this.status = status; }

    public String getTanggalPeriksa() {
        return tanggalPeriksa;
    }

    public void setTanggalPeriksa(String tanggalPeriksa) {
        this.tanggalPeriksa = tanggalPeriksa;
    }

    public String getIdPendaftaran() {
        return idPendaftaran;
    }

    public void setIdPendaftaran(String idPendaftaran) {
        this.idPendaftaran = idPendaftaran;
    }

    public Pendaftaran(String noAntre, String namaPasien, String poliTujuan, String tanggalPeriksa, String idPendaftaran){
        this.noAntre = noAntre;
        this.namaPasien = namaPasien;
        this.poliTujuan = poliTujuan;
        this.tanggalPeriksa = tanggalPeriksa;
        this.idPendaftaran = idPendaftaran;

    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getPoliTujuan() {
        return poliTujuan;
    }

    public void setPoliTujuan(String poliTujuan) {
        this.poliTujuan = poliTujuan;
    }




    public String getNoAntre() {
        return noAntre;
    }

    public void setNoAntre(String noAntre) {
        this.noAntre = noAntre;
    }




}
