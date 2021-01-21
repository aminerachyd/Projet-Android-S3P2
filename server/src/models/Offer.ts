import mongoose from "mongoose";
const Schema = mongoose.Schema;

const OfferSchema = new Schema({
  user: {
    type: Schema.Types.ObjectId,
    ref: "users",
  },
  lieudepart: {
    type: String,
    required: true,
  },
  lieuarrivee: {
    type: String,
    required: true,
  },
  datedepart: {
    type: Date,
    required: true,
  },
  datearrivee: {
    type: Date,
    required: true,
  },
  prixkg: {
    type: Number,
    required: true,
  },
  poidsdispo: {
    type: Number,
    required: true,
  },
} as const);

const Offer = mongoose.model("offer", OfferSchema);
export default Offer;
