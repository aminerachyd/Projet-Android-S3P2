package com.inpt.jibmaak.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.services.RetrofitUserService;

import java.util.HashMap;

import javax.inject.Inject;

/** Implementation de UserRepository qui utilise un back-end distant comme
 * source de données et Retrofit
 */
public class RetrofitUserRepository implements UserRepository {
    protected MutableLiveData<Resource<User>> userData;
    protected MutableLiveData<Resource<String>> operationData;
    protected RetrofitUserService userService;

    @Inject
    public RetrofitUserRepository(RetrofitUserService userService) {
        this.userService = userService;
    }

    @Override
    public void getUser(String userId) {
        userService.getUser(userId).enqueue(new CrudCallback<>(Resource.Operation.READ,userData));
    }

    @Override
    public void updateUser(String id,HashMap<String,String> userToUpdate) {
        userService.updateUser(id,userToUpdate)
                .enqueue(new CrudCallback<>(Resource.Operation.UPDATE,operationData));
    }

    @Override
    public void deleteUser(String userId) {
        userService.deleteUser(userId)
                .enqueue(new CrudCallback<>(Resource.Operation.DELETE,operationData));
    }

    @Override
    public LiveData<Resource<User>> getUserData() {
        return userData;
    }

    @Override
    public void setUserData(MutableLiveData<Resource<User>> userData) {
        this.userData = userData;
    }

    @Override
    public void setOperationData(MutableLiveData<Resource<String>> operationData) {
        this.operationData = operationData;
    }

    @Override
    public LiveData<Resource<String>> getOperationData() {
        return operationData;
    }

    public RetrofitUserService getUserService() {
        return userService;
    }

    public void setUserService(RetrofitUserService userService) {
        this.userService = userService;
    }
}
