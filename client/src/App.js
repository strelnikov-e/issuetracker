import React, { useState } from "react";
import {
  createBrowserRouter,
  Route,
  createRoutesFromElements,
  RouterProvider,
  defer,
  useNavigate,
} from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

import { Button } from "react-bootstrap";
// pages
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import { AuthLayout } from "./layouts/AuthLayout";
import NotFound from "./pages/errors/NotFound";
import { Projects } from "./pages/Projects";
import ProjectError from "./pages/errors/ProjectError";
import ProjectLayout from "./layouts/ProjectLayout";
import ProjectDetails from "./pages/ProjectDetails";
import IssuesLayout from "./layouts/IssuesLayout";
import IssueError from "./pages/errors/IssueError";
import Issues from "./pages/Issues";
import IssueDetails from "./pages/IssueDetails";
import Boards from "./pages/Boards";

import { PublicLayout } from "./layouts/PublicLayout";
import { PrivateLayout } from "./layouts/PrivateLayout";
import SettingsLayout from "./layouts/SettingsLayout";
import { setAuthToken } from "./utils/SetGlobalAuthToken";
import { ProjectAccess } from "./pages/ProjectAccess";
import ProjectDetailsLayout from "./layouts/ProjectDetailsLayout";
import CreateProjectPage from "./pages/CreateProjectPage";
import MyWork from "./pages/MyWork";
import MyWorkLayout from "./layouts/MyWorkLayout";

// ideally this would be an API call to server to get logged in user data

// const GetUserData = () =>
//   new Promise((resolve) =>

//     setTimeout(() => {
//       const user = window.localStorage.getItem("user");
//       resolve(user);
//     }, 1)
//   );

const GetUserData = () => {
  // const [ setUser] = useLocalStorage("user");
  const user = window.localStorage.getItem("user");

  // const fetchUser = async () => {
  //   const responce = await axios.get("http://localhost:8080/api/users/details");
  //   console.log(responce.data);
  //   const data = {
  //     ...JSON.parse(user),
  //     firstName: responce.data.firstName,
  //     lastName: responce.data.lastName,
  //     id: responce.data.id,
  //     company: responce.data.companyName,
  //   };
  //   console.log(data);
  //   console.log(user);
  //   return JSON.stringify( data);
  // };

  // return fetchUser();

  // const fetchUser = useFetchUser();
  // const response = {...fetchUser, "firstName": fetchUser.firstName, "lastName": fetchUser.lastName}
  // if (user.token) {
  // console.log("inside if")
  // const userFetch = axios.get("http://localhost:8080/api/users/details")
  // .then(data => console.log(data.data))
  // }
  return user;
};

export const ProjectContext = React.createContext(null);

export const CreateProject = () => {
  const navigate = useNavigate();
  return (
    <div className="">
      <Button
        valign="middle"
        variant="secondary"
        onClick={() => navigate("/projects/new")}
        className="btn ms-auto justify-content-center mb-4"
      >
        <span className="align-middle fw-semibold">
          Create your first project{" "}
        </span>
      </Button>
    </div>
  );
};

const App = () => {
  const user = JSON.parse(window.localStorage.getItem("user"));

  // console.log(user)

  const proj = user?.currentProject || { id: 0, name: <CreateProject /> };
  const [project, setProject] = useState(proj);
  setAuthToken(user?.token);

  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route
        element={<AuthLayout />}
        loader={() => defer({ userPromise: GetUserData() })}
      >
        {/* public routes */}
        <Route path="/" element={<PublicLayout />}>
          <Route index element={<Home />} />
          <Route path="login" element={<Login />} />
          <Route path="register" element={<Register />} />
        </Route>

        {/* protected routes */}
        {/* <Route element={<ProtectedRoute isAuthenticated={isAuthenticated} />}> */}
        <Route element={<PrivateLayout />}>
          <Route
            path="projects"
            element={<ProjectLayout />}
            errorElement={<ProjectError />}
          >
            <Route index element={<Projects />} />
          </Route>
          <Route
            path="projects/new"
            element={<ProjectDetailsLayout />}
            errorElement={<ProjectError />}
          >
            <Route index element={<CreateProjectPage />} />
          </Route>
          <Route
            path="projects/:id"
            element={<SettingsLayout />}
            errorElement={<ProjectError />}
          >
            <Route index element={<ProjectDetails />} />
            <Route path="access" element={<ProjectAccess />} />
          </Route>
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
          <Route
            path="mywork"
            element={<MyWorkLayout />}
            errorElement={<IssueError />}
          >
            <Route index element={<MyWork />} />
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
      <RouterProvider router={router} />
      <ReactQueryDevtools initialIsOpen={false} />
    </ProjectContext.Provider>
  );
};

export default App;
