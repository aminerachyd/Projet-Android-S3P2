import chai, { expect } from "chai";
import chaiHttp from "chai-http";

let should = chai.should();
chai.use(chaiHttp);

import createServer from "../../app";
const app = createServer();

const TEST_TOKEN = process.env.TEST_TOKEN;
let tempId: string;

describe("Route /offer", () => {
  it("POST : Doit publier une offre", (done) => {
    let dateDepart = new Date();
    let dateArrivee = dateDepart.setHours(dateDepart.getHours() + 1);

    chai
      .request(app)
      .post("/offer")
      .send({
        lieuDepart: "TEMARA",
        lieuArrivee: "RABAT",
        prixKg: 45,
        dateDepart,
        dateArrivee,
        poidsDispo: 20,
      })
      .set({ Accept: "application/json", "x-auth-token": TEST_TOKEN })
      .end((_, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        tempId = res.body.payload;
        done();
      });
  });

  it("GET : Doit récupérer une seule offre", (done) => {
    chai
      .request(app)
      .get(`/offer/${tempId}`)
      .end((_, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        expect(res.body.payload).to.be.an.instanceof(Object);
        expect(res.body.payload).to.have.property("id");
        expect(res.body.payload).to.have.property("user");
        expect(res.body.payload).to.have.property("lieuDepart");
        expect(res.body.payload).to.have.property("lieuArrivee");
        expect(res.body.payload).to.have.property("dateDepart");
        expect(res.body.payload).to.have.property("dateArrivee");
        expect(res.body.payload).to.have.property("prixKg");
        expect(res.body.payload).to.have.property("poidsDispo");

        done();
      });
  });

  it("PUT : Doit modifier une offre", (done) => {
    chai
      .request(app)
      .put(`/offer/${tempId}`)
      .set({ Accept: "application/json" })
      .set({ "x-auth-token": TEST_TOKEN })
      .send({
        lieuDepart: "CASABLANCA",
        poidsDispo: 30,
      })
      .end((_, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        done();
      });
  });

  it("DELETE : Doit supprimer une offre", function (done) {
    this.timeout(10000);
    chai
      .request(app)
      .delete(`/offer/${tempId}`)
      .set({ "x-auth-token": TEST_TOKEN })
      .end((_, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        done();
      });
  });
});
