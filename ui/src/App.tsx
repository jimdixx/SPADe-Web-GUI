import React from 'react';
import { useState, useEffect } from 'react';
import axios from 'axios'

const App = () => {

  let [data, setData] = useState(null);
  let [loading, setLoading] = useState(false);

  useEffect(() => {
      setLoading(true);

      axios
          .get("http://localhost:8080/v2/testCall")
          .then((result) => {
              setData(result.data.response);
              setLoading(false);
          })
          .catch((error) => console.log(error));
  }, []);

    if (loading) {
        return <p>Loading...</p>
    }


  return (
      <div className="container">{data}</div>
  );
};


export default App;
