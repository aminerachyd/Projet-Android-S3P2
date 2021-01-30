import { findUserOffers } from "../helpers";

/**
 * Fonction pour lire les offres d'un utilisateur donné
 * @param id L'ID de l'utilisateur dont on souhaite lire les offres
 * @param userId L'ID de l'utilisateur authentifié, doit être identitique à l'ID précédent
 * @param page La page demandée
 * @param limit La limite d'offre par page
 * @param MAX_LIMIT La limite maximale à appliquer dans la pagination
 */
const fetchUserOffers = async (
  id,
  userId,
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

  if (id !== userId) {
    // L'utilisateur ne correspond à celui authentifié
    return {
      isFetched: false,
      message: "Non autorisé",
      statusCode: 401,
    };
  } else {
    try {
      const query = await findUserOffers(id, limitNumber, pageNumber);

      if (!query) {
        throw new Error();
      }

      const { offers: result, hasMore } = query;

      // const hasMore = (<any[]>result).length !== limitNumber + 1;
      return {
        isFetched: true,
        message: "Offres récupérées",
        data: {
          hasMore,
          offers: result,
          page: realPageNumber,
          limit: limitNumber,
        },
      };
    } catch (error) {
      console.log(error);

      return {
        isFetched: false,
        message: "Erreur du serveur",
        statusCode: 500,
      };
    }
  }
};
export default fetchUserOffers;
