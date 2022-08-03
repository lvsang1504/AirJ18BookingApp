package com.devpro.airj18bookingapp.repository;

import com.devpro.airj18bookingapp.models.BookingResponseDetail;
import com.devpro.airj18bookingapp.models.CategoryResponse;
import com.devpro.airj18bookingapp.models.ResetPass;
import com.devpro.airj18bookingapp.models.Response.BookingResponse;
import com.devpro.airj18bookingapp.models.Response.PasswordResponse;
import com.devpro.airj18bookingapp.models.RoomDetailResponse;
import com.devpro.airj18bookingapp.models.RoomResponse;
import com.devpro.airj18bookingapp.models.UserLogin;
import com.devpro.airj18bookingapp.models.UserRegister;
import com.devpro.airj18bookingapp.models.UserResponse;
import com.devpro.airj18bookingapp.models.WishlistListIdResponse;
import com.devpro.airj18bookingapp.models.WishlistResponse;
import com.devpro.airj18bookingapp.models.WishlistResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IService {
    //    @Headers({"Content-Type: application/json"})
//    @GET("/category/getAll")
//    Call<CategoryResponse> categoryResponseCall();
//
//    @Headers({"Content-Type: application/json"})
//    @GET("/product/getAllProductByCategory")
//    Call<ProductResponse> productResponseCall();
//
//    @Headers({"Content-Type: application/json"})
//    @GET("/product/detail/{id}")
//    Call<ProductDetailResponse> productDetailResponseCall(@Path("id") String id);
//
//    @Headers({"Content-Type: application/json"})
//    @GET("/bill/getListBill/{id}")
//    Call<List<Bill>> getAllBill(@Path("id") String id);
//
//    @POST("/api/login")
//    Call<UserResponse> loginUser(@Body UserLogin userLogin);
//
//    @POST("/api/register")
//    Call<UserResponse> registerUser(@Body UserLogin userLogin);
    @Headers({"Content-Type: application/json"})
    @GET("/api/user/booked-rooms?query=")
    Call<BookingResponseDetail> bookingResponseCall(@Header("Cookie") String cookie);

    @Headers({"Content-Type: application/json"})
    @GET("/api/categories")
    Call<CategoryResponse> categoryResponseCall();

    @Headers({"Content-Type: application/json"})
    @GET("/api/user/wishlists")
    Call<WishlistResponseData> getWishListResponseCall(@Header("Cookie") String cookie);

    @Headers({"Content-Type: application/json"})
    @GET("/api/user/wishlists/ids")
    Call<WishlistListIdResponse> getWishListIDResponseCall(@Header("Cookie") String cookie);

    @Headers({"Content-Type: application/json"})
    @PUT("/api/user/add-to-favoritelists/{id}")
    Call<WishlistResponse> addWishListResponseCall(@Path("id") String id,
                                                   @Header("Cookie") String cookie);

    @Headers({"Content-Type: application/json"})
    @PUT("/api/user/remove-from-favoritelists/{id}")
    Call<WishlistResponse> removeWishListResponseCall(@Path("id") String id,
                                                      @Header("Cookie") String cookie);

    @POST("/api/auth/login")
    Call<UserResponse> loginUser(@Body UserLogin userLogin);

    @POST("/api/auth/register")
    Call<UserResponse> registerUser(@Body UserRegister userRegister);

    @GET("/api/rooms")
    Call<RoomResponse> roomResponseCall(@Query("categoryId") int id);

    @GET("/api/rooms")
    Call<RoomResponse> roomResponseCallQuery(@Query("categoryId") int id,
                                             @Query("query") String query);

    @Headers({"Content-Type: application/json"})
    @PUT("/api/auth/reset-password")
    Call<PasswordResponse> resetPassword(@Body ResetPass resetPass);

    @GET("/api/room/{id}")
    Call<RoomDetailResponse> roomDetailResponseCall(@Path("id") int id);

    @Headers({"Content-Type: application/json"})
    @GET("api/booking/{roomid}/create")
    Call<BookingResponse> bookingResponseCall(@Path("roomid") int roomid,
                                              @Query("checkin") String checkin,
                                              @Query("checkout") String checkout,
                                              @Query("numberOfDays") int numberOfDays,
                                              @Query("clientMessage") String clientMessage,
                                              @Header("Cookie") String cookie);
}
