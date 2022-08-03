package com.devpro.airj18bookingapp.repository;


import android.util.Log;

import com.devpro.airj18bookingapp.listeners.BaseResponseListener;
import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.listeners.BookingResponseListener;
import com.devpro.airj18bookingapp.listeners.CategoryResponseListener;
import com.devpro.airj18bookingapp.listeners.LoginResponseListener;
import com.devpro.airj18bookingapp.listeners.PasswordResponseListener;
import com.devpro.airj18bookingapp.listeners.RegisterResponseListener;
import com.devpro.airj18bookingapp.listeners.RoomDetailResponseListener;
import com.devpro.airj18bookingapp.listeners.RoomResponseListener;
import com.devpro.airj18bookingapp.listeners.WishlistEventResponseListener;
import com.devpro.airj18bookingapp.listeners.WishlistListAllResponseListener;
import com.devpro.airj18bookingapp.listeners.WishlistListResponseListener;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceImpl {


    BaseResponseListener listener;
    BaseService baseService;

    public ServiceImpl(BaseResponseListener baseResponseListener) {
        this.listener = baseResponseListener;
    }


    public void getCategories(CategoryResponseListener listener) {
        Call<CategoryResponse> call = baseService.createService(IService.class).categoryResponseCall();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRoomByCategory(RoomResponseListener listener, int id) {
        Call<RoomResponse> call = baseService.createService(IService.class).roomResponseCall(id);
        call.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }

                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRoomByQuery(RoomResponseListener listener, int id, String search) {
        Call<RoomResponse> call = baseService.createService(IService.class).roomResponseCallQuery(id, search);
        call.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }

                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRoomDetail(RoomDetailResponseListener listener, int id) {
        Call<RoomDetailResponse> call = baseService.createService(IService.class).roomDetailResponseCall(id);
        call.enqueue(new Callback<RoomDetailResponse>() {
            @Override
            public void onResponse(Call<RoomDetailResponse> call, Response<RoomDetailResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RoomDetailResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getBooking(BookingResponseListener listener, int roomid, String checkin, String checkout, String clientMessage, int numberOfDays, String cookie) {
        Call<BookingResponse> call = baseService.createService(IService.class).bookingResponseCall(roomid, checkin, checkout, numberOfDays, clientMessage, cookie);
        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getLogin(LoginResponseListener listener, UserLogin userLogin) {
        Call<UserResponse> call = baseService.createService(IService.class).loginUser(userLogin);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 400) {
                    if (!response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            JSONObject errorMessage = jsonObject.getJSONObject("error");
                            listener.didError(errorMessage.getString("message"));
                        } catch (JSONException e) {
                            listener.didError(e.toString());
                            e.printStackTrace();
                        } catch (IOException e) {
                            listener.didError(e.toString());
                            e.printStackTrace();
                        }
                    }
                    return;
                }
                List<String> Cookielist = response.headers().values("Set-Cookie");
                String jsessionid = (Cookielist.get(0).split(";"))[0];


                Log.d("Header", jsessionid);
                listener.didFetch(response.body(), response.message(), jsessionid);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRegister(RegisterResponseListener listener, UserRegister userRegister) {
        Call<UserResponse> call = baseService.createService(IService.class).registerUser(userRegister);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 400) {
                    if (!response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            listener.didError(jsonObject.getString("error"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }

                // System.out.println(response.body().toString());
                listener.didFetch(response.body(), response.message());

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                listener.didError("Error0: " + t.getMessage());
            }
        });
    }

    public void getWishlists(WishlistListResponseListener listener, String cookie) {
        Call<WishlistListIdResponse> call = baseService.createService(IService.class).getWishListIDResponseCall(cookie);
        call.enqueue(new Callback<WishlistListIdResponse>() {
            @Override
            public void onResponse(Call<WishlistListIdResponse> call, Response<WishlistListIdResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("XXXX1", response.errorBody() + "hfdh");
                    listener.didError(response.message());
                    return;
                }
                Log.d("XXXX", response.body().success + "");
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<WishlistListIdResponse> call, Throwable t) {
                Log.d("XXXX2", t.getMessage() + "");
                listener.didError(t.getMessage());
            }
        });
    }

    public void getAddWishlist(WishlistEventResponseListener listener, String id, String cookie) {
        Call<WishlistResponse> call = baseService.createService(IService.class).addWishListResponseCall(id, cookie);
        call.enqueue(new Callback<WishlistResponse>() {
            @Override
            public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<WishlistResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRemoveWishlist(WishlistEventResponseListener listener, String id, String cookie) {
        Call<WishlistResponse> call = baseService.createService(IService.class).removeWishListResponseCall(id, cookie);
        call.enqueue(new Callback<WishlistResponse>() {
            @Override
            public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<WishlistResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getAllWishlists(WishlistListAllResponseListener listener, String cookie) {
        Call<WishlistResponseData> call = baseService.createService(IService.class).getWishListResponseCall(cookie);
        call.enqueue(new Callback<WishlistResponseData>() {
            @Override
            public void onResponse(Call<WishlistResponseData> call, Response<WishlistResponseData> response) {
                if (!response.isSuccessful()) {
                    Log.d("XXXX1", response.errorBody() + "hfdh");
                    listener.didError(response.message());
                    return;
                }
                Log.d("XXXX", response.body().success + "");
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<WishlistResponseData> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getBookingResponseDetail(BookingResponseDetailListener listener, String cookie) {
        Call<BookingResponseDetail> call = baseService.createService(IService.class).bookingResponseCall(cookie);
        call.enqueue(new Callback<BookingResponseDetail>() {
            @Override
            public void onResponse(Call<BookingResponseDetail> call, Response<BookingResponseDetail> response) {
                if (!response.isSuccessful()) {
                    Log.d("XXXX1", response.errorBody() + "hfdh");
                    listener.didError(response.message());
                    return;
                }
                Log.d("XXXX", response.body().success + "");
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<BookingResponseDetail> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void resetPassword(PasswordResponseListener listener, String email, String password) {

        Call<PasswordResponse> call = baseService.createService(IService.class).resetPassword(new ResetPass(email, password, password));
        call.enqueue(new Callback<PasswordResponse>() {
            @Override
            public void onResponse(Call<PasswordResponse> call, Response<PasswordResponse> response) {

                if (response.code() == 400) {
                    if (!response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            //JSONObject errorMessage = jsonObject.getJSONObject("error");
                            listener.didError(jsonObject.getString("error"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }

//                System.out.println(response.body().toString());
//                listener.didFetch(response.body(), response.message());
//
//
//                if (!response.isSuccessful()) {
//                    listener.didError(response.message());
//                    return;
//                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<PasswordResponse> call, Throwable t) {
                Log.d("FFFF", "Fail");
                listener.didError(t.getMessage());
            }
        });
    }

//    public void getCategory() {
////        Call<CategoryResponse> call = baseService.getIService().categoryResponseCall();
//        Call<CategoryResponse> call = baseService.createService(IService.class).categoryResponseCall();
//        call.enqueue(new Callback<CategoryResponse>() {
//            @Override
//            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
//                if (!response.isSuccessful() || response.code() != 200) {
//                    listener.didError(response.code(),response.message());
//                    return;
//                }
//                listener.didFetch(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<CategoryResponse> call, Throwable t) {
//                listener.didError(-1,t.getMessage());
//            }
//        });
//    }
//
//    public void getAllProduct() {
//        Call<ProductResponse> call = baseService.createService(IService.class).productResponseCall();
//        call.enqueue(new Callback<ProductResponse>() {
//            @Override
//            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
//                if (!response.isSuccessful() || response.code() != 200) {
//                    listener.didError(response.code(),response.message());
//                    return;
//                }
//                listener.didFetch(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<ProductResponse> call, Throwable t) {
//                listener.didError(-1,t.getMessage());
//            }
//        });
//    }
//
//    public void getProductDetail(String id) {
//        Call<ProductDetailResponse> call = baseService.createService(IService.class).productDetailResponseCall(id);
//        call.enqueue(new Callback<ProductDetailResponse>() {
//            @Override
//            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
//                if (!response.isSuccessful() || response.code() != 200) {
//                    listener.didError(response.code(),response.message());
//                    return;
//                }
//                listener.didFetch(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
//                listener.didError(-1,t.getMessage());
//            }
//        });
//    }
//
//    public void getAllBill(String id) {
//        Call<List<Bill>> call = baseService.createService(IService.class).getAllBill(id);
//        call.enqueue(new Callback<List<Bill>>() {
//            @Override
//            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
//                if (!response.isSuccessful() || response.code() != 200) {
//                    listener.didError(response.code(),response.message());
//                    return;
//                }
//                listener.didFetch(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<Bill>> call, Throwable t) {
//                listener.didError(-1,t.getMessage());
//            }
//        });
//    }
//
//
//    public void login(UserLogin userLogin) {
//        Call<UserResponse> callLogin = baseService.createService(IService.class, new AccessToken()).loginUser(userLogin);
//        callLogin.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                if (response.code() == 400) {
//                    if (!response.isSuccessful()) {
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(response.errorBody().string());
//                            listener.didError(response.code(),jsonObject.getString("error"));
//                        } catch (JSONException e) {
//                            listener.didError(response.code(),e.toString());
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            listener.didError(response.code(),e.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                    return;
//                }
//                if (response.code() == 200) {
//                    listener.didFetch(response.body());
//                }
//                Log.d("ERRRRROR", response.code() + "");
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                listener.didError(100,t.getMessage());
//            }
//        });
//    }
//
//    public void register(UserLogin userLogin) {
//        Call<UserResponse> callLogin = baseService.createService(IService.class, new AccessToken()).registerUser(userLogin);
//        callLogin.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                if (response.code() == 400) {
//                    if (!response.isSuccessful()) {
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(response.errorBody().string());
//                            listener.didError(response.code(),jsonObject.getString("error"));
//                        } catch (JSONException e) {
//                            listener.didError(response.code(),e.toString());
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            listener.didError(response.code(),e.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                    return;
//                }
//                if (response.code() == 200) {
//                    listener.didFetch(response.body());
//                }
//                Log.d("ERRRRROR", response.code() + "");
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                listener.didError(100,t.getMessage());
//            }
//        });
//    }
}
