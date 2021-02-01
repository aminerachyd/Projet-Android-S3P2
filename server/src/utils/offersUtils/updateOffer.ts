import OfferModel from "../../models/Offer";
import { isUserValid, validerDates, validerVille } from "../helpers";

/**
 * Fonction pour mettre à jour une offre
 * @param maj Objet contenant les champs à mettre à jour
 * @param offerId L'ID de l'offre à mettre à jour
 * @param userId L'ID de l'utilisateur authentifié, doit être propriétaire de l'offre
 */
const updateOffer = async (
  { lieuDepart, lieuArrivee, dateDepart, dateArrivee, prixKg, poidsDispo },
  offerId,
  userId
): Promise<{
  isUpdated: Boolean;
  message: String;
  statusCode?: number;
  data?: {};
}> => {
  try {
    let offer = await OfferModel.findById(offerId);
    const { isValid } = await isUserValid(userId);
    if (!offer || !isValid) {
      // Offre non trouvée
      return {
        isUpdated: false,
        message: "Utilisateur ou offre non disponible",
        statusCode: 400,
      };
    } else {
      if (offer.user._id != userId) {
        // L'utilisateur actuel ne correspond pas à l'utilisateur propriétaire de l'offre
        return {
          isUpdated: false,
          message: "Non autorisé",
          statusCode: 401,
        };
      } else {
        if (
          !validerDates(
            dateDepart ? new Date(dateDepart) : offer.dateDepart,
            dateArrivee ? new Date(dateArrivee) : offer.dateArrivee
          )
        ) {
          // Les dates sont invalides
          return {
            isUpdated: false,
            message: "Dates invalides",
            statusCode: 400,
          };
        } else if (
          (lieuDepart && !validerVille(lieuDepart)) ||
          (lieuArrivee && !validerVille(lieuArrivee))
        ) {
          return {
            isUpdated: false,
            statusCode: 400,
            message: "Villes invalides",
          };
        } else {
          // La mise à jour, on évalue les champs nuls
          const update = {
            lieuDepart: lieuDepart ?? offer.lieuDepart,
            lieuArrivee: lieuArrivee ?? offer.lieuArrivee,
            dateDepart: dateDepart ? new Date(dateDepart) : offer.dateDepart,
            dateArrivee: dateArrivee
              ? new Date(dateArrivee)
              : offer.dateArrivee,
            prixKg: prixKg ?? offer.prixKg,
            poidsDispo: poidsDispo ?? offer.poidsDispo,
          };

          await offer.updateOne(update);
          return {
            isUpdated: true,
            message: "Offre mise à jour",
            data: offerId,
          };
        }
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

export default updateOffer;
