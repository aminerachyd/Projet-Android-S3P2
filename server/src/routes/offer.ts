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

  // FIXME
  /**
   * Utiliser un format pour les dates
   * Recevoir la date du client
   * Créer la date avec :
   * let date = new Date('yyyy-mm-ddThh:mm:ss')
   * Ou
   * let date = new Date(yyyy,mm,dd,hh,mm,ss)
   */
  let date = new Date();

  const newOffer = new OfferModel({
    user: user!.id,
    lieuDepart,
    lieuArrivee,
    dateDepart: date,
    dateArrivee: date,
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
    const result: any = await OfferModel.findById(id);
    const user = await UserModel.findById(result.user);

    let {
      lieuDepart,
      lieuArrivee,
      dateDepart,
      dateArrivee,
      prixKg,
      poidsDispo,
    } = result;

    if (!user) {
      // TODO l'utilisateur n'existe pas
      console.log("no user");
    } else {
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
        message: "Utilisateur récupéré",
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

// /**
//  * Route pour modifier un utilisateur
//  * ROUTE: /user/id
//  * METHOD: PUT
//  * RETURN: ID de l'utilisateur modifié
//  */
// router.put("/:id", async (req, res) => {
//   const id = req.params.id;
//   const update = {
//     email: req.body.email,
//     nom: req.body.nom,
//     prenom: req.body.prenom,
//     telepone: req.body.telephone,
//     password: hash(req.body.password),
//   };

//   try {
//     await UserModel.findOneAndUpdate({ _id: id }, update);

//     res.send({ message: "Utilisateur mis à jour", payload: id });
//   } catch (error) {
//     console.log(error);

//     res.status(500).send({
//       error: "Erreur du serveur",
//     });
//   }
// });

// /**
//  * Route pour supprimer un utilisateur
//  * ROUTE: /user/id
//  * METHOD: DELETE
//  * RETURN: ID de l'utilisateur modifié
//  */
// router.delete("/:id", async (req, res) => {
//   const id = req.params.id;

//   try {
//     await UserModel.findOneAndDelete({ _id: id });

//     res.send({ message: "Utilisateur supprimé", payload: id });
//   } catch (error) {
//     console.log(error);

//     res.status(500).send({
//       error: "Erreur du serveur",
//     });
//   }
// });

export default router;
