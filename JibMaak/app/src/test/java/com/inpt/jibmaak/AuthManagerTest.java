package com.inpt.jibmaak;

import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.filters.SmallTest;

import com.google.gson.Gson;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.AuthManager;
import com.inpt.jibmaak.services.AuthResponse;
import com.inpt.jibmaak.services.RetrofitAuthService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class AuthManagerTest {
    @Mock
    public SharedPreferences sharedPreferences;
    public User fakeUser;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup(){
        fakeUser = new User();
        fakeUser.setEmail("test@hotmail.com");
        fakeUser.setId(7);
        fakeUser.setNom("TestNom");
        fakeUser.setPrenom("TestPrenom");
        fakeUser.setNom("TestNom");
        fakeUser.setTelephone("048452367");

        when(sharedPreferences.contains(AuthManager.ACCESS_TOKEN)).thenReturn(true);
        when(sharedPreferences.getString(AuthManager.ACCESS_TOKEN,null))
                .thenReturn("fakeToken");
        when(sharedPreferences.edit()).thenReturn(mock(SharedPreferences.Editor.class));
    }

    @Test
    public void testCorrectLogin() throws IOException, InterruptedException {
        // On vérifie que la classe répond correctement lors de l'authentification
        // MockWebServer va jouer le role du serveur
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();

        // On crée une reponse (correcte) pour le login
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("utilisateur authentifié");
        authResponse.setPayload("fakeToken");
        server.enqueue(new MockResponse().setBody(gson.toJson(authResponse)).setResponseCode(200));

        authResponse.setPayload(gson.toJson(fakeUser));
        server.enqueue(new MockResponse().setBody(gson.toJson(authResponse)).setResponseCode(200));

        // On lance le serveur
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAuthService authService = retrofit.create(RetrofitAuthService.class);
        AuthManager authManager = new AuthManager(authService,sharedPreferences);

        // On lance le test
        authManager.login("test@hotmail.com","Test");

        // On attend un peu pour que le callback soit executé
        Thread.sleep(300);
        assertNotNull(authManager.getTokens());
        assertEquals(fakeUser,authManager.getUser());
    }

    @Test
    public void testErrorLogin() throws IOException, InterruptedException {
        // On teste le cas où les identifiants sont incorrects
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();
        AuthResponse authResponse = new AuthResponse();
        authResponse.setError("Email ou mot de passe incorrect");
        server.enqueue(new MockResponse().setBody(gson.toJson(authResponse)).setResponseCode(400));

        server.start();
        HttpUrl baseUrl = server.url("/");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAuthService authService = retrofit.create(RetrofitAuthService.class);
        AuthManager authManager = new AuthManager(authService,sharedPreferences);

        authManager.login("test@hotmail.com","Test");

        Thread.sleep(300);

        assertEquals(1,server.getRequestCount());
        assertEquals(AuthManager.AuthAction.LOGIN_INCORRECT,authManager.getAuthAction().getValue());
        assertNull(authManager.getUser());
    }
}
