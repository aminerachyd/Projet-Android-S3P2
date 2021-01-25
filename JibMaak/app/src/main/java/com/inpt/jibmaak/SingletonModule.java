package com.inpt.jibmaak;

import android.content.Context;
import android.content.SharedPreferences;

import com.inpt.jibmaak.repository.AuthManager;
import com.inpt.jibmaak.services.AddAuthTokenInterceptor;
import com.inpt.jibmaak.services.RetrofitAuthService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/** Module qui définit comment fournir les dependances pour l'injection
 * Elles ont une portée de singleton
 * */
@Module
@InstallIn(SingletonComponent.class)
public abstract class SingletonModule {
    @Singleton
    @Provides
    public static SharedPreferences getSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences("infos", Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    public static RetrofitAuthService getRetrofitAuthService(AddAuthTokenInterceptor tokenInterceptor) {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(tokenInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RepositoryModule.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitAuthService.class);
    }

    @Singleton
    @Provides
    public static AuthManager getAuthManager(RetrofitAuthService authService,
                                             SharedPreferences sharedPreferences){
        return new AuthManager(authService,sharedPreferences);
    }
}
