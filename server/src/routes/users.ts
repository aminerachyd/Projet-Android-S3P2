import express from "express";
import UserModel from "../models/User";
import { userInfos } from "../utils/helpers";

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
    const modifiedResult = result.map((user) => userInfos(user));

    res.send({
      message: "Liste d'utilisateurs récupérée",
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
