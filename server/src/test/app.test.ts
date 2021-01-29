import chai from "chai";
import chaiHttp from "chai-http";

chai.use(chaiHttp);

import createServer from "../app";
const app = createServer();

describe("Route /", () => {
  it("GET : Le serveur doit se lance correctement", (done) => {
    chai
      .request(app)
      .get("/")
      .end((_, res) => {
        res.should.have.status(200);
        res.body.should.be.a("object");
        done();
      });
  });
});
