package com.inpt.jibmaak;

import android.content.Context;
import android.content.SharedPreferences;

import com.inpt.jibmaak.repository.OfferRepository;
import com.inpt.jibmaak.repository.RetrofitOfferRepository;
import com.inpt.jibmaak.repository.RetrofitUserRepository;
import com.inpt.jibmaak.repository.UserRepository;
import com.inpt.jibmaak.services.AuthInterceptor;
import com.inpt.jibmaak.services.RetrofitAuthService;
import com.inpt.jibmaak.services.RetrofitOfferService;
import com.inpt.jibmaak.services.RetrofitUserService;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Module qui définit comment fournir les dependances pour l'injection */
@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class RepositoryModule {
    public static final String BASE_URL = "http://10.0.2.2:55300"; // TODO : changer url
    // 10.0.2.2 pour une application dans l'émulateur permet d'acceder au localhost de
    // la machine

    @Provides
    public static SharedPreferences getSharedPreferences(@ApplicationContext Context context){
        return context.getSharedPreferences("infos",Context.MODE_PRIVATE);
    }
    
    @Provides
    public static RetrofitAuthService getRetrofitAuthService(AuthInterceptor interceptor){
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitAuthService.class);
    }

    @Provides
    public static RetrofitOfferService getRetrofitOfferService(AuthInterceptor interceptor){
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitOfferService.class);
    }

    @Provides
    public static RetrofitUserService getRetrofitUserService(AuthInterceptor interceptor){
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitUserService.class);
    }
    
    @Binds
    public abstract OfferRepository getOfferRepository(RetrofitOfferRepository repo);

    @Binds
    public abstract UserRepository getUserRepository(RetrofitUserRepository repo);

}
