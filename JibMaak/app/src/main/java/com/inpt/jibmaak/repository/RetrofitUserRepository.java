package com.inpt.jibmaak.repository;


import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.services.RetrofitUserService;

import javax.inject.Inject;

/** Implementation de UserRepository qui utilise un back-end distant comme
 * source de données et Retrofit
 */
public class RetrofitUserRepository implements UserRepository {
    protected MutableLiveData<Resource<User>> userData;
    protected RetrofitUserService userService;

    @Inject
    public RetrofitUserRepository(RetrofitUserService userService) {
        this.userService = userService;
    }

    // TODO: methodes
    @Override
    public void getUser(int userId) {
    }

    @Override
    public void updateUser(User userToUpdate) {
    }

    @Override
    public MutableLiveData<Resource<User>> getUserData() {
        return userData;
    }

    @Override
    public void setUserData(MutableLiveData<Resource<User>> userData) {
        this.userData = userData;
    }

    public RetrofitUserService getUserService() {
        return userService;
    }

    public void setUserService(RetrofitUserService userService) {
        this.userService = userService;
    }
}
