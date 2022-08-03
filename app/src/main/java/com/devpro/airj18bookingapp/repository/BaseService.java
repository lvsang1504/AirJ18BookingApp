package com.devpro.airj18bookingapp.repository;

import com.devpro.airj18bookingapp.BuildConfig;
import com.devpro.airj18bookingapp.models.AccessToken;
import com.devpro.airj18bookingapp.utils.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
 * Supports reusability of a retrofit instance and allows the usage of an authenticated/public retrofit instance for endpoints requiring authentication or for endpoints that are public.
 * Objective was to provide maximum reusability of retrofit while avoiding issues such as duplicate POST requests or usage of expired access tokens.
 * Reference: https://gist.github.com/dinukapj/b315e5f4438bc670d7509f7aa7aaffdd
 */

/*
Problem/Objectives:
- I needed global access to a retrofit instance without having to re-initialise an instance every time
- The code in the question was confusing the public retrofit instance with authenticated endpoints thus leading to a 401: Unauthorised error.
Solution:
- Re-usability of a retrofit instance by using a Singleton design pattern
- Ability to write an interface `APIServices` and include all endpoints in it. This allows extensibility.
- Ability to switch between a retrofit built for public endpoints and a retrofit built for authenticated endpoints.
*/
public class BaseService {
    //retrofit instance containing auth data and another instance for public APIs
    private static Retrofit retrofit_noauth, retrofit_auth;
    private static OkHttpClient client_noauth, client_auth;
    private static BaseService instance = null;

    //retrofit without auth
    public static Retrofit createRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        //append logging interceptor if it's debug build type
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(loggingInterceptor);
        }

        client_noauth = okHttpBuilder.build();

        retrofit_noauth = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client_noauth)
                .build();

        return retrofit_noauth;
    }

    //retrofit with auth
    public static Retrofit createRetrofitAuth(final AccessToken auth) {
        if (auth != null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

            //append logging interceptor if it's debug build type
            if (BuildConfig.DEBUG) {
                okHttpBuilder.addInterceptor(loggingInterceptor);
            }

            okHttpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", auth.getTokenType() + " " + auth.getAccessToken()) //change these to match your own Auth model
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            client_auth = okHttpBuilder.build();

            retrofit_auth = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client_auth)
                    .build();
        }

        return retrofit_auth;
    }

    //checks if a public retrofit already exists and re-uses it, else a new instance is created
    public static <S> S createService(Class<S> serviceClass) {
        if (retrofit_noauth == null)
            return createRetrofit().create(serviceClass);
        else
            return retrofit_noauth.create(serviceClass);
    }

    //checks if an authenticated retrofit already exists and re-uses it, else a new instance is created
    public static <S> S createService(Class<S> serviceClass, final AccessToken auth) {
        if (retrofit_auth == null)
            return createRetrofitAuth(auth).create(serviceClass);
        else
            return retrofit_auth.create(serviceClass);
    }

    //call this when the user logs out of the app or if the auth token is refreshed from backend. This resets all retrofit instances to null
    public static void clearRetrofit() {
        retrofit_auth = null;
        retrofit_noauth = null;
    }

    //in any case where the current retrofit instance is required, call this method
    public static Retrofit getRetrofit() {
        if (retrofit_noauth == null)
            return createRetrofit();
        else
            return retrofit_noauth;
    }

    public static BaseService getInstance() {
        if (instance == null) {
            instance = new BaseService();
        }
        return instance;
    }
}
