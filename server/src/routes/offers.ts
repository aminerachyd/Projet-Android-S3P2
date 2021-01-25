import express from "express";
import UserModel from "../models/User";
import OfferModel from "../models/Offer";
import { userInfos, filterOffers } from "../utils/helpers";

// Limite par défaut d'offres à récupérer par requete
const MAX_LIMIT = 15;

const router = express.Router();

/**
 * Route pour récupérer toutes les offres
 * ROUTE: /offers
 * METHOD: GET
 * RETURN: Tableau d'offres, un boolean hasMore qui indique si il y'a plus de données à récupérer, la page et la limite
 */
router.get("/", async (req, res) => {
  // On récupère la page et le limit depuis la requete
  const { page, limit } = req.query;

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

    res.send({
      message: "Offres récupérées",
      payload: { hasMore, offers, page: realPageNumber, limit: limitNumber },
    });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

/**
 * Route pour récupérer toutes les offres avec filtres
 * ROUTE: /offers
 * METHOD: POST
 * RETURN: Tableau d'offres, un boolean hasMore qui indique si il y'a plus de données à récupérer, la page et la limite
 */
router.post("/", async (req, res) => {
  // On récupère la page et le limit depuis la requete
  const { page, limit } = req.query;

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
    const result = await filterOffers(req.body, limitNumber, pageNumber);

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

    res.send({
      message: "Offres récupérées",
      payload: { hasMore, offers, page: realPageNumber, limit: limitNumber },
    });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

export default router;
