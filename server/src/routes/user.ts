import express from "express";
const router = express.Router();
import User from "../models/User";
import { hash } from "../utils/helpers";

router.get("/", (_, res) => {
  res.send("Route user");
});

/**
 * Route pour enregistrer un nouvel utilisateur
 * ROUTE: /user
 * METHOD: Post
 * RETURN: L'utilisateur enregistré
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

  const result = await newUser.save();

  console.log(result);

  res.send(result);
});

export default router;
