import express from "express";
import auth from "../middleware/auth";
import OfferModel from "../models/Offer";
import UserModel from "../models/User";
import { userInfos, validerDates } from "../utils/helpers";

const router = express.Router();

/**
 * Route pour enregistrer une nouvelle offre
 * ROUTE: /user
 * METHOD: POST
 * RETURN: ID de l'offre enregistrée
 */
router.post("/", auth, async (req, res) => {
  // On récupère les données de l'offre
  const {
    lieuDepart,
    lieuArrivee,
    dateDepart,
    dateArrivee,
    prixKg,
    poidsDispo,
  } = req.body;

  if (!validerDates(dateDepart, dateArrivee)) {
    res.status(400).send({
      error: "Dates invalides",
    });
  } else {
    // On récupère l'utilisateur
    // FIXME Doit vérifier si l'utilisateur existe déjà dans la base de données ou pas
    const user = req.user;
    let thisUser = await UserModel.findById(user?.id);
    if (!user || !thisUser) {
      res.status(400).send({
        error: "Utilisateur invalide",
      });
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
        thisUser.offres.push(result._id);
        await thisUser.save();
        res.send({ message: "Nouvelle offre ajoutée", payload: result._id });
      } catch (error) {
        // Si un champ est manquant, on renvoit une erreur
        if ((<string>error._message).includes("offer validation failed")) {
          res.status(400).send({
            error: "Un ou plusieurs champs sont manquants",
          });
        } else {
          console.log(error._message);

          res.status(500).send({
            error: "Erreur du serveur",
          });
        }
      }
    }
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
    const offer = await OfferModel.findById(id);
    const user = await UserModel.findById(offer?.user);

    if (!user || !offer) {
      // L'utilisateur n'existe pas, on renvoit une erreur
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
      } = offer;

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

  try {
    let offer = await OfferModel.findById(id);

    if (!offer) {
      // Offre non trouvée
      res.status(400).send({
        error: "Utilisateur ou offre non disponible",
      });
    } else {
      if (offer.user._id != req?.user?.id) {
        // FIXME Doit vérifier si l'utilisateur existe déjà dans la base de données ou pas
        // L'utilisateur actuel ne correspond pas à l'utilisateur propriétaire de l'offre
        res.status(401).send({
          error: "Non autorisé",
        });
      } else {
        if (
          !validerDates(
            dateDepart ? new Date(dateDepart) : offer.dateDepart,
            dateArrivee ? new Date(dateArrivee) : offer.dateArrivee
          )
        ) {
          // Les dates sont invalides
          res.status(400).send({
            error: "Dates invalides",
          });
        } else {
          // La mise à jour, on évalue les champs nuls
          const update = {
            lieuDepart: lieuDepart ?? offer.lieuDepart,
            lieuArrivee: lieuArrivee ?? offer.lieuArrivee,
            dateDepart: dateDepart ? new Date(dateDepart) : offer.dateDepart,
            dateArrivee: dateArrivee
              ? new Date(dateArrivee)
              : offer.dateArrivee,
            prixKg: prixKg ?? offer.prixKg,
            poidsDispo: poidsDispo ?? offer.poidsDispo,
          };

          await offer.updateOne(update);

          res.send({ message: "Offre mise à jour", payload: id });
        }
      }
    }
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

/**
 * Route pour supprimer une offre
 * ROUTE: /offer/id
 * METHOD: DELETE
 * RETURN: ID de l'offre supprimée
 */
router.delete("/:id", auth, async (req, res) => {
  const id = req.params.id;

  try {
    let offer = await OfferModel.findById(id);
    if (!offer) {
      // Offre non trouvée
      res.status(400).send({
        error: "Offre non disponible",
      });
    } else {
      if (offer.user._id != req?.user?.id) {
        // FIXME Doit vérifier si l'utilisateur existe déjà dans la base de données ou pas
        // L'utilisateur actuel ne correspond pas à l'utilisateur propriétaire de l'offre
        res.status(401).send({
          error: "Non autorisé",
        });
      } else {
        await offer.deleteOne();

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
