import React, { useState, useContext } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { Redirect } from "react-router-dom";
import { UserContext } from "./UserContext";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [id, setId] = useState();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName, setUserName] = useContext(UserContext);

  const onChangeUsername = (e) => {
    const username = e.target.value;
    setUsername(username);
  };

  const onChangePassword = (e) => {
    const pass = e.target.value;
    setPassword(pass);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    let user = {
      username,
      password,
    };

    fetch(`http://localhost:8080/user/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
    })
      .then((res) => res.json())
      .then((res) => {
        console.log(res);
        if (res.success) {
          setId(res.id);
          let userToSave = {
            id: res.id,
            username: username,
            is_manager: res.is_manager,
          };
          setUserName(userToSave);
          setIsLoggedIn(true);
        }
      });
  };

  if (isLoggedIn) {
    return <Redirect to={`/profile/${id}`} />;
  } else {
    return (
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="username">
          <Form.Label>Enter username</Form.Label>
          <Form.Control
            type="text"
            placeholder="username"
            value={username}
            onChange={onChangeUsername}
          />
        </Form.Group>

        <Form.Group controlId="formBasicPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            value={password}
            onChange={onChangePassword}
          />
        </Form.Group>
        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
    );
  }
};

export default Login;
