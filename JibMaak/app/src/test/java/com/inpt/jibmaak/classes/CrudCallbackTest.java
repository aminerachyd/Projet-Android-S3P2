package com.inpt.jibmaak.classes;

import androidx.lifecycle.MutableLiveData;
import androidx.test.filters.SmallTest;

import com.google.gson.Gson;
import com.inpt.jibmaak.repository.CrudCallback;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.services.RetrofitOfferService;
import com.inpt.jibmaak.services.ServerResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class CrudCallbackTest {
    @Mock
    MutableLiveData<Resource<String>> resultData;
    @Captor
    ArgumentCaptor<Resource<String>> argumentCaptor;

    @Test
    public void voidTestCorrectOperation() throws IOException {
        // On vérifie que le callback répond correctement à une requete correcte
        // Pour réaliser le test on va utiliser l'opération de suppression d'une offre
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();

        ServerResponse<String> serverResponse = new ServerResponse<>();
        serverResponse.setMessage("Offre supprimée");
        server.enqueue(new MockResponse().setBody(gson.toJson(serverResponse)).setResponseCode(200));

        // On lance le serveur
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet
        RetrofitOfferService offerService = setupRetrofit(baseUrl).create(RetrofitOfferService.class);

        // On lance le test
        Call<ServerResponse<String>> call = offerService.deleteOffer("fakeId");
        Response<ServerResponse<String>> response = call.execute();
        new CrudCallback<>(Resource.Operation.DELETE, resultData).onResponse(call,response);

        // On vérifie le résultat
        verify(resultData).setValue(argumentCaptor.capture());
        Resource<String> resource = argumentCaptor.getValue();
        assertEquals(Resource.Status.OK,resource.getStatus());
        assertEquals(Resource.Operation.DELETE,resource.getOperation());
    }

    @Test
    public void voidTestInCorrectOperation() throws IOException {
        // On vérifie que le callback répond correctement à une requete incorrecte
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();

        ServerResponse<String> serverResponse = new ServerResponse<>();
        serverResponse.setError("Non autorisé");
        server.enqueue(new MockResponse().setBody(gson.toJson(serverResponse)).setResponseCode(401));

        // On lance le serveur
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet
        RetrofitOfferService offerService = setupRetrofit(baseUrl).create(RetrofitOfferService.class);

        // On lance le test
        Call<ServerResponse<String>> call = offerService.deleteOffer("fakeId");
        Response<ServerResponse<String>> response = call.execute();
        new CrudCallback<>(Resource.Operation.DELETE, resultData).onResponse(call,response);

        // On vérifie le résultat
        verify(resultData).setValue(argumentCaptor.capture());
        Resource<String> resource = argumentCaptor.getValue();
        assertEquals(Resource.Status.UNAUTHORIZED,resource.getStatus());
        assertEquals(Resource.Operation.DELETE,resource.getOperation());
    }

    public static Retrofit setupRetrofit(HttpUrl baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
