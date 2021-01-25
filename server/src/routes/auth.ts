import express from "express";
import jwt from "jsonwebtoken";
import { hash, userInfos } from "../utils/helpers";
import UserModel from "../models/User";
import auth from "../middleware/auth";

const router = express.Router();

/**
 * Route pour vérifier l'authentification
 * ROUTE: /auth
 * METHOD: GET
 * RETURN: Test de l'authentification
 */
router.get("/", auth, async (req, res) => {
  res.send({
    message: "Utilisateur authentifié",
    payload: req.user,
  });
});

/**
 * Route pour authentifier un utilisateur
 * ROUTE: /auth
 * METHOD: POST
 * RETURN: Un token si l'utilisateur est bien connecté
 */
router.post("/", async (req, res) => {
  const { email, password } = req.body;

  let user = await UserModel.findOne({ email });

  if (!user || !password) {
    // Utilisateur non trouvé
    res.status(400).send({
      error: "Email ou mot de passe incorrect",
    });
  } else {
    // Boolean pour vérifier si le mot de passe est valide ou pas
    let passwordIsValid = hash(password) === user.password;

    if (!passwordIsValid) {
      // Mot de passe incorrect
      res.status(400).send({
        error: "Email ou mot de passe incorrect",
      });
    } else {
      // On met l'ID de l'utilisateur dans le JWT
      const userData = {
        user: {
          id: user._id,
          jwtVersion: process.env.JWT_VERSION,
        },
      };

      jwt.sign(userData, process.env.JWT_SECRET!, (err, token) => {
        // Une erreur existe
        if (err) {
          console.log(err);
          res.status(500).send({
            error: "Erreur du serveur",
          });
        } else {
          res.send({
            message: "Utilisateur authentifié",
            payload: { token, ...userInfos(user!) },
          });
        }
      });
    }
  }
});

export default router;
