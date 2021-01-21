package com.inpt.jibmaak.repository;

import androidx.lifecycle.LiveData;

import com.inpt.jibmaak.model.User;

/**
 * Repository en charge de la gestion utilisateur
 */
public interface UserRepository {

    /**
     * Recupere l'utilisateur correspondant à l'id
     * @param userId l'identifiant de l'utilisateur qu'on cherche
     */
    void getUser(int userId);

    /**
     * Met à jour un utilisateur. Le mot de passe, l'email et l'identifiant ne sont pas mis à jour.
     * @param userToUpdate L'utilisateur à mettre à jour
     */
    void updateUser(User userToUpdate);

    void setUserData(LiveData<Resource<User>> userData);

    LiveData<Resource<User>> getUserData();
}
