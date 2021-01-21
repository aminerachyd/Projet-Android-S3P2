import express from "express";
import { User } from "src/types";
import UserModel from "../models/User";

const router = express.Router();

/**
 * Route pour récupérer la liste d'utilisateurs
 * ROUTE: /users
 * METHOD: GET
 * RETURN: La liste des utilisateurs
 */
router.get("/", async (_, res) => {
  try {
    const result = await UserModel.find();
    const modifiedResult = result.map((user: User | any) => ({
      id: user._id,
      email: user.email,
      nom: user.nom,
      prenom: user.prenom,
      telephone: user.telephone,
    }));

    res.send({
      message: "Liste d'ttilisateurs récupérée",
      payload: modifiedResult,
    });
  } catch (error) {
    console.log(error);

    res.status(500).send({
      error: "Erreur du serveur",
    });
  }
});

export default router;
