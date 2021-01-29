import chai, { expect } from "chai";
import chaiHttp from "chai-http";

chai.use(chaiHttp);

import createServer from "../../app";
const app = createServer();

describe("Route /users", () => {
  it("GET : Doit récupérer tout les utilisateurs", function (done) {
    this.timeout(10000);
    chai
      .request(app)
      .get("/users")
      .end((_, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        expect(res.body.payload).to.be.an.instanceof(Array);
        expect(res.body.payload[0]).to.have.property("id");
        expect(res.body.payload[0]).to.have.property("email");
        expect(res.body.payload[0]).to.have.property("nom");
        expect(res.body.payload[0]).to.have.property("prenom");
        expect(res.body.payload[0]).to.have.property("telephone");

        done();
      });
  });
});
