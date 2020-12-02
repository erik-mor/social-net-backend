import React, { useEffect, useState, useContext } from "react";
import { UserContext } from "./UserContext";

const Channels = () => {
  const [channels, setChannels] = useState([]);
  const [userName, setUserName] = useContext(UserContext);

  useEffect(() => {
    if (userName) {
      fetch(`http://localhost:8080/messaging/channels/${userName.id}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      })
        .then((res) => {
          if (res.status == 200) {
            return res.json();
          }
        })
        .then((res) => console.log(res));
    }
  }, [userName, channels]);

  return <h1>Messages</h1>;
};

export default Channels;
