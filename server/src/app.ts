import "dotenv/config";
import express from "express";
import connectDB from "./config/db";

// Routes de l'application
import userRoute from "./routes/user";
import usersRoute from "./routes/users";

const app = express();

// Middlewares
app.use(express.json());

app.use("/user", userRoute);
app.use("/users", usersRoute);

const PORT = process.env.PORT || 5000;

app.listen(PORT, async () => {
  try {
    await connectDB();
    console.log(`Node started at port ${PORT}`);
  } catch (error) {
    console.log(error);
  }
});
