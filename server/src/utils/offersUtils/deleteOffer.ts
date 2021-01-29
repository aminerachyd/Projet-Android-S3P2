import OfferModel from "../../models/Offer";
import { isUserValid } from "../helpers";

/**
 * Fonction pour supprimer une offre de la base de données
 * @param offerId L'ID de l'offre à supprimer
 * @param userId L'ID de l'utilisateur authentifié
 */
const deleteOffer = async (
  offerId,
  userId
): Promise<{
  isDeleted: boolean;
  message: string;
  data?: {};
  statusCode?: number;
}> => {
  try {
    let offer = await OfferModel.findById(offerId);
    let { isValid } = await isUserValid(userId);
    if (!offer || !isValid) {
      // Offre non trouvée
      return {
        isDeleted: false,
        message: "Offre ou utilisateur non disponible",
        statusCode: 400,
      };
    } else {
      if (offer.user._id != userId) {
        // L'utilisateur actuel ne correspond pas à l'utilisateur propriétaire de l'offre
        return {
          isDeleted: false,
          message: "Non autorisé",
          statusCode: 401,
        };
      } else {
        await offer.deleteOne();

        return {
          isDeleted: true,
          message: "Offre supprimée",
          data: offerId,
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

export default deleteOffer;
