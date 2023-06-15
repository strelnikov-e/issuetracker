import React, { useEffect, useRef, useState } from "react";
import axios from "axios";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import { Link, useLocation, useNavigate } from "react-router-dom";

import { setAuthToken } from "../utils/SetGlobalAuthToken";
import useAuth from "../utils/hooks/useAuth";
import "../css/Login.css";

const Login = () => {
  const { setAuth } = useAuth();

  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/boards";

  // to set focus on error
  const errRef = useRef();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errMsg, setErrMsg] = useState("");

  // clear error on user input
  useEffect(() => {
    setErrMsg("");
  }, [username, password]);

  function validateForm() {
    return username.length > 0 && password.length > 0;
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    setAuthToken(null);
    try {
      const response = await axios.post(
        "http://localhost:8080/api/token",
        { username, password },
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      console.log(JSON.stringify(response?.data));
      const accessToken = response?.data;
      // implement roles
      const roles = null;
      setAuth({ username, password, accessToken });
      setAuthToken(accessToken);
      localStorage.setItem("username", username);
      localStorage.setItem("password", password);
      localStorage.setItem("accessToken", accessToken);

      setUsername("");
      setPassword("");

      navigate(from, { replace: true });
    } catch (err) {
      if (!err?.response) {
        setErrMsg("No response from server");
      } else if (err.response?.status === 400) {
        setErrMsg("Missing Username or Password");
      } else if (err.response?.status === 401) {
        setErrMsg("Wrong username or password");
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
            <Form.Group size="lg" className="mb-3" controlId="username">
              <Form.Label className="text-muted fw-semibold">
                Username
              </Form.Label>
              <Form.Control
                autoFocus
                type="text"
                value={username}
                autoComplete="off"
                onChange={(e) => setUsername(e.target.value)}
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
      </Card>
    </div>
  );
};

export default Login;
