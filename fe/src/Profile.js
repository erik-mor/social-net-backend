import React, { useEffect, useState, useContext } from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import profilePicture from "./static/profile-image.jpeg";
import { UserContext } from "./UserContext";
import { Redirect } from "react-router-dom";

const Profile = ({ match }) => {
  const [user, setUser] = useState({});
  const [userName, setUserName] = useContext(UserContext);
  const [isFollowing, setIsFollowing] = useState(false);
  const [isMe, setIsMe] = useState(true);
  const [redirect, setRedirect] = useState(false);
  const [channelId, setChannelId] = useState("");

  useEffect(() => {
    let id = match.params.id;
    if (userName !== null) {
      if (userName.id != id) {
        setIsMe(false);
        fetch(`http://localhost:8080/user/following/`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            profile_id: id,
            user_id: userName.id,
          }),
        })
          .then((res) => res.json())
          .then((res) => {
            setIsFollowing(res.success);
          });
      }
      fetch(`http://localhost:8080/user/${id}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      })
        .then((res) => res.json())
        .then((res) => {
          setUser(res);
        });
    }
  }, [userName]);

  const handleFollow = () => {
    setIsFollowing(true);
    fetch(`http://localhost:8080/user/follow`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        follower_id: userName.id,
        following_id: user.id,
      }),
    })
      .then((res) => res.json())
      .then((res) => {
        console.log(res);
      });
  };

  const handleContact = () => {
    fetch(`http://localhost:8080/messaging/contact`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        user_id: userName.id,
        contacted_user_id: user.id,
      }),
    })
      .then((res) => res.text())
      .then((res) => {
        setChannelId(res);
        setRedirect(true);
      });
  };

  if (redirect) {
    return <Redirect to={`/channels/${channelId}`} />;
  } else {
    return (
      <React.Fragment>
        {userName ? (
          <Card style={{ width: "18rem" }}>
            <Card.Img variant="top" src={profilePicture} />
            <Card.Body>
              <Card.Title>
                {user.firstName} {user.lastName}
              </Card.Title>
              <Card.Text>{user.is_manager ? "Manager" : "Player"}</Card.Text>
              <Button
                className="primary"
                hidden={isMe}
                onClick={handleFollow}
                disabled={isFollowing}
              >
                {isFollowing ? "Already following" : "Follow me"}
              </Button>
              <Button onClick={handleContact} hidden={isMe}>
                Contact me
              </Button>
            </Card.Body>
          </Card>
        ) : (
          <h1>No user logged on</h1>
        )}
      </React.Fragment>
    );
  }
};

export default Profile;
