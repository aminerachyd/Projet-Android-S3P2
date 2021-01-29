import express from "express";
import fetchAllUsers from "../utils/usersUtils/fetchAllUsers";

const router = express.Router();

/**
 * Route pour récupérer la liste d'utilisateurs
 * ROUTE: /users
 * METHOD: GET
 * RETURN: La liste des utilisateurs
 */
router.get("/", async (_, res) => {
  const { isFetched, data, message, statusCode } = await fetchAllUsers();

  if (!isFetched) {
    res.status(statusCode!).send({
      message,
    });
  } else {
    res.send({
      message,
      payload: data,
    });
  }
});

export default router;
