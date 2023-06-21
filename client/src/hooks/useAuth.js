import { createContext, useContext, useMemo} from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useLocalStorage } from "./useLocalStorage";
import axios from "axios";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useLocalStorage("user", null);
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/boards";

  // call this function when you want to authenticate the user
  const login = async (data) => {
    // setAuthToken(null);
    try {
      const response = await axios.post(
        "http://localhost:8080/api/token",
        data,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      console.log(JSON.stringify(response?.data));
      const accessToken = response?.data;

      setUser(...data, accessToken);
      // setAuthToken(accessToken);
      // localStorage.setItem("email", email);
      // localStorage.setItem("password", password);
      // localStorage.setItem("accessToken", accessToken);

      // setemail("");
      // setPassword("");

      navigate(from, { replace: true });
    } catch (err) {
      if (!err?.response) {
        return "No response from server";
      } else if (err.response?.status === 400) {
        return "Missing email or Password";
      } else if (err.response?.status === 401) {
        return "Wrong email or password";
      } else {
        return "Login failed";
      }
    }


    setUser(data);
    navigate("/boards");
  };

  // call this function to sign out logged in user
  const logout = () => {
    setUser(null);
    navigate("/", { replace: true });
  };

  const value = useMemo(
    () => ({
      user,
      login,
      logout
    }),
    [user]
  );
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  return useContext(AuthContext);
};