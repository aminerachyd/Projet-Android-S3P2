import mongoose, { Schema } from "mongoose";
import { UserType } from "src/types";

const UserSchema = new Schema({
  email: {
    type: String,
    required: true,
  },
  nom: {
    type: String,
    required: true,
  },
  prenom: {
    type: String,
    required: true,
  },
  telephone: {
    type: String,
    required: true,
  },
  password: {
    type: String,
    required: true,
  },
});

const User = mongoose.model<UserType>("user", UserSchema);

export default User;
