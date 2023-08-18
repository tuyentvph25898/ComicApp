package tuyentvph25898.fpoly.comicapp.models;

import java.util.ArrayList;
import java.util.List;

public class Comics {
    private String _id;
    private String tentruyen;
    private String motangan;
    private String tentacgia;
    private Integer namxuatban;
    private String anhbia;
    private ArrayList<String> anhnoidung;

    public Comics() {
    }
    public Comics(String _id, String tentruyen, String motangan, String tentacgia, Integer namxuatban, String anhbia, ArrayList<String> anhnoidung) {
        this._id = _id;
        this.tentruyen = tentruyen;
        this.motangan = motangan;
        this.tentacgia = tentacgia;
        this.namxuatban = namxuatban;
        this.anhbia = anhbia;
        this.anhnoidung = anhnoidung;
    }

    public String getTruyenId() {
        return _id;
    }

    public void setTruyenId(String _id) {
        this._id = _id;
    }

    public String getTentruyen() {
        return tentruyen;
    }

    public void setTentruyen(String tentruyen) {
        this.tentruyen = tentruyen;
    }

    public String getMotangan() {
        return motangan;
    }

    public void setMotangan(String motangan) {
        this.motangan = motangan;
    }

    public String getTentacgia() {
        return tentacgia;
    }

    public void setTentacgia(String tentacgia) {
        this.tentacgia = tentacgia;
    }

    public Integer getNamxuatban() {
        return namxuatban;
    }

    public void setNamxuatban(Integer namxuatban) {
        this.namxuatban = namxuatban;
    }

    public String getAnhbia() {
        return anhbia;
    }

    public void setAnhbia(String anhbia) {
        this.anhbia = anhbia;
    }

    public ArrayList<String> getAnhnoidung() {
        return anhnoidung;
    }

    public void setAnhnoidung(ArrayList<String> anhnoidung) {
        this.anhnoidung = anhnoidung;
    }
}
