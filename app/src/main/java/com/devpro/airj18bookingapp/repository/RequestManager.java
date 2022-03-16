package com.devpro.airj18bookingapp.repository;

import android.content.Context;
import android.util.Log;

import com.devpro.airj18bookingapp.listeners.CategoryResponseListener;
import com.devpro.airj18bookingapp.listeners.LoginResponseListener;
import com.devpro.airj18bookingapp.listeners.RoomDetailResponseListener;
import com.devpro.airj18bookingapp.listeners.RoomResponseListener;
import com.devpro.airj18bookingapp.models.Category;
import com.devpro.airj18bookingapp.models.Room;
import com.devpro.airj18bookingapp.models.RoomDetail;
import com.devpro.airj18bookingapp.models.UserLogin;
import com.devpro.airj18bookingapp.models.UserResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    public RequestManager(Context context) {
        this.context = context;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://airj18.skqist225.xyz")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getCategories(CategoryResponseListener listener) {
        CallCategories callCategories = retrofit.create(CallCategories.class);
        Call<List<Category>> call = callCategories.categoryResponseCall();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRoomByCategory(RoomResponseListener listener, int id) {
        CallRoomByCategory callRandomRecipes = retrofit.create(CallRoomByCategory.class);
        Call<List<Room>> call = callRandomRecipes.roomResponseCall(id);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }

                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRoomDetail(RoomDetailResponseListener listener, int id) {
        CallRoomDetail callRoomDetail = retrofit.create(CallRoomDetail.class);
        Call<RoomDetail> call = callRoomDetail.roomDetailResponseCall(id);
        call.enqueue(new Callback<RoomDetail>() {
            @Override
            public void onResponse(Call<RoomDetail> call, Response<RoomDetail> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RoomDetail> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getLogin(LoginResponseListener listener, UserLogin userLogin) {
        CallLogin callLogin = retrofit.create(CallLogin.class);
        Call<UserResponse> call = callLogin.loginUser(userLogin);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 400) {
                    if (!response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            String errorMessage = jsonObject.getString("errorMessage");
                            listener.didError(errorMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }

                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    private interface CallCategories {
        @GET("/api/categories")
        Call<List<Category>> categoryResponseCall();
    }

    private interface CallLogin {
        @POST("api/user/login")
        Call<UserResponse> loginUser(@Body UserLogin userLogin);
    }

    private interface CallRoomByCategory {
        @GET("/api/rooms")
        Call<List<Room>> roomResponseCall(@Query("categoryId") int id);
    }

    private interface CallRoomDetail {
        @GET("/api/room/{id}")
        Call<RoomDetail> roomDetailResponseCall(@Path("id") int id);
    }
}
