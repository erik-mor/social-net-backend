import React, { useContext } from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import { UserContext } from "./UserContext";

const Navigation = () => {
  const [userName, setUserName] = useContext(UserContext);

  const logout = () => {
    localStorage.removeItem("userName");
  };

  return (
    <Navbar bg="light" expand="lg">
      <Navbar.Brand href="/">Social-net</Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
          {userName && userName.is_manager && (
            <React.Fragment>
              <Nav.Link href="/discover">Discover</Nav.Link>
            </React.Fragment>
          )}
          {userName && (
            <React.Fragment>
              <Nav.Link href="/channels">Messages</Nav.Link>
            </React.Fragment>
          )}
        </Nav>
      </Navbar.Collapse>

      <Nav className="mr-auto">
        {userName ? (
          <React.Fragment>
            <Nav.Link href={`/profile/${userName.id}`}>
              {userName.username}
            </Nav.Link>
            <Nav.Link href="/login" onClick={logout}>
              Logout
            </Nav.Link>
          </React.Fragment>
        ) : (
          <React.Fragment>
            <Nav.Link href="/login">Login</Nav.Link>
            <Nav.Link href="/register">Sign up</Nav.Link>
          </React.Fragment>
        )}
      </Nav>
    </Navbar>
  );
};

export default Navigation;
