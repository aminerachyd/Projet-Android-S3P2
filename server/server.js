require("dotenv").config();
const express = require("express");
const app = express();
const connectDB = require("./src/config/db");

const PORT = process.env.PORT || 5000;

app.get("/", (req, res) => {
  res.send("Node server working\n");
});

app.use("/user", require("./src/routes/user"));

app.listen(PORT, async () => {
  try {
    await connectDB();
    console.log(`Node started at port ${PORT}`);
  } catch (error) {
    console.log(error);
  }
});
