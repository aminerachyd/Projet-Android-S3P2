import express from "express";
import User from "../models/User";
import { hash } from "../utils/helpers";

const router = express.Router();

router.get("/", (_, res) => {
  res.send("Route user");
});

/**
 * Route pour enregistrer un nouvel utilisateur
 * ROUTE: /user
 * METHOD: POST
 * RETURN: ID de l'utilisateur enregistré
 */
router.post("/", async (req, res) => {
  const { email, nom, prenom, telephone, password } = req.body;

  const newUser = new User({
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
    const result = await User.findById(id);

    res.send({
      message: "Utilisateur récupéré",
      payload: result,
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
  const update = {
    email: req.body.email,
    nom: req.body.nom,
    prenom: req.body.prenom,
    telepone: req.body.telephone,
    password: hash(req.body.password),
  };

  try {
    await User.findOneAndUpdate({ _id: id }, update);

    res.send({ message: "Utilisateur mis à jour", payload: id });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "erreur du serveur",
    });
  }
});

/**
 * Route pour supprimer un utilisateur
 * ROUTE: /user/id
 * METHOD: DELETE
 * RETURN: ID de l'utilisateur modifié
 */
router.delete("/:id", async (req, res) => {
  const id = req.params.id;

  try {
    await User.findOneAndDelete({ _id: id });

    res.send({ message: "Utilisateur supprimé", payload: id });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "erreur du serveur",
    });
  }
});

export default router;
