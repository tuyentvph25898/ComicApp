package tuyentvph25898.fpoly.comicapp.apiservice;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import tuyentvph25898.fpoly.comicapp.models.Comics;
import tuyentvph25898.fpoly.comicapp.models.Comment;
import tuyentvph25898.fpoly.comicapp.models.User;

public interface ApiService {

    @POST("/api/dangki")
    Call<User> addUser(@Body User newUser);
    @FormUrlEncoded
    @POST("/api/dangnhap")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @GET("/api/truyentranh")
    Call<List<Comics>> getAllComic();

    @GET("/api/binhluan/{id_truyen}")
    Call<List<Comment>> getComment(@Path("id_truyen") String idTruyen);

    @POST("/api/truyentranh")
    Call<Comics> addComic(@Body Comics comic);
    @POST("/api/thembinhluan")
    Call<Comment> addComment(@Body Comment newComment);
    @DELETE("/api/truyentranh/{id}")
    Call<Comics> deleteComic(@Path("id") String idTruyen);
    @PUT("/api/truyentranh/{id}")
    Call<Comics> updateComic(@Path("id") String comicId, @Body Comics comic);
    @DELETE("/api/binhluan/{id}")
    Call<Comment> deleteComment(@Path("id") String commentId);

    @PUT("/api/binhluan/{id}")
    Call<Comment> updateComment(@Path("id") String commentId, @Body Map<String, String> noidung);
}
