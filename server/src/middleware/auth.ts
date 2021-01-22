import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";
import { DecodedJWT } from "../types";

/**
 * Middleware d'authentification
 * Si le token est disponible
 * On renvoi l'id du utilisateur dans req.user
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

    const decoded: DecodedJWT = <DecodedJWT>(
      jwt.verify(token, process.env.JWT_SECRET!)
    );
    if (!decoded) {
    } else {
      req.user = decoded.user;

      return next();
    }
  }
};

export default auth;
