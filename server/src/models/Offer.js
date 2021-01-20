const mongoose = require("mongoose");
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
});

module.exports = Post = mongoose.model("offer", OfferSchema);
