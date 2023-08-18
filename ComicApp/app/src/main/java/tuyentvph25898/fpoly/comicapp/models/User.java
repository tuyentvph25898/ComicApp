package tuyentvph25898.fpoly.comicapp.models;

public class User {
    private String _id;
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String role;
    private String anhdaidien;

    public User() {
    }

    public User(String _id, String username, String password, String email, String fullname, String role, String anhdaidien) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.role = role;
        this.anhdaidien = anhdaidien;
    }

    public String getAnhdaidien() {
        return anhdaidien;
    }

    public void setAnhdaidien(String anhdaidien) {
        this.anhdaidien = anhdaidien;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return _id;
    }

    public void setUserId(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
