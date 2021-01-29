import express from "express";
import auth from "../middleware/auth";
import addOffer from "../utils/offersUtils/addOffer";
import fetchOffer from "../utils/offersUtils/fetchOffer";
import updateOffer from "../utils/offersUtils/updateOffer";
import deleteOffer from "../utils/offersUtils/deleteOffer";

const router = express.Router();

/**
 * Route pour enregistrer une nouvelle offre
 * ROUTE: /user
 * METHOD: POST
 * RETURN: ID de l'offre enregistrée
 */
router.post("/", auth, async (req, res) => {
  const { isAdded, message, data, statusCode } = await addOffer(
    req.body,
    req.user
  );

  if (!isAdded) {
    res.status(statusCode!).send({
      error: message,
    });
  } else {
    res.send({ message, payload: data });
  }
});

/**
 * Route pour récupérer une offre
 * ROUTE: /offer/id
 * METHOD: GET
 * RETURN: L'offre demandée
 */
router.get("/:id", async (req, res) => {
  const id = req.params.id;

  const { isFetched, message, data, statusCode } = await fetchOffer(id);
  if (!isFetched) {
    res.status(statusCode!).send({
      error: message,
    });
  } else {
    res.send({ message, payload: data });
  }
});

/**
 * Route pour modifier une offre
 * ROUTE: /offer/id
 * METHOD: PUT
 * RETURN: ID de l'offre modifiée
 */
router.put("/:id", auth, async (req, res) => {
  const id = req.params.id;

  const { isUpdated, message, data, statusCode } = await updateOffer(
    req.body,
    id,
    req.user?.id
  );

  if (!isUpdated) {
    res.status(statusCode!).send({
      error: message,
    });
  } else {
    res.send({ message, payload: data });
  }
});

/**
 * Route pour supprimer une offre
 * ROUTE: /offer/id
 * METHOD: DELETE
 * RETURN: ID de l'offre supprimée
 */
router.delete("/:id", auth, async (req, res) => {
  const id = req.params.id;

  const { isDeleted, message, data, statusCode } = await deleteOffer(
    id,
    req.user?.id
  );

  if (!isDeleted) {
    res.status(statusCode!).send({
      error: message,
    });
  } else {
    res.send({ message, payload: data });
  }
});

export default router;
