import "dotenv/config";
import express from "express";
import connectDB from "./config/db";
import userRoute from "./routes/user";
import usersRoute from "./routes/users";
import authRoute from "./routes/auth";
import offerRoute from "./routes/offer";
import offersRoute from "./routes/offers";

/**
 * Fonction pour créer le serveur
 */
const createServer = () => {
  const app = express();

  app.get("/", (req, res) => {
    res.send("Server running");
  });

  // Middlewares
  app.use(express.json());

  // Routes de l'application
  app.use("/user", userRoute);
  app.use("/users", usersRoute);
  app.use("/auth", authRoute);
  app.use("/offer", offerRoute);
  app.use("/offers", offersRoute);

  return app;
};

const app = createServer();

const PORT = process.env.PORT || 5000;

app.listen(PORT, async () => {
  try {
    await connectDB();
    console.log(`Node started at port ${PORT}`);
  } catch (error) {
    console.log(error);
  }
});

export default createServer;
