import React, { useEffect, useRef, useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import { Link, useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

import { useAuth } from "../hooks/useAuth";
import "../css/Login.css";
import { setAuthToken } from "../utils/SetGlobalAuthToken";
import { InfoCircleFill } from "react-bootstrap-icons";
import { Stack } from "react-bootstrap";

const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/boards";

  // to set focus on error
  const errRef = useRef();

  const [email, setemail] = useState("");
  const [password, setPassword] = useState("");
  const [errMsg, setErrMsg] = useState("");

  // clear error on user input
  useEffect(() => {
    setErrMsg("");
  }, [email, password]);

  function validateForm() {
    return email.length > 0 && password.length > 0;
  }

  // send email and password to backend and recieve token
  const handleSubmit = async (event) => {
    event.preventDefault();
    setAuthToken("")
    const data = { email, password }
    try {
      const response = await axios.post(
        "http://localhost:8080/api/token",
        data,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      // console.log(JSON.stringify(response?.data));
      data["token"] = response?.data;
      setAuthToken(response?.data);

      const getUser = await axios.get("http://localhost:8080/api/users/details");
      const user = {...getUser.data, token: response?.data}
      
      login(user);
      navigate(from, { replace: true });

    } catch (err) {
      // console.log(err)
      if (!err?.response) {
        setErrMsg("No response from server");
      } else if (err.response?.status === 400) {
        setErrMsg("Missing email or Password");
      } else if (err.response?.status === 401) {
        // console.log("error 401")
        setErrMsg("Wrong email or password");
      } else {
        setErrMsg("Login failed");
      }
    }
  };

  return (
    <div className="Login row d-flex justify-content-center align-items-center h-100">
      <Card border="0" style={{ width: "400px" }} className="">
        <Card.Title as={"h2"} className="text-center">
          Welcome
        </Card.Title>

        {errMsg ? (
          <Card.Subtitle
            as={"h6"}
            ref={errRef}
            className="text-danger fw-normal text-center"
          >
            {errMsg}
          </Card.Subtitle>
        ) : (
          <Card.Subtitle
            as={"h6"}
            className="mb-2 text-muted text-center fw-normal"
          >
            Please sign in to get started
          </Card.Subtitle>
        )}
        <Card.Body className="p-0">
          <hr></hr>
          <Form onSubmit={handleSubmit} className="d-grid mb-5">
            <Form.Group size="lg" className="mb-3" controlId="email">
              <Form.Label className="text-muted fw-semibold">
                Email
              </Form.Label>
              <Form.Control
                autoFocus
                type="text"
                value={email}
                autoComplete="off"
                onChange={(e) => setemail(e.target.value)}
                required
              />
            </Form.Group>
            <Form.Group size="lg" controlId="password" className="mb-5">
              <Form.Label className="text-muted fw-semibold">
                Password
              </Form.Label>
              <Form.Control
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </Form.Group>
            <Button
              className="btn-dark fw-semibold"
              block="true"
              size="lg"
              type="submit"
              disabled={!validateForm()}
            >
              Sign In
            </Button>
          </Form>

          <p className="text-center">
            Don't have an account?{" "}
            <span>
              <Link to="/register">Sign up</Link>
            </span>
          </p>
        </Card.Body>
        <Stack direction="horizontal" gap={3} className="px-3 py-5">
        <div className="">
          <InfoCircleFill size={20} color="RebeccaPurple" />
        </div>{" "}
        <div className="text-secondary">
          To test application you can create account OR user one of the following test users:
          <ul className="mt-2">
            <li>ryan@test.com - Administrator</li>
            <li>jared@test.com - Manager (Recommended)</li>
            <li>sandra@test.com - Viewer</li>
          </ul>
            Password for all test users: password
        </div>
      </Stack>
      </Card>

      
    </div>
  );
};

export default Login;
