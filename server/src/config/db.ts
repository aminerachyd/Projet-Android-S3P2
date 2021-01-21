import mongoose from "mongoose";

const db = process.env.MONGO_URI;

// console.log(db);

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
