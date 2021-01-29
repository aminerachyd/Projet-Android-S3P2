import chai, { expect } from "chai";
import chaiHttp from "chai-http";

let should = chai.should();
chai.use(chaiHttp);

import createServer from "../../app";
const app = createServer();

describe("Route /offers", () => {
  it("GET : Doit récupérer toutes les offres", function (done) {
    this.timeout(10000);
    chai
      .request(app)
      .get("/offers")
      .end((_, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        expect(res.body.payload)
          .to.be.an.instanceof(Object)
          .and.to.have.property("hasMore");
        expect(res.body.payload).to.have.property("offers");
        expect(res.body.payload).to.have.property("page");
        expect(res.body.payload).to.have.property("limit");

        expect(res.body.payload.offers).to.be.an.instanceof(Array);

        done();
      });
  });

  it("GET : Doit récupérer toutes les offres avec pagination et limite", function (done) {
    this.timeout(10000);
    let limit = 2,
      page = 4;

    chai
      .request(app)
      .get(`/offers?limit=${limit}&page=${page}`)
      .end((_, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        expect(res.body.payload)
          .to.be.an.instanceof(Object)
          .and.to.have.property("hasMore");
        expect(res.body.payload).to.have.property("offers");
        expect(res.body.payload).to.have.property("page");
        expect(res.body.payload).to.have.property("limit");

        expect(res.body.payload.offers).to.be.an.instanceof(Array);

        res.body.payload.page.should.equal(page);
        res.body.payload.limit.should.equal(limit);

        done();
      });
  });

  it("POST : Doit récupérer toutes les offres avec un filtre", function (done) {
    this.timeout(10000);
    chai
      .request(app)
      .post("/offers")
      .set("Accept", "application/json")
      .send({
        lieuDepart: "Tanger",
        dateDepart: "2020-01-26 10:00:00",
        poidsDispo: 20,
      })
      .end((_, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        expect(res.body.payload)
          .to.be.an.instanceof(Object)
          .and.to.have.property("hasMore");
        expect(res.body.payload).to.have.property("offers");
        expect(res.body.payload).to.have.property("page");
        expect(res.body.payload).to.have.property("limit");

        expect(res.body.payload.offers).to.be.an.instanceof(Array);

        done();
      });
  });
});
