package com.inpt.jibmaak.classes;

import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.filters.SmallTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.AuthAction;
import com.inpt.jibmaak.repository.AuthManager;
import com.inpt.jibmaak.services.RetrofitAuthService;
import com.inpt.jibmaak.services.ServerResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
        fakeUser.setId("erzer7");
        fakeUser.setNom("TestNom");
        fakeUser.setPrenom("TestPrenom");
        fakeUser.setNom("TestNom");
        fakeUser.setTelephone("048452367");

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
        ServerResponse<JsonObject> authResponse = new ServerResponse<>();
        authResponse.setMessage("utilisateur authentifié");
        JsonObject payload = gson.toJsonTree(fakeUser).getAsJsonObject();
        payload.addProperty("token","fakeToken");
        authResponse.setPayload(payload);
        server.enqueue(new MockResponse().setBody(gson.toJson(authResponse)).setResponseCode(200));

        // On lance le serveur
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet
        RetrofitAuthService authService = setupRetrofit(baseUrl).create(RetrofitAuthService.class);
        AuthManager authManager = new AuthManager(authService,sharedPreferences);

        // On lance le test
        authManager.login("test@hotmail.com","Test");

        // On attend un peu pour que le callback soit executé
        Thread.sleep(300);
        assertNotNull(authManager.getTokens());
        assertEquals(fakeUser,authManager.getUserData().getValue());
    }

    @Test
    public void testErrorLogin() throws IOException, InterruptedException {
        // On teste le cas où les identifiants sont incorrects
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();
        ServerResponse<JsonObject> authResponse = new ServerResponse<>();
        authResponse.setError("Email ou mot de passe incorrect");
        server.enqueue(new MockResponse().setBody(gson.toJson(authResponse)).setResponseCode(400));

        server.start();
        HttpUrl baseUrl = server.url("/");
        RetrofitAuthService authService = setupRetrofit(baseUrl).create(RetrofitAuthService.class);
        AuthManager authManager = new AuthManager(authService,sharedPreferences);

        authManager.login("test@hotmail.com","Test");

        Thread.sleep(300);

        assertEquals(1,server.getRequestCount());
        assertEquals(AuthAction.Action.LOGIN_INCORRECT,authManager.getAuthActionData().getValue().getAction());
        assertNull(authManager.getUserData().getValue());
    }

    @Test
    public void testCorrectRegister() throws IOException, InterruptedException {
        // On vérifie la réponse à un enregistrement correct
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();
        ServerResponse<String> registerResponse = new ServerResponse<>();
        registerResponse.setMessage("Utilisateur ajouté");
        registerResponse.setPayload("fakeId");
        server.enqueue(new MockResponse().setBody(gson.toJson(registerResponse)).setResponseCode(200));

        server.start();
        HttpUrl baseUrl = server.url("/");
        RetrofitAuthService authService = setupRetrofit(baseUrl).create(RetrofitAuthService.class);
        AuthManager authManager = new AuthManager(authService,sharedPreferences);

        // Pas besoin de faire une vraie requete : le serveur ne verra pas le contenu
        HashMap<String,String> userToRegister = new HashMap<>();
        userToRegister.put("nom","Bonjour");
        authManager.register(userToRegister);

        Thread.sleep(300);

        assertEquals(1,server.getRequestCount());
        assertEquals(AuthAction.Action.REGISTER,authManager.getAuthActionData().getValue().getAction());
    }

    @Test
    public void testIncorrectRegister() throws IOException, InterruptedException {
        // On vérifie la réponse à un enregistrement incorrect
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();
        ServerResponse<String> registerResponse = new ServerResponse<>();
        registerResponse.setError("Erreur");
        server.enqueue(new MockResponse().setBody(gson.toJson(registerResponse)).setResponseCode(500));

        server.start();
        HttpUrl baseUrl = server.url("/");
        RetrofitAuthService authService = setupRetrofit(baseUrl).create(RetrofitAuthService.class);
        AuthManager authManager = new AuthManager(authService,sharedPreferences);

        // Pas besoin de faire une vraie requete : le serveur ne verra pas le contenu
        HashMap<String,String> userToRegister = new HashMap<>();
        userToRegister.put("nom","Bonjour");
        authManager.register(userToRegister);

        Thread.sleep(300);

        assertEquals(1,server.getRequestCount());
        assertEquals(AuthAction.Action.ERROR,authManager.getAuthActionData().getValue().getAction());
    }

    public static Retrofit setupRetrofit(HttpUrl url){
        return new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
