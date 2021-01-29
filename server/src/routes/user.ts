import express from "express";
import auth from "../middleware/auth";
import addUser from "../utils/usersUtils/addUser";
import fetchUser from "../utils/usersUtils/fetchUser";
import updateUser from "../utils/usersUtils/updateUser";
import deleteUser from "../utils/usersUtils/deleteUser";

const router = express.Router();

/**
 * Route pour enregistrer un nouvel utilisateur
 * ROUTE: /user
 * METHOD: POST
 * RETURN: ID de l'utilisateur enregistré
 */
router.post("/", async (req, res) => {
  const { isAdded, message, data, statusCode } = await addUser(req.body);
  if (!isAdded) {
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

/**
 * Route pour récupérer un unique utilisateur
 * ROUTE: /user/id
 * METHOD: GET
 * RETURN: L'utilisateur demandé
 */
router.get("/:id", async (req, res) => {
  const { isFetched, message, data, statusCode } = await fetchUser(
    req.params.id
  );
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

/**
 * Route pour modifier un utilisateur
 * ROUTE: /user/id
 * METHOD: PUT
 * RETURN: ID de l'utilisateur modifié
 */
router.put("/:id", auth, async (req, res) => {
  const id = req.params.id;

  const { isUpdated, data, message, statusCode } = await updateUser(
    req.body,
    id,
    req.user?.id
  );

  if (!isUpdated) {
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

/**
 * Route pour supprimer un utilisateur
 * ROUTE: /user/id
 * METHOD: DELETE
 * RETURN: ID de l'utilisateur supprimée
 */
router.delete("/:id", auth, async (req, res) => {
  const id = req.params.id;

  const { isDeleted, data, message, statusCode } = await deleteUser(
    id,
    req.user?.id
  );

  if (!isDeleted) {
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
