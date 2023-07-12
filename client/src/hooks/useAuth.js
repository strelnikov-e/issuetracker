import { createContext, useContext, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { useLocalStorage } from "./useLocalStorage";
import axios from "axios";
import { setAuthToken } from "../utils/SetGlobalAuthToken";
import { ProjectContext } from "../App";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useLocalStorage("user", null);
  const navigate = useNavigate();
  const { setCurrentProject } = useContext(ProjectContext);

  // call this function when you want to authenticate the user
  const login = async (data) => {
    // setAuthToken(null);
    setUser(data);
    setAuthToken(data.token);

    // after succesful login - fetch user data and set the project user working on
    const response = await axios.get(
      "http://localhost:8080/api/users/details",
      data,
      {
        headers: { "Content-Type": "application/json" },
      }
    );
    data["currentProject"] = response?.data?.currentProject;
    setUser(data);
    setCurrentProject(response?.data?.currentProject);
  };

  // call this function to sign out logged in user
  const logout = () => {
    setUser(null);
    setAuthToken("");
    setCurrentProject({id: 0})
    navigate("/", { replace: true });
  };

  const value = useMemo(
    () => ({
      user,
      login,
      logout,
    }),
    [user]
  );
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  return useContext(AuthContext);
};
