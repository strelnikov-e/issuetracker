import axios from "axios";

export const setAuthToken = (token) => {
  if (token) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    // console.log('AXIOS auth header set.')
  } else {
    delete axios.defaults.headers.common["Authorization"];
    // console.log('AXIOS auth header deleted.')
  }
};
