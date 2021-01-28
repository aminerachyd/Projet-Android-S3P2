import chai, { expect } from "chai";
import chaiHttp from "chai-http";

let should = chai.should();
chai.use(chaiHttp);

import createServer from "../../app";
const app = createServer();

const TEST_TOKEN = process.env.TEST_TOKEN;
let tempId: string;

describe("Route /user", () => {
  //   before((done) => {
  //     chai
  //       .request(app)
  //       .post("/user")
  //       .send({
  //         email: "temp@temp.com",
  //         nom: "temp",
  //         prenom: "temp",
  //         telephone: "temp",
  //         password: "temp",
  //       })
  //       .set({ Accept: "application/json" })
  //       .end((err, res) => {
  //         tempId = res.body.payload;
  //         done();
  //       });
  //   });

  it("POST : Doit enregistrer un utilisateur", (done) => {
    chai
      .request(app)
      .post("/user")
      .send({
        email: "temp@temp.com",
        nom: "temp",
        prenom: "temp",
        telephone: "temp",
        password: "temp",
      })
      .set({ Accept: "application/json" })
      .end((err, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        tempId = res.body.payload;

        done();
      });
  });

  it("GET : Doit récupérer un seul utilisateur", (done) => {
    chai
      .request(app)
      .get(`/user/${tempId}`)
      .end((err, res) => {
        res.should.have.status(200);

        expect(res.body).to.be.an.instanceof(Object);
        expect(res.body).to.have.property("message");
        expect(res.body).to.have.property("payload");

        expect(res.body.payload).to.be.an.instanceof(Object);
        expect(res.body.payload).to.have.property("id");
        expect(res.body.payload).to.have.property("email");
        expect(res.body.payload).to.have.property("nom");
        expect(res.body.payload).to.have.property("prenom");
        expect(res.body.payload).to.have.property("telephone");

        done();
      });
  });

  it("PUT : Doit modifier un utilisateur", (done) => {
    chai
      .request(app)
      .post("/auth")
      .send({
        email: "temp@temp.com",
        password: "temp",
      })
      .set({ Accept: "application/json" })
      .end((req, res) => {
        let tempToken = res.body.payload.token;

        chai
          .request(app)
          .put(`/user/${tempId}`)
          .set({ Accept: "application/json", "x-auth-token": tempToken })
          .send({
            nom: "temptest",
            prenom: "temptest",
          })
          .end((err, res) => {
            res.should.have.status(200);

            expect(res.body).to.be.an.instanceof(Object);
            expect(res.body).to.have.property("message");
            expect(res.body).to.have.property("payload");

            done();
          });
      });
  });

  it("DELETE : Doit supprimer un utilisateur", (done) => {
    chai
      .request(app)
      .post("/auth")
      .send({
        email: "temp@temp.com",
        password: "temp",
      })
      .set({ Accept: "application/json" })
      .end((req, res) => {
        let tempToken = res.body.payload.token;

        chai
          .request(app)
          .delete(`/user/${tempId}`)
          .set({ "x-auth-token": tempToken })
          .end((err, res) => {
            res.should.have.status(200);

            expect(res.body).to.be.an.instanceof(Object);
            expect(res.body).to.have.property("message");
            expect(res.body).to.have.property("payload");

            done();
          });
      });
  });
});
