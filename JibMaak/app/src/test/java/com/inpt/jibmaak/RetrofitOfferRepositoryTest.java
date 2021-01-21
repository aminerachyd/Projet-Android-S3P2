package com.inpt.jibmaak;

import androidx.lifecycle.LiveData;
import androidx.test.filters.SmallTest;

import com.inpt.jibmaak.model.Offer;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.repository.RetrofitOfferRepository;
import com.inpt.jibmaak.repository.RetrofitOfferService;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class RetrofitOfferRepositoryTest {
    RetrofitOfferRepository repo;
    @Mock
    RetrofitOfferService offerService;
    @Mock
    LiveData<Resource<Offer>> offerData;

    @Before
    public void setup(){

    }
    // TODO : ecrire les tests
}
