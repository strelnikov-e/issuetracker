import React from "react";
import {
  createBrowserRouter,
  Route,
  createRoutesFromElements,
  RouterProvider,
} from "react-router-dom";
import { useState } from "react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
// pages
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import RootLayout from "./layouts/RootLayout";
import NotFound from "./pages/errors/NotFound";
import Projects from "./pages/Projects";
import ProjectError from "./pages/errors/ProjectError";
import ProjectLayout from "./layouts/ProjectLayout";
import ProjectDetails from "./pages/ProjectDetails";
import IssuesLayout from "./layouts/IssuesLayout";
import IssueError from "./pages/errors/IssueError";
import Issues from "./pages/Issues";
import IssueDetails from "./pages/IssueDetails";
import Boards from "./pages/Boards";
import { setAuthToken } from "./utils/SetGlobalAuthToken";
import RequireAuth from "./components/RequireAuth";
import useAuth from "./utils/hooks/useAuth";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 20,
    },
  },
});

export const ProjectContext = React.createContext(null);

function App() {
  const [project, setProject] = useState({
    id: 1,
    name: "Bugtracker",
    ket: "BT",
  });
  const { auth, setAuth } = useAuth({
    username: localStorage.getItem("username"),
    accessToken: localStorage.getItem("accessToken"),
  });

  console.log("App project: ", project);
  console.log("Local storage ", localStorage.getItem("username"));

  //check jwt token
  const token = localStorage.getItem("accessToken");
  if (token) {
    setAuthToken(token);
  }
  console.log("USE AUTH : ", auth);

  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path="/" element={<RootLayout />}>
        {/* public routes */}
        <Route index element={<Home />} />
        <Route path="login" element={<Login />} />
        <Route path="register" element={<Register />} />

        {/* protected routes */}
        {/* <Route element={<ProtectedRoute isAuthenticated={isAuthenticated} />}> */}
        <Route element={<RequireAuth />}>
          <Route
            path="projects"
            element={<ProjectLayout />}
            errorElement={<ProjectError />}
          >
            <Route index element={<Projects />} />
          </Route>
          <Route path="projects/:id" element={<ProjectDetails />} />

          <Route
            path="issues"
            element={<IssuesLayout />}
            errorElement={<IssueError />}
          >
            <Route index element={<Issues />} />
          </Route>
          <Route
            path="boards"
            element={<IssuesLayout />}
            errorElement={<IssueError />}
          >
            <Route index element={<Boards />} />
          </Route>
          <Route path="issues/:id" element={<IssueDetails />} />
        </Route>

        {/* catch all */}
        <Route path="*" element={<NotFound />} />
      </Route>
    )
  );

  return (
    <ProjectContext.Provider
      value={{ currentProject: project, setCurrentProject: setProject }}
    >
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={router} />
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </ProjectContext.Provider>
  );
}

export default App;
