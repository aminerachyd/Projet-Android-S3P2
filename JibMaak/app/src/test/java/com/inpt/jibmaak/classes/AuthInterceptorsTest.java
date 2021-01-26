package com.inpt.jibmaak.classes;

import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.filters.SmallTest;

import com.inpt.jibmaak.repository.AuthManager;
import com.inpt.jibmaak.services.AddAuthTokenInterceptor;
import com.inpt.jibmaak.services.ManageAuthErrorInterceptor;
import com.inpt.jibmaak.services.ServerResponse;
import com.inpt.jibmaak.services.RetrofitAuthService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class AuthInterceptorsTest {
    @Mock()
    public SharedPreferences sharedPreferences;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void isTokenSent() throws IOException, InterruptedException {
        // On vérifie que l'intercepteur ajoute bien le token
        when(sharedPreferences.contains(AuthManager.ACCESS_TOKEN)).thenReturn(true);
        when(sharedPreferences.getString(AuthManager.ACCESS_TOKEN,""))
                .thenReturn("fakeToken");
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200));
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet avec l'intercepteur chargé d'ajouter le token
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new AddAuthTokenInterceptor(sharedPreferences))
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAuthService authService = retrofit.create(RetrofitAuthService.class);

        // On lance le test
        authService.checkLogin().enqueue(new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
            }
            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
            }
        });
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);

        assertNotNull(request);
        assertEquals(request.getHeader("x-auth-token"),"fakeToken");
    }

    @Test
    public void isAuthErrorInterceptorCorrect() throws IOException {
        // On vérifie que l'intercepteur detecte bien l'erreur
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setBody("{\"error\":\"Token invalide, veuillez vous reconnecter\"")
                .setResponseCode(400));
        server.start();
        HttpUrl baseUrl = server.url("/");
        AuthManager authManager = mock(AuthManager.class);

        // On cree l'objet avec l'intercepteur
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new ManageAuthErrorInterceptor(authManager))
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAuthService authService = retrofit.create(RetrofitAuthService.class);


        // On lance le test
        Call<ServerResponse<String>> call = authService.checkLogin();
        call.execute();
        verify(authManager,times(1)).unauthorizedAction();

        call = authService.checkLogin();
        call.execute();
        verify(authManager,times(1)).logout();
    }
}
