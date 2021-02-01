import { validerDates, isUserValid, validerVille } from "../helpers";
import OfferModel from "../../models/Offer";

interface offerDetails {
  lieuDepart: string;
  lieuArrivee: string;
  dateDepart: Date;
  dateArrivee: Date;
  prixKg: number;
  poidsDispo: number;
}

/**
 * Fonction pour ajouter une offre dans la base de données
 * @param details Objet contenant les détails de l'offre
 * @param user L'ID de l'utilisateur qui enregistre l'offre
 */
const addOffer = async (
  {
    lieuDepart,
    lieuArrivee,
    dateDepart,
    dateArrivee,
    prixKg,
    poidsDispo,
  }: offerDetails,
  user
): Promise<{
  isAdded: Boolean;
  message: String;
  statusCode?: number;
  data?: {};
}> => {
  if (!validerDates(dateDepart, dateArrivee)) {
    return {
      isAdded: false,
      statusCode: 400,
      message: "Dates invalides",
    };
  } else if (!validerVille(lieuDepart) || !validerVille(lieuArrivee)) {
    return {
      isAdded: false,
      statusCode: 400,
      message: "Villes invalides",
    };
  } else {
    // On récupère l'utilisateur
    const { isValid, user: thisUser } = await isUserValid(user?.id);
    if (!isValid) {
      return {
        isAdded: false,
        statusCode: 400,
        message: "Utilisateur introuvable",
      };
    } else {
      const newOffer = new OfferModel({
        user: user.id,
        lieuDepart,
        lieuArrivee,
        dateDepart: new Date(dateDepart),
        dateArrivee: new Date(dateArrivee),
        prixKg,
        poidsDispo,
      });

      try {
        const result = await newOffer.save();
        thisUser!.offres.push(result._id);
        await thisUser!.save();
        return {
          isAdded: true,
          message: "Nouvelle offre ajoutée",
          data: result._id,
        };
      } catch (error) {
        // Si un champ est manquant, on renvoit une erreur
        if ((<string>error._message).includes("offer validation failed")) {
          return {
            isAdded: false,
            message: "Un ou plusieurs champs sont manquants",
            statusCode: 400,
          };
        } else {
          console.log(error._message);

          return {
            isAdded: false,
            message: "Erreur du serveur",
            statusCode: 500,
          };
        }
      }
    }
  }
};

export default addOffer;
