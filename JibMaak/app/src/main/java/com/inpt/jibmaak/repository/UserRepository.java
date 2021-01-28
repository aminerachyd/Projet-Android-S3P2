package com.inpt.jibmaak.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.model.User;

import java.util.HashMap;

/**
 * Repository en charge de la gestion utilisateur
 */
public interface UserRepository {

    /**
     * Recupere l'utilisateur correspondant à l'id
     * @param userId l'identifiant de l'utilisateur qu'on cherche
     */
    void getUser(String userId);

    /**
     * Met à jour un utilisateur. L'identifiant n'est pas mis à jour.
     * @param userToUpdate L'utilisateur à mettre à jour
     * @param id L'identifiant de l'utilisateur
     */
    void updateUser(String id,HashMap<String,String> userToUpdate);

    /**
     * Supprime l'utilisateur connecté
     * @param userId l'identifiant de l'utilisateur
     */
    void deleteUser(String userId);

    void setUserData(MutableLiveData<Resource<User>> userData);

    LiveData<Resource<User>> getUserData();

    void setOperationData(MutableLiveData<Resource<String>> operationData);

    LiveData<Resource<String>> getOperationData();
}
