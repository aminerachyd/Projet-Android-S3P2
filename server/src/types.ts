export type User = {
  id: string | "_id";
  nom: string;
  email: string;
  prenom: string;
  telephone: string;
  password: string;
};

export type DecodedJWT = {
  user: object;
  iat: number;
  exp: number;
};