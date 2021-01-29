import UserModel from "../../models/User";
import { userInfos } from "../helpers";

/**
 * Fonction pour récupérer un utilisateur donné
 * @param id L'ID de l'utilisateur à récupérer
 */
const fetchUser = async (
  id
): Promise<{
  isFetched: boolean;
  message: string;
  data?: {};
  statusCode?: number;
}> => {
  try {
    const user = await UserModel.findById(id);

    if (user) {
      return {
        isFetched: true,
        message: "Utilisateur récupéré",
        data: userInfos(user),
      };
    } else {
      // Utilisateur introuvable
      return {
        isFetched: false,
        message: "Utilisateur introuvable",
        statusCode: 400,
      };
    }
  } catch (error) {
    console.log(error);
    return {
      isFetched: false,
      message: "Erreur du serveur",
      statusCode: 500,
    };
  }
};

export default fetchUser;
