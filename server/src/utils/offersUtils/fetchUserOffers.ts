import { findUserOffers } from "../helpers";

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
      const result = await findUserOffers(id, limitNumber, pageNumber);

      let hasMore = (<any[]>result).length === limitNumber + 1;
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
