import { filterOffers, userInfos } from "../helpers";
import UserModel from "../../models/User";

/**
 * Fonction pour lire toutes les offres depuis la base de données en appliquant un filtre
 * @param filters Un objet contenant plusieurs critères de recherche
 * @param page La page demandée
 * @param limit La limite d'offre par page
 * @param MAX_LIMIT La limite maximale à appliquer dans la pagination
 */
const fetchAllOffersWithFilters = async (
  filters,
  page,
  limit,
  MAX_LIMIT
): Promise<{
  isFetched: boolean;
  message: string;
  data?: {};
  statusCode?: number;
}> => {
  /*
   * On évalue la limite et la page
   * La limite ne doit pas dépasser la limite par défaut
   * La page est calculée en fonction de la limite évaluée
   */
  let limitNumber = parseInt(<string>limit)
    ? Math.min(parseInt(<string>limit), MAX_LIMIT)
    : MAX_LIMIT;

  let realPageNumber = page ? parseInt(<string>page) : 1;

  let pageNumber =
    parseInt(<string>page) > 1 ? limitNumber * (parseInt(<string>page) - 1) : 0;

  try {
    const result = await filterOffers(filters, limitNumber, pageNumber);

    let offers: any = [];

    let hasMore = result.length === limitNumber + 1;

    for (let i = 0; i < limitNumber; i++) {
      let offer = result[i];
      // On s'arrete au dernier index si l'offre de plus est null
      if (!offer) {
        break;
      }

      let {
        id,
        user,
        lieuDepart,
        lieuArrivee,
        dateDepart,
        dateArrivee,
        prixKg,
        poidsDispo,
      } = offer;

      let userData = await UserModel.findById(user._id);
      // Si l'utilisateur n'existe pas pour cette offre, on la saute
      if (!userData) {
        continue;
      }

      offers.push({
        id,
        user: userInfos(userData),
        lieuDepart,
        lieuArrivee,
        dateDepart,
        dateArrivee,
        prixKg,
        poidsDispo,
      });
    }

    return {
      isFetched: true,
      message: "Offres récupérées",
      data: { hasMore, offers, page: realPageNumber, limit: limitNumber },
    };
  } catch (error) {
    console.log(error);

    return {
      isFetched: false,
      message: "Erreur du serveur",
      statusCode: 500,
    };
  }
};

export default fetchAllOffersWithFilters;
