import React, { useState, useEffect, useRef } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

const vusername = (value) => {
  if (value.length < 3 || value.length > 20) {
    return (
      <div className="alert alert-danger" role="alert">
        The username must be between 3 and 20 characters.
      </div>
    );
  }
};

const vpassword = (value) => {
  if (value.length < 6 || value.length > 40) {
    return (
      <div className="alert alert-danger" role="alert">
        The password must be between 6 and 40 characters.
      </div>
    );
  }
};

const Register = () => {
  const form = useRef();

  const [username, setUsername] = useState("");
  const [first_name, setFirstName] = useState("");
  const [last_name, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [is_manager, setIsManager] = useState("");
  const [successful, setSuccessful] = useState(false);
  const [location, setLocation] = useState(false);

  const onChangeUsername = (e) => {
    const username = e.target.value;
    setUsername(username);
  };

  const onChangeFirstName = (e) => {
    const firstName = e.target.value;
    setFirstName(firstName);
  };

  const onChangeLastName = (e) => {
    const lastName = e.target.value;
    setLastName(lastName);
  };

  const onChangeEmail = (e) => {
    const email = e.target.value;
    setEmail(email);
  };

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };

  const onChangeIsManager = (e) => {
    const isManager = e.target.checked;
    setIsManager(isManager);
  };

  const onChangeLocation = (e) => {};

  const callback = (position) => {
    console.log(position);
    callAPI(position.coords.longitude, position.coords.latitude);
  };

  const onError = () => {
    callAPI(null, null);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (navigator.geolocation) {
      console.log("Here");
      navigator.geolocation.getCurrentPosition(callback, onError);
    } else {
      console.log("Not supported operation");
    }
  };

  const callAPI = (long, lat) => {
    console.log(long, lat);
    let user = {
      username,
      first_name,
      last_name,
      email,
      password,
      is_manager,
      long,
      lat,
    };

    console.log(user);

    fetch(`http://localhost:8080/user/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
    })
      .then((res) => res.json())
      .then((res) => {
        console.log(res);
      });
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group controlId="formBasicEmail">
        <Form.Label>Enter username</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter username"
          value={username}
          onChange={onChangeUsername}
        />
      </Form.Group>

      <Form.Group controlId="firstName">
        <Form.Label>Enter first name</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter first name"
          value={first_name}
          onChange={onChangeFirstName}
        />
      </Form.Group>

      <Form.Group controlId="lastName">
        <Form.Label>Enter last name</Form.Label>
        <Form.Control
          type="text"
          placeholder="Enter last name"
          value={last_name}
          onChange={onChangeLastName}
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
      <Form.Group controlId="email">
        <Form.Label>Email</Form.Label>
        <Form.Control
          type="email"
          placeholder="Email"
          value={email}
          onChange={onChangeEmail}
        />
        <Form.Text className="text-muted">
          We'll never share your email with anyone else.
        </Form.Text>
      </Form.Group>

      <Form.Group controlId="formBasicCheckbox">
        <Form.Check
          type="checkbox"
          label="Are you a manager"
          checked={is_manager}
          onChange={onChangeIsManager}
        />
      </Form.Group>
      <Form.Group controlId="location">
        <Form.Check
          type="checkbox"
          label="Accept using your location"
          checked={location}
          onChange={onChangeLocation}
        />
        <Form.Text className="text-muted">
          Withou your location we will not be able to include you in
          discoveries.
        </Form.Text>
      </Form.Group>

      <Button variant="primary" type="submit">
        Submit
      </Button>
    </Form>
  );
};

export default Register;
