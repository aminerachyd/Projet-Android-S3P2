const express = require("express");
const app = express();

const PORT = 5000;

app.get("/", (req, res) => {
  res.send("Node server working");
});

app.listen(PORT, () => console.log(`Node started at port ${PORT}`));
