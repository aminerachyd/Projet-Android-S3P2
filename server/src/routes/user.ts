import express from "express";
import UserModel from "../models/User";
import { hash, userInfos } from "../utils/helpers";
import auth from "../middleware/auth";

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
    // Si un champ est manquant, on renvoit une erreur
    if ((<string>error._message).includes("user validation failed")) {
      res.status(400).send({
        error: "Un ou plusieurs champs sont manquants",
      });
    } else {
      console.log(error);
      // Email déjà utilisé par un autre utilisateur
      if (error.code === 11000) {
        res.status(409).send({
          error: "Email déjà utilisé",
        });
      } else {
        res.status(500).send({
          error: "Erreur du serveur",
        });
      }
    }
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
    const user = await UserModel.findById(id);

    if (user) {
      res.send({
        message: "Utilisateur récupéré",
        payload: userInfos(user),
      });
    } else {
      // Utilisateur introuvable
      res.status(404).send({
        error: "Utilisateur introuvable",
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
 * Route pour modifier un utilisateur
 * ROUTE: /user/id
 * METHOD: PUT
 * RETURN: ID de l'utilisateur modifié
 */
router.put("/:id", auth, async (req, res) => {
  const id = req.params.id;

  const { email, nom, prenom, telephone, password } = req.body;

  try {
    let user = await UserModel.findById(id);

    if (!user) {
      // Utilisateur introuvable
      res.status(404).send({
        error: "Utilisateur introuvable",
      });
    } else {
      if (user._id != req?.user?.id) {
        // L'utilisateur actuel ne correspond pas à l'utilisateur subissant la modification
        res.status(401).send({
          error: "Non autorisé",
        });
      } else {
        // Vérification des champs nuls
        const update = {
          email: email ?? user.email,
          nom: nom ?? user.nom,
          prenom: prenom ?? user.prenom,
          telephone: telephone ?? user.telephone,
          password: password ? <string>hash(password) : user.password,
        };

        await user.updateOne(update);

        res.send({
          message: "Utilisateur mis à jour",
          payload: id,
        });
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
 * Route pour supprimer un utilisateur
 * ROUTE: /user/id
 * METHOD: DELETE
 * RETURN: ID de l'utilisateur supprimée
 */
router.delete("/:id", auth, async (req, res) => {
  const id = req.params.id;

  try {
    let user = await UserModel.findById(id);

    if (!user) {
      // Utilisateur introuvable
      res.status(404).send({
        error: "Utilisateur introuvable",
      });
    } else {
      if (user._id != req?.user?.id) {
        // L'utilisateur actuel ne correspond pas à l'utilisateur subissant la suppression
        res.status(401).send({
          error: "Non autorisé",
        });
      } else {
        user.deleteOne();

        res.send({ message: "Utilisateur supprimé", payload: id });
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
