package tuyentvph25898.fpoly.comicapp.models;

public class Comment {
    private String _id;
    private String id_nguoidung;
    private String tennguoidung;
    private String noidung;
    private String thoigian;
    private String id_truyen;
    public Comment() {
    }

    public Comment(String _id, String id_truyen,String id_nguoidung, String tennguoidung, String noidung, String thoigian) {
        this._id = _id;
        this.id_truyen = id_truyen;
        this.id_nguoidung = id_nguoidung;
        this.tennguoidung = tennguoidung;
        this.noidung = noidung;
        this.thoigian = thoigian;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_truyen() {
        return id_truyen;
    }

    public void setId_truyen(String id_truyen) {
        this.id_truyen = id_truyen;
    }

    public String getId_nguoidung() {
        return id_nguoidung;
    }

    public void setId_nguoidung(String id_nguoidung) {
        this.id_nguoidung = id_nguoidung;
    }

    public String getFullname() {
        return tennguoidung;
    }

    public void setFullname(String fullname) {
        this.tennguoidung = fullname;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getThoi_gian() {
        return thoigian;
    }

    public void setThoi_gian(String thoi_gian) {
        this.thoigian = thoi_gian;
    }
}
