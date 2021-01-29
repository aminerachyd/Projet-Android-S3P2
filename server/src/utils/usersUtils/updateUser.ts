import UserModel from "../../models/User";
import { hash } from "../helpers";

/**
 * Fonction pour mettre à jour un utilisateur
 * @param maj Objet contenant les champs à mettre à jour
 * @param id L'ID de l'utilisateur à mettre à jour
 * @param userId L'ID de l'utilisateur authentifié
 */
const updateUser = async (
  { email, nom, prenom, telephone, password },
  id,
  userId
): Promise<{
  isUpdated: boolean;
  message: string;
  data?: {};
  statusCode?: number;
}> => {
  try {
    let user = await UserModel.findById(id);

    if (!user) {
      // Utilisateur introuvable
      return {
        isUpdated: false,
        message: "Utilisateur introuvable",
        statusCode: 404,
      };
    } else {
      if (id != userId) {
        // L'utilisateur actuel ne correspond pas à l'utilisateur subissant la modification
        return {
          isUpdated: false,
          message: "Non autorisé",
          statusCode: 401,
        };
      } else {
        // Vérification des champs nuls
        const update = {
          email: email ?? user.email,
          nom: nom ?? user.nom,
          prenom: prenom ?? user.prenom,
          telephone: telephone ?? user.telephone,
          password: password ? <string>hash(password) : user.password,
        };

        await user.updateOne(update);
        return {
          isUpdated: true,
          message: "Utilisateur mis à jour",
          data: id,
        };
      }
    }
  } catch (error) {
    console.log(error);

    return {
      isUpdated: false,
      message: "Erreur du serveur",
      statusCode: 500,
    };
  }
};

export default updateUser;
