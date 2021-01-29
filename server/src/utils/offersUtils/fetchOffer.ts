import OfferModel from "../../models/Offer";
import UserModel from "../../models/User";
import { userInfos } from "../helpers";

/**
 * Fonction pour récupérer une offre à partir de son id
 * @param id L'id de l'offre
 */
const fetchOffer = async (
  id
): Promise<{
  isFetched: Boolean;
  message: String;
  statusCode?: number;
  data?: {};
}> => {
  try {
    const offer = await OfferModel.findById(id);
    const user = await UserModel.findById(offer?.user);

    if (!user || !offer) {
      // L'utilisateur n'existe pas, on renvoit une erreur
      return {
        isFetched: false,
        statusCode: 400,
        message: "Utilisateur ou offre non disponible",
      };
    } else {
      // La data à envoyer
      let payload = {
        id,
        user: userInfos(user),
        lieuDepart: offer.lieuDepart,
        lieuArrivee: offer.lieuArrivee,
        dateDepart: offer.dateDepart,
        dateArrivee: offer.dateArrivee,
        prixKg: offer.prixKg,
        poidsDispo: offer.poidsDispo,
      };
      return {
        isFetched: true,
        message: "Offre récupérée",
        data: payload,
      };
    }
  } catch (error) {
    console.log(error);

    return {
      isFetched: false,
      message: "Erreur du serveur",
      statusCode: 500,
    };
  }
};

export default fetchOffer;
