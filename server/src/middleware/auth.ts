import { NextFunction, Request, Response } from "express";
import { DecodedJWT } from "../types";
import jwt from "jsonwebtoken";

/**
 * Middleware d'authentification
 * Si le token est disponible on renvoi l'id de l'utilisateur dans req.user
 */
const auth = (req: Request, res: Response, next: NextFunction) => {
  let token = req.header("x-auth-token");

  // Pas de token dans la requête
  if (!token) {
    return res.status(401).send({
      error: "Non autorisé",
    });
  } else {
    // On décode le JWT pour retrouver l'utilisateur

    try {
      const decoded: DecodedJWT = <DecodedJWT>(
        jwt.verify(token, process.env.JWT_SECRET!)
      );

      req.user = decoded.user;

      if (decoded.user.jwtVersion === process.env.JWT_VERSION) {
        return next();
      } else {
        res.status(400).send({
          error: "Token invalide, veuillez vous reconnecter",
        });
      }
    } catch (error) {
      console.log(error);

      res.status(500).send({
        error: "Erreur du serveur",
      });
    }
  }
};

export default auth;
