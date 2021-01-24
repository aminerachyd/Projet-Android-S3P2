package com.inpt.jibmaak;

import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.filters.SmallTest;

import com.inpt.jibmaak.repository.AuthCallbackInterceptor;
import com.inpt.jibmaak.repository.AuthManager;
import com.inpt.jibmaak.services.AuthInterceptor;
import com.inpt.jibmaak.services.AuthResponse;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class AuthInterceptorTest {
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
                .addInterceptor(new AuthInterceptor(sharedPreferences))
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAuthService authService = retrofit.create(RetrofitAuthService.class);

        // On lance le test
        authService.checkLogin().enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) { }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) { }
        });
        RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);

        assertNotNull(request);
        assertEquals(request.getHeader("x-auth-token"),"fakeToken");
    }

    @Test
    public void isAuthCallbackInterceptorCorrect() throws IOException {
        // On vérifie le callback fonctionne bien
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setBody("Token invalide, veuillez vous reconnecter")
                .setResponseCode(400));
        server.start();
        HttpUrl baseUrl = server.url("/");


        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAuthService authService = retrofit.create(RetrofitAuthService.class);

        AuthManager authManager = mock(AuthManager.class);
        AuthCallbackInterceptor<AuthResponse> interceptor = new AuthCallbackInterceptor<AuthResponse>(authManager) {
            @Override
            public void onGetResponse(Call<AuthResponse> call, Response<AuthResponse> response) { }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) { }
        };
        // On lance le test
        Call<AuthResponse> call = authService.checkLogin();
        Response<AuthResponse> response = call.execute();
        interceptor.onResponse(call,response);
        verify(authManager,times(1)).unauthorizedAction();

        call = authService.checkLogin();
        response = call.execute();
        interceptor.onResponse(call,response);
        verify(authManager,times(1)).logout(true);
    }
}
