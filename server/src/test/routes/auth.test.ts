import chai, { expect } from "chai";
import chaiHttp from "chai-http";
import createServer from "../../app";

let should = chai.should();

chai.use(chaiHttp);
const app = createServer();

const TEST_TOKEN = process.env.TEST_TOKEN;

describe("Route /auth", () => {
  it("POST : Doit authentifier un utilisateur", function (done) {
    this.timeout(10000);
    chai
      .request(app)
      .post("/auth")
      .send({
        email: "test@test.com",
        password: "test",
      })
      .set({ Accept: "application/json" })
      .end((req, res) => {
        res.status.should.equal(200);
        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");
        expect(res.body.payload)
          .to.be.an.instanceof(Object)
          .and.to.have.property("token");
        done();
      });
  });
  it("GET : Doit vérifier la validité d'un token", (done) => {
    chai
      .request(app)
      .get("/auth")
      .set({ "x-auth-token": TEST_TOKEN })
      .end((req, res) => {
        res.status.should.equal(200);
        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");
        expect(res.body.payload)
          .to.be.an.instanceof(Object)
          .and.to.have.property("id");
        done();
      });
  });
});
