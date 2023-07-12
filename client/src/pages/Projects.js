import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";

import Table from "react-bootstrap/Table";
import { Link } from "react-router-dom";
import Button from "react-bootstrap/esm/Button";
import Stack from "react-bootstrap/Stack";
import { Dropdown } from "react-bootstrap";
import DropdownButton from "react-bootstrap/DropdownButton";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Popover from "react-bootstrap/Popover";

import {
  CaretRightFill,
  ChevronLeft,
  ChevronRight,
  InfoCircleFill,
  ThreeDots,
} from "react-bootstrap-icons";
import { CaretLeftFill } from "react-bootstrap-icons";

import { useFetchProjects } from "../utils/Repositories";

import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { ProjectContext } from "../App";
import { useAuth } from "../hooks/useAuth";
import { UserBadgeLarge, UserBadge } from "../components/UserBadge";
import { useLocalStorage } from "../hooks/useLocalStorage";

export const Projects = () => {
  const [user, setUser] = useLocalStorage("user", null);
  const navigate = useNavigate();
  const { logout } = useAuth();
  // const [user, setUser] = useLocalStorage("user");
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const { currentProject, setCurrentProject } = useContext(ProjectContext);
  const { data, isLoading, error } = useFetchProjects(page, size);
  const queryClient = useQueryClient();

  // we use the meta data returned in the response to disable
  // the "Next" button below
  const meta = data?.page || {};

  const handleChooseProject = (project) => {
    setCurrentProject(project);
  };

  const remove = async (id) => {
    await axios.delete(`/api/projects/${id}`);
    // TO MAKE OPTIMISTIC UPDATE:
    // let updatedProjects = [...projects].filter((i) => i.id !== id);
    // setProjects(updatedProjects);
    const response = await axios.get(
      "http://localhost:8080/api/users/details",
      {
        headers: { "Content-Type": "application/json" },
      }
    );
    setUser({ ...user, currentProject: response.data.currentProject });
    setCurrentProject(response?.data?.currentProject || { id: 0 });

    queryClient.invalidateQueries({ queryKey: ["projects", page, size] });
  };

  useEffect(() => {
    queryClient.invalidateQueries({ queryKey: ["projects", page, size] });
  }, []);

  // TO MAKE: proper loading state
  if (isLoading) {
    return <p>Loading...</p>;
  }
  // TO MAKE: proper fault message
  if (error) {
    if (error.response?.status === 401) {
      logout();
      // setUser(null)
      return navigate("/login", { replace: true });
    } else {
      return <p>An error occured: {error.message}</p>;
    }
  }

  const projectList = data._embedded?.projectModelList?.map((project) => {
    const id = project.id;
    const name = project.name;
    // console.log(project);
    const managersPopover = (
      <Popover id="managersPopover">
        <Popover.Header as="h3">Project Managers</Popover.Header>
        <Popover.Body>
          <ul>
            {project.managers.map((manager) => (
              <div key={manager.id} className="mt-2">
                <UserBadge user={manager} />
              </div>
            ))}
          </ul>
        </Popover.Body>
      </Popover>
    );

    return (
      <tr className="fs-5" key={project.id}>
        <td className="fs-5 text-muted" valign="middle">
          {project?.key}
        </td>
        <td as={Link} valign="middle">
          <Link
            onClick={() => handleChooseProject(project)}
            to={{
              pathname: `/boards`,
            }}
            state={project}
            className="fs-5 link-dark link-offset-3 text-decoration-none"
          >
            {project.name}
          </Link>
        </td>
        <td valign="middle" className="fs-5">
          <OverlayTrigger
            trigger="focus"
            placement="right"
            overlay={managersPopover}
          >
            <Button variant="outline">
              <span>
                <UserBadgeLarge
                  user={
                    project.managers[0] || {
                      id: 0,
                      firstName: "",
                      lastName: "",
                    }
                  }
                />
              </span>
              <span valign="top" className="ms-2">
                {Array.from(project.managers).length > 1 && (
                  <CaretRightFill size={14} />
                )}
              </span>
            </Button>
          </OverlayTrigger>
        </td>
        <td valign="middle" className="fs-5">
          {project.startDate}
        </td>
        <td valign="middle">
          <DropdownButton
            variant="outline-secondary border-0"
            className="border-0"
            title=<ThreeDots size={20} />
          >
            <Dropdown>
              <Dropdown.Item
                as={Link}
                to={"/projects/" + project.id}
                className={!project._links.update && "disabled"}
              >
                Edit
              </Dropdown.Item>
              <Dropdown.Item
                onClick={() => remove(project.id)}
                className={!project._links.delete && "disabled"}
              >
                Remove
              </Dropdown.Item>
            </Dropdown>
          </DropdownButton>
        </td>
      </tr>
    );
  });

  return (
    <>
      {" "}
      <Stack direction="horizontal" className="px-2 mb-5">
        <div className="p-3">
          <InfoCircleFill size={20} color="RebeccaPurple" />
        </div>
        {!data?._embedded?.projectModelList ||
        Array.from(data._embedded.projectModelList).length === 0 ? (
          <div>
            You don't have any project yet. To start you work create new project
            by pressing "Create" button or ask your collegue to invite you to
            his project.
          </div>
        ) : currentProject === null || currentProject.id === 0 ? (
          <div>Please choose a project to stark work.</div>
        ) : (
          <div>
            On this page can see and edit all the project you have access to.
          </div>
        )}
      </Stack>
      <Table responsive hover className="text-secondary overflow">
        <thead>
          <tr>
            <th className="fw-semibold" width="80px">
              Key
            </th>
            <th className="fw-semibold">Name</th>
            <th className="fw-semibold" width="250">
              Manager
            </th>
            <th className="fw-semibold" width="150">
              Date Opened
            </th>
            <th className="fw-semibold" width="100"></th>
          </tr>
        </thead>
        <tbody>{projectList}</tbody>
      </Table>
      <Stack direction="horizontal" gap={2}>
        <div className="me-auto">
          <Button
            variant=""
            className="border-0"
            onClick={() => setPage(page - 1)}
            disabled={page <= 0}
          >
            <ChevronLeft size={24} color={page <= 0 && "grey"} />
          </Button>
          <span valign="middle" className="align-middle">
            <span
              valign="middle"
              className="fs-5 px-3 py-2"
              style={{
                backgroundColor: "midnightblue",
                color: "white",
                borderRadius: 3,
              }}
            >
              {meta.number + 1}
            </span>
          </span>
          <Button
            variant=""
            className="border-0"
            onClick={() => setPage(page + 1)}
            disabled={meta.totalPages - meta.number - 1 <= 0}
          >
            <ChevronRight
              size={24}
              color={meta.totalPages - meta.number - 1 <= 0 && "grey"}
            />
          </Button>
        </div>
        Page size:
        <DropdownButton variant="outline" className="border-light" title={size}>
          <Dropdown.Item
            className={size === 5 && "text-primary"}
            onClick={() => setSize(5)}
          >
            5
          </Dropdown.Item>
          <Dropdown.Item
            className={size === 10 && "text-primary"}
            onClick={() => setSize(10)}
          >
            10
          </Dropdown.Item>
          <Dropdown.Item
            className={size === 15 && "text-primary"}
            onClick={() => setSize(15)}
          >
            15
          </Dropdown.Item>
          <Dropdown.Item
            className={size === 20 && "text-primary"}
            onClick={() => setSize(20)}
          >
            20
          </Dropdown.Item>
        </DropdownButton>
      </Stack>
    </>
  );
};
