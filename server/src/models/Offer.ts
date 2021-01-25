import mongoose from "mongoose";
import { OfferType } from "../types";
const Schema = mongoose.Schema;

// TODO Add addedAt, updatedAt
const OfferSchema = new Schema({
  user: {
    type: Schema.Types.ObjectId,
    ref: "users",
  },
  lieuDepart: {
    type: String,
    required: true,
  },
  lieuArrivee: {
    type: String,
    required: true,
  },
  dateDepart: {
    type: Date,
    required: true,
  },
  dateArrivee: {
    type: Date,
    required: true,
  },
  prixKg: {
    type: Number,
    required: true,
  },
  poidsDispo: {
    type: Number,
    required: true,
  },
} as const);

const Offer = mongoose.model<OfferType>("offer", OfferSchema);
export default Offer;
