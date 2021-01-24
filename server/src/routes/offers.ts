import express from "express";
import OfferModel from "../models/Offer";
import UserModel from "../models/User";
import { userInfos } from "../utils/helpers";

const router = express.Router();

// TODO Have to implement pagination and offset
/**
 * Route pour récupérer toutes les offres
 * ROUTE: /offers
 * METHOD: GET
 * RETURN: Tableau d'offres
 */
router.get("/", async (_, res) => {
  try {
    const result = await OfferModel.find();

    let payload: any = [];

    for (const offer of result) {
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

      payload.push({
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
      payload,
    });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

export default router;
