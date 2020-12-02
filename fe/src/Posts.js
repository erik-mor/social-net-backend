import React, { useEffect, useState } from "react";
import Post from "./Post";

const Posts = () => {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/post/", {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    })
      .then((res) => {
        if (res.status == 200) {
          return res.json();
        }
      })
      .then((res) => setPosts(res));
  }, []);

  if (posts && posts.length) {
    return posts.map((post) => (
      <Post
        user_id={post.user_id}
        created={post.created}
        content={post.content}
        key={post.id}
      />
    ));
  } else {
    return <h1>No posts available</h1>;
  }
};

export default Posts;
