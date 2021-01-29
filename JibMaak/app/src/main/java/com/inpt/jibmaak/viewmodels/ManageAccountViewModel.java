package com.inpt.jibmaak.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.repository.UserRepository;

import java.util.HashMap;

public class ManageAccountViewModel extends ViewModel {
    protected UserRepository userRepository;
    protected MutableLiveData<Resource<String>> operationData;
    protected User userToUpdate;

    @ViewModelInject
    public ManageAccountViewModel(@Assisted SavedStateHandle savedStateHandle, UserRepository
                                  userRepository){
        this.userRepository = userRepository;
        this.operationData = new MutableLiveData<>();
        this.userRepository.setResultData(this.operationData);
    }

    public void updateUser(String id, HashMap<String, String> userToUpdate){
        userRepository.updateUser(id, userToUpdate);
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<Resource<String>> getOperationData() {
        return operationData;
    }

    public void setOperationData(MutableLiveData<Resource<String>> operationData) {
        this.operationData = operationData;
    }

    public User getUserToUpdate() {
        return userToUpdate;
    }

    public void setUserToUpdate(User userToUpdate) {
        this.userToUpdate = userToUpdate;
    }


}
