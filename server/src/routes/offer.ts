import express from "express";
import auth from "../middleware/auth";
import OfferModel from "../models/Offer";
import UserModel from "../models/User";
import { userInfos } from "../utils/helpers";

const router = express.Router();

/**
 * Route pour enregistrer une nouvelle offre
 * ROUTE: /user
 * METHOD: POST
 * RETURN: ID de l'offre enregistrée
 */
router.post("/", auth, async (req, res) => {
  // On récupère l'utilisateur
  const user = req.user;

  // On récupère les données de l'offre
  const {
    lieuDepart,
    lieuArrivee,
    dateDepart,
    dateArrivee,
    prixKg,
    poidsDispo,
  } = req.body;

  const newOffer = new OfferModel({
    user: user!.id,
    lieuDepart,
    lieuArrivee,
    dateDepart: new Date(dateDepart),
    dateArrivee: new Date(dateArrivee),
    prixKg,
    poidsDispo,
  });

  try {
    const result = await newOffer.save();

    res.send({ message: "Nouvelle offre ajoutée", payload: result._id });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

/**
 * Route pour récupérer une offre
 * ROUTE: /offer/id
 * METHOD: GET
 * RETURN: L'offre demandée
 */
router.get("/:id", async (req, res) => {
  const id = req.params.id;

  try {
    // XXX Fix types
    const result: any = await OfferModel.findById(id);
    const user = await UserModel.findById(result.user);

    if (!user || !result) {
      // L'utilisateur n'existe pas
      res.status(400).send({
        error: "Utilisateur ou offre non disponible",
      });
    } else {
      let {
        lieuDepart,
        lieuArrivee,
        dateDepart,
        dateArrivee,
        prixKg,
        poidsDispo,
      } = result;

      let payload = {
        id,
        user: userInfos(user),
        lieuDepart,
        lieuArrivee,
        dateDepart,
        dateArrivee,
        prixKg,
        poidsDispo,
      };
      res.send({
        message: "Offre récupérée",
        payload,
      });
    }
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

/**
 * Route pour modifier une offre
 * ROUTE: /offer/id
 * METHOD: PUT
 * RETURN: ID de l'offre modifiée
 */
router.put("/:id", auth, async (req, res) => {
  const id = req.params.id;

  const {
    lieuDepart,
    lieuArrivee,
    dateDepart,
    dateArrivee,
    prixKg,
    poidsDispo,
  } = req.body;

  const update = {
    lieuDepart,
    lieuArrivee,
    dateDepart: new Date(dateDepart),
    dateArrivee: new Date(dateArrivee),
    prixKg,
    poidsDispo,
  };

  try {
    // XXX Fix types
    let offer: any = await OfferModel.findById(id);

    if (!offer) {
      // Offre non trouvée
      res.status(400).send({
        error: "Utilisateur ou offre non disponible",
      });
    } else {
      if (offer.user.id !== req?.user?.id) {
        // L'utilisateur actuel ne correspond pas à l'utilisateur propriétaire de l'offre
        res.status(401).send({
          error: "Non autorisé",
        });
      } else {
        offer?.update(update);

        res.send({ message: "Offre mise à jour", payload: id });
      }
    }
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

// /**
//  * Route pour supprimer une offre
//  * ROUTE: /offer/id
//  * METHOD: DELETE
//  * RETURN: ID de l'offre supprimée
//  */
router.delete("/:id", auth, async (req, res) => {
  const id = req.params.id;

  try {
    // XXX Fix types
    let offer: any = await OfferModel.findById(id);
    if (!offer) {
      // Offre non trouvée
      res.status(400).send({
        error: "Offre non disponible",
      });
    } else {
      if (offer.user.id !== req?.user?.id) {
        // L'utilisateur actuel ne correspond pas à l'utilisateur propriétaire de l'offre
        res.status(401).send({
          error: "Non autorisé",
        });
      } else {
        offer?.delete();

        res.send({ message: "Offre supprimée", payload: id });
      }
    }
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

export default router;
