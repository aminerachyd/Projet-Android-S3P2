import { hash, userInfos } from "./helpers";
import UserModel from "../models/User";
import jwt from "jsonwebtoken";

const JWT_VERSION = process.env.JWT_VERSION;
const JWT_SECRET = <string>process.env.JWT_SECRET;

/**
 * Fonction pour authentifier un utilisateur
 * @param email Email de l'utilisateur
 * @param password Mot de passe de l'utilisateur
 */
const authenticateUser = async (
  email: string,
  password: string
): Promise<{
  isAuth: Boolean;
  message: String;
  statusCode?: number;
  data?: {};
}> => {
  try {
    const user = await UserModel.findOne({ email });
    if (!user || !password) {
      // Utilisateur non trouvé
      return {
        isAuth: false,
        message: "Email ou mot de passe incorrect",
        statusCode: 400,
      };
    } else {
      // Boolean pour vérifier si le mot de passe est valide ou pas
      let passwordIsValid = hash(password) === user.password;

      if (!passwordIsValid) {
        // Mot de passe incorrect
        return {
          isAuth: false,
          message: "Email ou mot de passe incorrect",
          statusCode: 400,
        };
      } else {
        // On met l'ID de l'utilisateur dans le JWT
        const userData = {
          user: {
            id: user._id,
            jwtVersion: JWT_VERSION,
          },
        };

        // On récupère le token
        const token = jwt.sign(userData, JWT_SECRET);

        return {
          isAuth: true,
          message: "Utilisateur authentifié",
          data: { token, ...userInfos(user!) },
        };
      }
    }
  } catch (error) {
    console.log(error);
    return {
      isAuth: false,
      message: "Erreur du serveur",
      statusCode: 500,
    };
  }
};

export default authenticateUser;
