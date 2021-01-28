import express from "express";
import auth from "../middleware/auth";
import authenticateUser from "../utils/authenticateUser";

const router = express.Router();

/**
 * Route pour vérifier l'authentification
 * ROUTE: /auth
 * METHOD: GET
 * RETURN: Test de l'authentification
 */
router.get("/", auth, async (req, res) => {
  // Si le token est valide, le middleware auth devrait marcher et on aura le req.user
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
  // Données récupérées depuis la requete
  const { email, password } = req.body;

  const { isAuth, message, data, statusCode } = await authenticateUser(
    email,
    password
  );

  if (!isAuth) {
    res.status(statusCode!).send({
      error: message,
    });
  } else {
    res.send({
      message: message,
      payload: data,
    });
  }
});

export default router;
