import mongoose, { Schema } from "mongoose";
import { UserType } from "src/types";

const UserSchema = new Schema(
  {
    email: {
      type: String,
      required: true,
      unique: true,
    },
    nom: {
      type: String,
      required: true,
    },
    prenom: {
      type: String,
      required: true,
    },
    offres: [
      {
        type: Schema.Types.ObjectId,
        ref: "users",
      },
    ],
    telephone: {
      type: String,
      required: true,
    },
    password: {
      type: String,
      required: true,
    },
  } as const,
  { timestamps: true }
);

const User = mongoose.model<UserType>("user", UserSchema);

export default User;
