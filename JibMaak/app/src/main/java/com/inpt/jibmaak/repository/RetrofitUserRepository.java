package com.inpt.jibmaak.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.model.User;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Implementation de UserRepository qui utilise un back-end distant comme
 * source de données et Retrofit
 */
public class RetrofitUserRepository implements UserRepository{
    public static final String BASE_URL = ""; // TODO: changer url
    protected LiveData<Resource<User>> userData;
    protected RetrofitUserService userService;

    public RetrofitUserRepository(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(RetrofitUserService.class);
        userData = new MutableLiveData<Resource<User>>();
    }

    public RetrofitUserRepository(RetrofitUserService userService,LiveData<Resource<User>> userData){
        this.userService = userService;
        this.userData = userData;
    }

    // TODO: methodes
    @Override
    public LiveData<Resource<User>> getUser(int userId) {

        return userData;
    }

    @Override
    public LiveData<Resource<User>> updateUser(User userToUpdate) {

        return userData;
    }

    public LiveData<Resource<User>> getUserData() {
        return userData;
    }

    public void setUserData(LiveData<Resource<User>> userData) {
        this.userData = userData;
    }

    public RetrofitUserService getUserService() {
        return userService;
    }

    public void setUserService(RetrofitUserService userService) {
        this.userService = userService;
    }
}
