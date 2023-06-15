import { useLocation, Navigate, Outlet } from "react-router-dom";
import useAuth from "../utils/hooks/useAuth";

const RequireAuth = () => {
  const { auth } = useAuth();
  const location = useLocation();

  return auth?.accessToken ? (
    // auth?.roles?.find(role => allowedRoles?.includes(role))
    //         ? <Outlet />
    //         : auth?.user
    //             ? <Navigate to="/unauthorized" state={{ from: location }} replace />
    //             : <Navigate to="/login" state={{ from: location }} replace />
    // );
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default RequireAuth;
