import mongoose from "mongoose";

const db =
  process.env.NODE_ENV === "dev"
    ? process.env.MONGO_URI_DEV
    : process.env.NODE_ENV === "test"
    ? process.env.MONGO_URI_TEST
    : process.env.MONGO_URI;

const connectDB = async () => {
  try {
    await mongoose.connect(<string>db, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
      useCreateIndex: true,
      useFindAndModify: false,
    });
    console.log("MongoDB connected...");
    return;
  } catch (error) {
    console.error(error.message);
    // Exit process with failure
    process.exit(1);
  }
};

export default connectDB;
