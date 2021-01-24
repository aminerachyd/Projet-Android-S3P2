import express from "express";
import UserModel from "../models/User";
import { hash, userInfos } from "../utils/helpers";

const router = express.Router();

/**
 * Route pour enregistrer un nouvel utilisateur
 * ROUTE: /user
 * METHOD: POST
 * RETURN: ID de l'utilisateur enregistré
 */
router.post("/", async (req, res) => {
  const { email, nom, prenom, telephone, password } = req.body;

  const newUser = new UserModel({
    email,
    nom,
    prenom,
    telephone,
    password: hash(password),
  });

  try {
    const result = await newUser.save();

    res.send({ message: "Utilisateur ajouté", payload: result._id });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

/**
 * Route pour récupérer un unique utilisateur
 * ROUTE: /user/id
 * METHOD: GET
 * RETURN: L'utilisateur demandé
 */
router.get("/:id", async (req, res) => {
  const id = req.params.id;

  try {
    const result = await UserModel.findById(id);

    res.send({
      message: "Utilisateur récupéré",
      payload: userInfos(result),
    });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

/**
 * Route pour modifier un utilisateur
 * ROUTE: /user/id
 * METHOD: PUT
 * RETURN: ID de l'utilisateur modifié
 */
router.put("/:id", async (req, res) => {
  const id = req.params.id;

  // TODO Check if the fields are not null
  const update = {
    email: req.body.email,
    nom: req.body.nom,
    prenom: req.body.prenom,
    telephone: req.body.telephone,
    password: <string>hash(req.body.password),
  };

  try {
    await UserModel.findOneAndUpdate({ _id: id }, update);

    res.send({ message: "Utilisateur mis à jour", payload: id });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

/**
 * Route pour supprimer un utilisateur
 * ROUTE: /user/id
 * METHOD: DELETE
 * RETURN: ID de l'utilisateur supprimée
 */
router.delete("/:id", async (req, res) => {
  const id = req.params.id;

  try {
    await UserModel.findOneAndDelete({ _id: id });

    res.send({ message: "Utilisateur supprimé", payload: id });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

export default router;
