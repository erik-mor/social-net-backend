import React, { useState, createContext, useEffect } from "react";

export const UserContext = createContext();

export const UserProvider = (props) => {
  const [userName, setUserName] = useState(null);

  useEffect(() => {
    if (userName == null) {
      let x = localStorage.getItem("userName");
      if (x != null) {
        setUserName(JSON.parse(x));
      }
    } else {
      localStorage.setItem("userName", JSON.stringify(userName));
    }
  }, [userName]);

  return (
    <UserContext.Provider value={[userName, setUserName]}>
      {" "}
      {props.children}{" "}
    </UserContext.Provider>
  );
};
