import express from "express";
import jwt from "jsonwebtoken";
import { hash } from "../utils/helpers";
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
    message: "utilisateur authentifié",
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

  if (!user) {
    // Utilisateur non trouvé
    res.status(400).send({
      error: "Email ou mot de passe incorrect",
    });
  } else {
    let passwordIsValid = hash(password) === (<any>user).password;

    if (!passwordIsValid) {
      // Mot de passe incorrect
      res.status(400).send({
        error: "Email ou mot de passe incorrect",
      });
    } else {
      // On met l'ID de l'utilisateur dans le JWT
      const payload = {
        user: {
          id: user?._id,
        },
      };

      jwt.sign(
        payload,
        process.env.JWT_SECRET!,
        {
          expiresIn: 3600,
        },
        (err, token) => {
          // Une erreur existe
          if (err) {
            console.log(err);
            res.status(500).send({
              error: "Erreur du serveur",
            });
          } else {
            res.send({
              message: "utilisateur authentifié",
              payload: token,
            });
          }
        }
      );
    }
  }
});

export default router;
