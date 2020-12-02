import React, { useContext } from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Posts from "./Posts";
import Profile from "./Profile";
import Register from "./Register";
import Login from "./Login";
import { UserProvider } from "./UserContext";
import Navigation from "./Navigation";
import Discover from "./Discover";
import Channels from "./Channels";
import Channel from "./Channel";

function App() {
  return (
    <Router>
      <UserProvider>
        <div className="App">
          <Navigation />
          <div className="container">
            <Switch>
              <Route
                path={["/", "/home"]}
                exact
                render={(props) => (
                  <React.Fragment>
                    <Posts />
                  </React.Fragment>
                )}
              />
              <Route exact path="/login" component={Login} />
              <Route exact path="/register" component={Register} />

              <Route exact path="/profile/:id" component={Profile} />
              <Route exact path="/discover" component={Discover} />
              <Route exact path="/channels" component={Channels} />
              <Route exact path="/channels/:id" component={Channel} />
            </Switch>
          </div>
        </div>
      </UserProvider>
    </Router>
  );
}

export default App;
