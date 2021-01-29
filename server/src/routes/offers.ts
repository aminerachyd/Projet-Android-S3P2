import express from "express";
import UserModel from "../models/User";
import OfferModel from "../models/Offer";
import { userInfos, filterOffers, findUserOffers } from "../utils/helpers";
import auth from "../middleware/auth";
import fetchAllOffers from "../utils/offersUtils/fetchAllOffers";
import fetchAllOffersWithFilters from "../utils/offersUtils/fetchAllOffersWithFilters";
import fetchUserOffers from "../utils/offersUtils/fetchUserOffers";

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

  const { isFetched, data, message, statusCode } = await fetchAllOffers(
    page,
    limit,
    MAX_LIMIT
  );

  if (!isFetched) {
    res.status(statusCode!).send({
      error: message,
    });
  } else {
    res.send({ message, payload: data });
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

  const {
    isFetched,
    data,
    message,
    statusCode,
  } = await fetchAllOffersWithFilters(req.body, page, limit, MAX_LIMIT);

  if (!isFetched) {
    res.status(statusCode!).send({
      error: message,
    });
  } else {
    res.send({ message, payload: data });
  }
});

/**
 * Route pour récupérer toutes les offres d'un utilisateur
 * ROUTE: /offers/user/id
 * METHOD: GET
 * RETURN: Tableau d'offres, un boolean hasMore qui indique si il y'a plus de données à récupérer, la page et la limite
 */
router.get("/user/:id", auth, async (req, res) => {
  // On récupère la page et le limit depuis la requete
  const { page, limit } = req.query;
  const { id } = req.params;
  const userId = req.user?.id;

  // FIXME régler la pagination ici
  const { isFetched, data, message, statusCode } = await fetchUserOffers(
    id,
    userId,
    page,
    limit,
    MAX_LIMIT
  );

  if (!isFetched) {
    res.status(statusCode!).send({
      error: message,
    });
  } else {
    res.send({ message, payload: data });
  }
});

export default router;
