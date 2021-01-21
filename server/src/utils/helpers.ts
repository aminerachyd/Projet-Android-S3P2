import crypto from "crypto";
const HASH_SECRET = process.env.HASH_SECRET;

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
