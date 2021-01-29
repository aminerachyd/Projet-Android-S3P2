import OfferModel from "../../models/Offer";
import UserModel from "../../models/User";
import { userInfos } from "../helpers";

const fetchAllOffers = async (
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
    /**
     * On récupère une offre de plus
     * Si cette dernière offre existe, c'est qu'il y'a plus de documents à récupérer, soit une prochaine page existe.
     * Si elle n'existe pas, c'est la dernière page.
     */
    const result = await OfferModel.find()
      .limit(limitNumber + 1)
      .skip(pageNumber)
      .sort({ updatedAt: "desc" });

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
      statusCode: 400,
    };
  }
};

export default fetchAllOffers;
