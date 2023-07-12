import React, { useEffect, useState, useRef, isValidElement } from "react";
import { Link } from "react-router-dom";

import axios from "axios";

import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import InputGroup from "react-bootstrap/InputGroup";

import "../css/Login.css";

const EMAIL_REGEX = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,50}$/;
const PWD_REGEX = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;

export default function Login() {
  // to set focus on error
  const errRef = useRef();

  const [firstName, setFirstName] = useState("");
  const [validFirstName, setValidFirstName] = useState("");

  const [lastName, setLastName] = useState("");
  const [validLastName, setValidLastName] = useState("");

  const [company, setCompany] = useState("");

  const [email, setEmail] = useState("");
  const [validEmail, setValidEmail] = useState(false);

  const [pwd, setPwd] = useState("");
  const [validPwd, setValidPwd] = useState(false);
  const [pwdFocus, setPwdFocus] = useState(false);

  const [matchPwd, setMatchPwd] = useState("");
  const [validMatchPwd, setValidMatchPwd] = useState(false);
  const [matchPwdFocus, setMatchPwdFocus] = useState(false);

  const [errMsg, setErrMsg] = useState("");

  useEffect(() => {
    const result = EMAIL_REGEX.test(email);
    setValidEmail(result);
  }, [email]);

  useEffect(() => {
    setValidFirstName(firstName.length > 0);
  }, [firstName]);

  useEffect(() => {
    setValidLastName(lastName.length > 0);
  }, [lastName]);

  useEffect(() => {
    const result = PWD_REGEX.test(pwd);
    setValidPwd(result);
    const match = pwd === matchPwd;
    setValidMatchPwd(match);
  }, [pwd, matchPwd]);

  useEffect(() => {
    setErrMsg("");
  }, [firstName, lastName, email, pwd, matchPwd]);

  function validateForm() {
    const v1 = firstName.length > 0;
    const v2 = lastName.length > 0;
    const v3 = EMAIL_REGEX.test(email);
    const v4 = PWD_REGEX.test(pwd);
    const v5 = matchPwd === pwd;
    return v1 && v2 && v3 && v4 && v5;
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    // if button enabled with JS hack
    const v1 = firstName.length > 0;
    const v2 = lastName.length > 0;
    const v3 = EMAIL_REGEX.test(email);
    const v4 = PWD_REGEX.test(pwd);
    const v5 = matchPwd === pwd;
    if (!v1 || !v2 || !v3 || !v4 || !v5) {
      setErrMsg("Invalid entry");
      return;
    }
    try {
      const response = await axios.post(
        "http://localhost:8080/api/users",
        JSON.stringify({ email, password: pwd, firstName, lastName, company }),
        {
          headers: { "Content-Type": "application/json" },
          
        }
      );
      setFirstName('');
      setLastName('');
      setCompany('');
      setEmail('');
      setPwd('')
      setMatchPwd('');
      window.location.href = "/login";
    } catch (err) {
      if (!err?.response) {
        setErrMsg("No response from server");
      } else if (err.response?.status === 409) {
        setErrMsg("Email already registered");
      } else {
        setErrMsg("Registration failed");
      }
    }
  };

  return (
    <div className="row d-flex justify-content-center align-items-center h-100">
      <Card border="0" style={{ width: "600px" }} className="">
        <Card.Title as={"h3"} className="text-center fw-normal">
          Create new account
        </Card.Title>
        {errMsg && (
          <Card.Subtitle
            as={"h6"}
            ref={errRef}
            className="text-danger fw-normal text-center"
          >
            {errMsg}
          </Card.Subtitle>
        )}
        <Card.Body className="">
          <hr></hr>
          <Form onSubmit={handleSubmit} className="row mb-4">
            <Form.Group
              size="lg"
              className="mb-3 col-12 col-sm-6"
              controlId="First name"
            >
              <Form.Label className="text-muted">First Name</Form.Label>
              <Form.Control
                className={validFirstName && "is-valid"}
                autoFocus
                type="text"
                value={firstName}
                autoComplete="off"
                onChange={(e) => setFirstName(e.target.value)}
                required
              />
            </Form.Group>
            <Form.Group
              size="lg"
              className="mb-3 col-12 col-sm-6"
              controlId="Last name"
            >
              <Form.Label className="text-muted">Last name</Form.Label>
              <Form.Control
                className={validLastName && "is-valid"}
                type="text"
                value={lastName}
                autoComplete="off"
                onChange={(e) => setLastName(e.target.value)}
                required
              />
            </Form.Group>
            <Form.Group size="lg" className="mb-3" controlId="Company">
              <Form.Label className="text-muted">Company</Form.Label>
              <Form.Control
                type="text"
                value={company}
                autoComplete="off"
                onChange={(e) => setCompany(e.target.value)}
              />
            </Form.Group>
            <Form.Group size="lg" className="mb-3" controlId="Email">
              <Form.Label className="text-muted">Email</Form.Label>
              <InputGroup hasValidation>
                <Form.Control
                  className={
                    email.length > 0
                      ? validEmail
                        ? "is-valid"
                        : "is-invalid"
                      : ""
                  }
                  type="text"
                  placeholder="name@company.com"
                  value={email}
                  autoComplete="off"
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
                <Form.Control.Feedback type="invalid">
                  Please type a valid email.
                </Form.Control.Feedback>
              </InputGroup>
            </Form.Group>
            <Form.Group size="lg" controlId="pwd" className="mb-3">
              <Form.Label className="text-muted">Password</Form.Label>
              <InputGroup hasValidation>
                <Form.Control
                  className={
                    pwd.length > 0 ? (validPwd ? "is-valid" : "is-invalid") : ""
                  }
                  type="password"
                  value={pwd}
                  onChange={(e) => setPwd(e.target.value)}
                  required
                />
                <Form.Control.Feedback type="invalid">
                  6 to 24 characters.
                  <br />
                  Must include uppercase and lowercase letters and a number.
                </Form.Control.Feedback>
              </InputGroup>
            </Form.Group>

            <Form.Group size="lg" controlId="matchPwd" className="mb-5">
              <Form.Label className="text-muted">Confirm password</Form.Label>
              <InputGroup hasValidation>
                <Form.Control
                  className={
                    matchPwd.length > 0
                      ? validMatchPwd
                        ? "is-valid"
                        : "is-invalid"
                      : ""
                  }
                  type="password"
                  value={matchPwd}
                  onChange={(e) => setMatchPwd(e.target.value)}
                  required
                />
                <Form.Control.Feedback type="invalid">
                  Passwords should match.
                </Form.Control.Feedback>
              </InputGroup>
            </Form.Group>
            <Button
              className="btn-dark fw-semibold"
              block="true"
              size="lg"
              type="submit"
              disabled={!validateForm()}
            >
              Sign up
            </Button>
          </Form>
          <p className="">
            Already registered?{" "}
            <span>
              <Link to="/login">Sign in</Link>
            </span>
          </p>
        </Card.Body>
      </Card>
    </div>
  );
}
