import crypto from "crypto";
const HASH_SECRET = process.env.HASH_SECRET;

// Fonction pour hasher un mot de passe
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

// Fonction pour récupérer les infos de l'utilisateur sans mot de passe
export const userInfos = (user: any) => {
  const { id, email, nom, prenom, telephone, ...rest } = user;
  return { id, email, nom, prenom, telephone };
};
