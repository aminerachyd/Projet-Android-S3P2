import crypto from "crypto";
import OfferModel from "../models/Offer";
import UserModel from "../models/User";
import { FiltersType, UserType } from "../types";
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

/**
 * Fonction pour récupérer un utilisateur depuis la base de données
 */
export const isUserValid = async (id: string) => {
  const user = await UserModel.findById(id);

  return { isValid: !!user, user };
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
    .skip(pageNumber)
    .sort({ updatedAt: "desc" });
  // Si un paramètre existe, on filtre avec
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

/**
 * Fonction pour retourner les offres d'un utilisateur donné celon son id avec un boolean qui indique si il y'a davantage de données à récupérer
 */
export const findUserOffers = async (
  id: UserType["_id"],
  limitNumber: number,
  pageNumber: number
) => {
  try {
    const user = await UserModel.findById(id);

    // Le tableau des offres
    let offersIds = user?.offres;

    if (!offersIds) {
      return false;
    }

    // On divise la liste selon notre pagination
    let slicedOffersIds = offersIds.slice(pageNumber, pageNumber + limitNumber);

    // Boolean qui indique si il y'a plus de données à récupérer ou pas (existance d'une page suivante)
    const hasMore = offersIds?.length - 1 === pageNumber + limitNumber;

    let offers: any[] = [];

    for (let i = 0; i < slicedOffersIds.length; i++) {
      let id = slicedOffersIds![i];

      if (!id) {
        break;
      }

      const offer = await OfferModel.findById(id);

      let {
        lieuDepart,
        lieuArrivee,
        dateDepart,
        dateArrivee,
        prixKg,
        poidsDispo,
      } = offer!;

      offers.push({
        id,
        lieuDepart,
        lieuArrivee,
        dateDepart,
        dateArrivee,
        prixKg,
        poidsDispo,
      });
    }

    return { offers, hasMore };
  } catch (error) {
    console.log(error);
    return false;
  }
};

/**
 * Fonction pour valider les dates
 */
export const validerDates = (dateDepart: Date, dateArrivee: Date) => {
  let dateNow = new Date();
  let dateD = new Date(dateDepart);
  let dateA = new Date(dateArrivee);

  return !(
    !dateArrivee ||
    !dateDepart ||
    dateA < dateD ||
    dateA < dateNow ||
    dateD < dateNow
  );
};
