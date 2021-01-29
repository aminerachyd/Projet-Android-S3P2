import UserModel from "../../models/User";
import { hash } from "../helpers";

/**
 * Fonction pour ajouter un utilisateur dans la base de données
 * @param infos Objet contenant les informations de l'utilisateur à ajouter
 */
const addUser = async ({
  email,
  nom,
  prenom,
  telephone,
  password,
}): Promise<{
  isAdded: boolean;
  message: string;
  data?: {};
  statusCode?: number;
}> => {
  const newUser = new UserModel({
    email,
    nom,
    prenom,
    telephone,
    offres: [],
    password: hash(password),
  });

  try {
    const result = await newUser.save();

    return {
      isAdded: true,
      message: "Utilisateur ajouté",
      data: result._id,
    };
    // res.send({ message: "Utilisateur ajouté", payload: result._id });
  } catch (error) {
    // Si un champ est manquant, on renvoit une erreur
    if ((<string>error._message).includes("user validation failed")) {
      return {
        isAdded: false,
        message: "Un ou plusieurs champs sont manquants",
        statusCode: 400,
      };
    } else {
      console.log(error);
      // Email déjà utilisé par un autre utilisateur
      if (error.code === 11000) {
        return {
          isAdded: false,
          message: "Email déjà utilisé",
          statusCode: 409,
        };
      } else {
        return {
          isAdded: false,
          message: "Erreur du serveur",
          statusCode: 500,
        };
      }
    }
  }
};

export default addUser;
