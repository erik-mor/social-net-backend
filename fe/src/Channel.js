import React, { useState, useContext, useEffect, useRef } from "react";
import "./Channel.css";
import { UserContext } from "./UserContext";

const Channel = ({ match }) => {
  const [messages, setMessages] = useState([]);

  const [formValue, setFormValue] = useState("");
  const dummy = useRef();
  const [userName, setUserName] = useContext(UserContext);

  useEffect(() => {
    fetch(`http://localhost:8080/messaging/messages/${match.params.id}`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    })
      .then((res) => {
        if (res.status == 200) {
          return res.json();
        }
      })
      .then((res) => setMessages(res));
  }, [messages]);

  const sendMessage = (e) => {
    e.preventDefault();
    setFormValue("");
    fetch(`http://localhost:8080/messaging/send`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        sender_id: userName.id,
        channel_id: match.params.id,
        message: formValue,
      }),
    })
      .then((res) => res.text())
      .then((res) => {
        console.log(res);
      });
  };

  const onFormChanged = (e) => {
    e.preventDefault();
    setFormValue(e.target.value);
  };

  return (
    <div className="Chat">
      <main>
        {messages &&
          messages.map((msg) => (
            <ChatMessage
              key={msg.id}
              message={msg.message}
              sender={msg.senderId}
            />
          ))}

        <span ref={dummy}></span>
      </main>

      <form className="chat-form" onSubmit={sendMessage}>
        <input
          className="chat-input"
          value={formValue}
          onChange={onFormChanged}
          placeholder="say something nice"
        />

        <button className="chat-button" type="submit" disabled={!formValue}>
          Submit
        </button>
      </form>
    </div>
  );
};

function ChatMessage(props) {
  const [userName, setUserName] = useContext(UserContext);

  return (
    <>
      {userName && (
        <div
          className={`message ${
            userName.id == props.sender ? "sent" : "received"
          }`}
        >
          {/* <img
            className="chat-img"
            src={"https://api.adorable.io/avatars/23/abott@adorable.png"}
          /> */}
          <p className="chat-message">{props.message}</p>
        </div>
      )}
    </>
  );
}

export default Channel;
