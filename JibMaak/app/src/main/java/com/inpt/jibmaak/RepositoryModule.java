package com.inpt.jibmaak;

import com.inpt.jibmaak.repository.OfferRepository;
import com.inpt.jibmaak.repository.RetrofitOfferRepository;
import com.inpt.jibmaak.repository.RetrofitUserRepository;
import com.inpt.jibmaak.repository.UserRepository;
import com.inpt.jibmaak.services.AddAuthTokenInterceptor;
import com.inpt.jibmaak.services.ManageAuthErrorInterceptor;
import com.inpt.jibmaak.services.RetrofitOfferService;
import com.inpt.jibmaak.services.RetrofitUserService;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/** Module qui définit comment fournir les dependances pour l'injection
 * Elles ont une portée de viewmodel
 * */
@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class RepositoryModule {
    public static final String BASE_URL = "http://10.0.2.2:5000"; // TODO : changer url
    // 10.0.2.2 pour une application dans l'émulateur permet d'acceder au localhost de
    // la machine

    @Provides
    public static RetrofitOfferService getRetrofitOfferService(AddAuthTokenInterceptor tokenInterceptor,
                                                               ManageAuthErrorInterceptor authErrorInterceptor) {
        Retrofit retrofit = setupRetrofit(tokenInterceptor, authErrorInterceptor);
        return retrofit.create(RetrofitOfferService.class);
    }

    @Provides
    public static RetrofitUserService getRetrofitUserService(AddAuthTokenInterceptor tokenInterceptor,
                                                             ManageAuthErrorInterceptor authErrorInterceptor) {
        Retrofit retrofit = setupRetrofit(tokenInterceptor, authErrorInterceptor);
        return retrofit.create(RetrofitUserService.class);
    }


    @Binds
    public abstract OfferRepository getOfferRepository(RetrofitOfferRepository repo);

    @Binds
    public abstract UserRepository getUserRepository(RetrofitUserRepository repo);

    public static Retrofit setupRetrofit(AddAuthTokenInterceptor tokenInterceptor,
                                         ManageAuthErrorInterceptor authErrorInterceptor){
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(authErrorInterceptor)
                .build();
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
