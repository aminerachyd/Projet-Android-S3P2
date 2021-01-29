import UserModel from "../../models/User";
import { userInfos } from "../helpers";

/**
 * Fonction pour lire tout les utilisateurs depuis la base de données
 */
const fetchAllUser = async (): Promise<{
  isFetched: boolean;
  message: string;
  data?: {};
  statusCode?: number;
}> => {
  try {
    const result = await UserModel.find();
    const modifiedResult = result.map((user) => userInfos(user));

    return {
      isFetched: true,
      message: "Liste d'utilisateur récupérée",
      data: modifiedResult,
    };
  } catch (error) {
    console.log(error);

    return {
      isFetched: false,
      message: "Erreur du serveur",
      statusCode: 500,
    };
  }
};

export default fetchAllUser;
