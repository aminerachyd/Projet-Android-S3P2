import { Document } from "mongoose";

export type UserType = Document<any> & {
  id?: string | "_id";
  nom: string;
  email: string;
  prenom: string;
  telephone: string;
  password?: string;
};

export type OfferType = Document<any> & {
  id?: string | "_id";
  user: UserType;
  lieuDepart: string;
  lieuArrivee: string;
  dateDepart: Date;
  dateArrivee: Date;
  prixKg: number;
  poidsDispo: number;
};

export type DecodedJWT = {
  user: {
    id: string;
    jwtVersion: string;
  };
  iat: number;
  exp: number;
};

export type FiltersType = {
  depart?: string;
  destination?: string;
  departAvant?: number;
  departApres?: number;
  arriveAvant?: number;
  arriveApres?: number;
  minPrixKg?: number;
  maxPrixKg?: number;
  minPoidsDisponible?: number;
  maxPoidsDisponible?: number;
};
