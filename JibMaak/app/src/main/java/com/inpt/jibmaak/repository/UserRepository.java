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
     * @return Une resource observable contenant l'utilisateur (ou null s'il n'est pas trouvé)
     */
    public LiveData<Resource<User>> getUser(int userId);

    /**
     * Met à jour un utilisateur. Le mot de passe, l'email et l'identifiant ne sont pas mis à jour.
     * @param userToUpdate L'utilisateur à mettre à jour
     * @return Une resource observable contenant l'utilisateur mis à jour
     */
    public LiveData<Resource<User>> updateUser(User userToUpdate);
}
