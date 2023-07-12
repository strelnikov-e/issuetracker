import { Navigate, useOutlet, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import AppNavbarBase from "../components/AppNavbarBase";
import { useEffect } from "react";
import { setAuthToken } from "../utils/SetGlobalAuthToken";

import Breadcrumbs from "../components/Breadcrumbs";

export const PrivateLayout = () => {
  const { user } = useAuth();

  useEffect(() => {
    setAuthToken(user?.token);
  }, []);

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // setUser({...user, firstName: fetchUser.firstName, lastName: fetchUser.lastName});

  return (
    <>
    
        <AppNavbarBase />
    <div className="root-layout">
      <header className="mb-4">
      </header>
      <main>
        <Outlet />
      </main>
    </div>
    </>
  );
};
