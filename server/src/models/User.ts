import mongoose from "mongoose";
const Schema = mongoose.Schema;

interface IUserSchema extends mongoose.Model<any> {
  email: string;
  nom: string;
  prenom: string;
  telephone: string;
  password: string;
}

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

const User: mongoose.Model<
  mongoose.Document<IUserSchema | any>
> = mongoose.model("user", UserSchema);

export default User;
