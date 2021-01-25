import crypto from "crypto";
import { FiltersType, UserType } from "../types";
import OfferModel from "../models/Offer";
const HASH_SECRET = process.env.HASH_SECRET;

/** Fonction pour hasher un mot de passe */
export const hash = (str: string): string | false => {
  if (str.length > 0) {
    let hash = crypto
      .createHmac("sha256", HASH_SECRET as string)
      .update(str)
      .digest("hex");

    return hash;
  } else {
    return false;
  }
};

/** Fonction pour récupérer les infos de l'utilisateur sans mot de passe */
export const userInfos = (user: UserType) => {
  const { id, email, nom, prenom, telephone } = user;
  return { id, email, nom, prenom, telephone };
};

/** Fonction pour filtrer les offres */
export const filterOffers = async (
  filters: FiltersType,
  limitNumber: number,
  pageNumber: number
) => {
  let {
    depart,
    destination,
    departAvant,
    departApres,
    arriveAvant,
    arriveApres,
    minPrixKg,
    maxPrixKg,
    minPoidsDisponible,
    maxPoidsDisponible,
  } = filters;

  let query = OfferModel.find()
    .limit(limitNumber + 1)
    .skip(pageNumber);

  depart && query.where("lieuDepart").equals(depart);
  destination && query.where("lieuArrivee").equals(destination);
  departAvant && query.where("dateDepart").lte(departAvant);
  departApres && query.where("dateDepart").gte(departApres);
  arriveAvant && query.where("dateArrivee").lte(arriveAvant);
  arriveApres && query.where("dateArrivee").gte(arriveApres);
  minPrixKg && query.where("prixKg").gte(minPrixKg);
  maxPrixKg && query.where("prixKg").lte(maxPrixKg);
  minPoidsDisponible && query.where("poidsDispo").gte(minPoidsDisponible);
  maxPoidsDisponible && query.where("poidsDispo").lte(maxPoidsDisponible);

  const result = await query.exec();

  return result;
};
