import { Document } from "mongoose";

export type User = (Document<any> | null) & {
  id: string | "_id";
  nom: string;
  email: string;
  prenom: string;
  telephone: string;
  password: string;
};

export type DecodedJWT = {
  user: {
    id: string;
    jwtVersion: string;
  };
  iat: number;
  exp: number;
};
