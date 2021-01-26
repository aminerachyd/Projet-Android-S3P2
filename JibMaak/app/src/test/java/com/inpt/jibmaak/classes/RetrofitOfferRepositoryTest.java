package com.inpt.jibmaak.classes;

import androidx.lifecycle.MutableLiveData;
import androidx.test.filters.SmallTest;

import com.google.gson.Gson;
import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.model.OfferSearchCriteria;
import com.inpt.jibmaak.model.Pagination;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.repository.RetrofitOfferRepository;
import com.inpt.jibmaak.services.RetrofitOfferService;
import com.inpt.jibmaak.services.SearchResponse;
import com.inpt.jibmaak.services.ServerResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class RetrofitOfferRepositoryTest {
    @Mock
    MutableLiveData<Resource<ArrayList<Offer>>> offerData;
    @Captor
    ArgumentCaptor<Resource<ArrayList<Offer>>> argumentCaptor;

    Offer fakeOffer;

    @Before
    public void setup(){
        fakeOffer = new Offer("fakeId",100,10,"Rabat",
                "Casablanca",new Date(1000),
                new Date(5000),new User("fakeUserId","test@test.com","nomTest",
                "prenomTest","0123456789"));
    }

    @Test
    public void testCorrectRead() throws IOException, InterruptedException {
        // On vérifie que la lecture d'une offre est correcte
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();

        // On crée une reponse (correcte) pour une offre
        ServerResponse<Offer> serverResponse = new ServerResponse<>();
        serverResponse.setMessage("Offre récupérée");
        serverResponse.setPayload(fakeOffer);
        server.enqueue(new MockResponse().setBody(gson.toJson(serverResponse)).setResponseCode(200));

        // On lance le serveur
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet
        RetrofitOfferService offerService = setupRetrofit(baseUrl).create(RetrofitOfferService.class);
        RetrofitOfferRepository repository = new RetrofitOfferRepository(offerService);
        repository.setSearchData(offerData);

        // On lance le test
        repository.getOffer(fakeOffer.getId());

        // On attend un peu pour que le callback soit executé
        Thread.sleep(300);

        // On vérifie le résultat
        verify(offerData).setValue(argumentCaptor.capture());
        Resource<ArrayList<Offer>> resource = argumentCaptor.getValue();
        assertEquals(1,resource.getResource().size());
        assertEquals(fakeOffer,resource.getResource().get(0));
        assertEquals(Resource.Status.OK,resource.getStatus());
        assertEquals(Resource.Operation.READ,resource.getOperation());
    }

    @Test
    public void testIncorrectRead() throws IOException, InterruptedException {
        // On vérifie que la réaction à une erreur (500 par exemple) est correcte
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();

        // On crée une reponse d'erreur
        ServerResponse<Offer> serverResponse = new ServerResponse<>();
        serverResponse.setError("Utilisateur ou offre non disponible");
        server.enqueue(new MockResponse().setBody(gson.toJson(serverResponse)).setResponseCode(500));

        // On lance le serveur
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet
        RetrofitOfferService offerService = setupRetrofit(baseUrl).create(RetrofitOfferService.class);
        RetrofitOfferRepository repository = new RetrofitOfferRepository(offerService);
        repository.setSearchData(offerData);

        // On lance le test
        repository.getOffer(fakeOffer.getId());

        // On attend un peu pour que le callback soit executé
        Thread.sleep(300);

        // On vérifie le résultat
        verify(offerData).setValue(argumentCaptor.capture());
        Resource<ArrayList<Offer>> resource = argumentCaptor.getValue();
        assertEquals(Resource.Status.SERVER_ERROR,resource.getStatus());
        assertEquals(Resource.Operation.READ,resource.getOperation());
    }

    @Test
    public void testCorrectSearch()  throws IOException, InterruptedException {
        // On vérifie que la réaction à une recherche reussie est correcte
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();

        // On crée une reponse (correcte) pour le résultat d'une recherche
        ServerResponse<SearchResponse> serverResponse = new ServerResponse<>();
        serverResponse.setMessage("Offres récupérées");
        ArrayList<Offer> offers = new ArrayList<>();
        offers.add(fakeOffer);
        serverResponse.setPayload(new SearchResponse(true,offers,1,20));
        server.enqueue(new MockResponse().setBody(gson.toJson(serverResponse)).setResponseCode(200));

        // On lance le serveur
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet
        RetrofitOfferService offerService = setupRetrofit(baseUrl).create(RetrofitOfferService.class);
        RetrofitOfferRepository repository = new RetrofitOfferRepository(offerService);
        repository.setSearchData(offerData);

        // On lance le test
        repository.searchOffer(new OfferSearchCriteria(),new Pagination(1,20));

        // On attend un peu pour que le callback soit executé
        Thread.sleep(300);

        // On vérifie le résultat
        verify(offerData).setValue(argumentCaptor.capture());
        Resource<ArrayList<Offer>> resource = argumentCaptor.getValue();
        assertEquals(1,resource.getResource().size());
        assertEquals(fakeOffer,resource.getResource().get(0));
        assertEquals(Resource.Status.OK,resource.getStatus());
        assertEquals(Resource.Operation.READ,resource.getOperation());
    }

    @Test
    public void testIncorrectSearch() throws IOException, InterruptedException{
    // On vérifie que la réaction à une erreur lors de la recherche est correcte
        MockWebServer server = new MockWebServer();
        Gson gson = new Gson();

        // On crée une reponse (incorrecte) pour le résultat d'une recherche
        ServerResponse<SearchResponse> serverResponse = new ServerResponse<>();
        serverResponse.setError("Erreur du serveur");
        server.enqueue(new MockResponse().setBody(gson.toJson(serverResponse)).setResponseCode(500));

        // On lance le serveur
        server.start();
        HttpUrl baseUrl = server.url("/");

        // On cree l'objet
        RetrofitOfferService offerService = setupRetrofit(baseUrl).create(RetrofitOfferService.class);
        RetrofitOfferRepository repository = new RetrofitOfferRepository(offerService);
        repository.setSearchData(offerData);

        // On lance le test
        repository.searchOffer(new OfferSearchCriteria(),new Pagination(1,20));

        // On attend un peu pour que le callback soit executé
        Thread.sleep(300);

        // On vérifie le résultat
        verify(offerData).setValue(argumentCaptor.capture());
        Resource<ArrayList<Offer>> resource = argumentCaptor.getValue();
        assertEquals(Resource.Status.SERVER_ERROR,resource.getStatus());
        assertEquals(Resource.Operation.READ,resource.getOperation());
    }

    public static Retrofit setupRetrofit(HttpUrl baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
