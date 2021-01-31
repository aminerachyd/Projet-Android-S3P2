import UserModel from "../../models/User";
import OfferModel from "../../models/Offer";

/**
 * Fonction pour supprimer un utilisateur de la base données
 * @param id L'ID de l'utilisateur à supprimer
 * @param userId L'ID de l'utilisateur authentifié
 */
const deleteUser = async (
  id,
  userId
): Promise<{
  isDeleted: boolean;
  message: string;
  data?: {};
  statusCode?: number;
}> => {
  try {
    let user = await UserModel.findById(id);

    if (!user) {
      // Utilisateur introuvable
      return {
        isDeleted: false,
        message: "Utilisateur introuvable",
        statusCode: 404,
      };
    } else {
      if (id != userId) {
        // L'utilisateur actuel ne correspond pas à l'utilisateur subissant la suppression
        return {
          isDeleted: false,
          message: "Non autorisé",
          statusCode: 401,
        };
      } else {
        // On supprime les offres de cet utilisateur
        for (let offreId in user.offres) {
          await OfferModel.findOneAndDelete({ id: offreId });
        }

        await user.deleteOne();

        return {
          isDeleted: true,
          message: "Utilisateur supprimé",
          data: id,
        };
      }
    }
  } catch (error) {
    console.log(error);

    return {
      isDeleted: false,
      message: "Erreur du serveur",
      statusCode: 500,
    };
  }
};

export default deleteUser;
