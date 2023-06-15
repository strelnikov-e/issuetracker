import axios from "axios";

export const setAuthToken = (token) => {
  if (token) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else delete axios.defaults.headers.common["Authorization"];
};

export const isAuthenticated = () => {
  const token = localStorage.getItem("token");
  return token !== null && token !== "";
};
