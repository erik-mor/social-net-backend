import React, { useEffect, useState, useContext } from "react";
import Card from "react-bootstrap/Card";
import { UserContext } from "./UserContext";

const Post = (props) => {
  const [userName, setUserName] = useContext(UserContext);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");

  useEffect(() => {
    const id = props.user_id;
    fetch(`http://localhost:8080/user/${id}`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    })
      .then((res) => res.json())
      .then((res) => {
        console.log(res);
        setFirstName(res.firstName);
        setLastName(res.lastName);
      });
  }, []);

  return (
    <div>
      {userName ? (
        <Card style={{ width: "18rem" }}>
          <Card.Body>
            <Card.Title>
              <a href={`profile/${props.user_id}`}>
                {firstName} {lastName}
              </a>
            </Card.Title>
            <Card.Text>{props.content}</Card.Text>
          </Card.Body>
        </Card>
      ) : (
        <h1>No user logged on</h1>
      )}
    </div>
  );
};

export default Post;
