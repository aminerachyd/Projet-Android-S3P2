package com.inpt.jibmaak;

import androidx.lifecycle.LiveData;
import androidx.test.filters.SmallTest;

import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.repository.RetrofitUserRepository;
import com.inpt.jibmaak.repository.RetrofitUserService;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class RetrofitUserRepositoryTest {
    RetrofitUserRepository repo;
    @Mock
    RetrofitUserService userService;
    @Mock
    LiveData<Resource<User>> userData;

    @Before
    public void setup(){

    }
    // TODO : ecrire les tests
}
