import { useState, useContext } from "react";
import { useQueryClient } from "@tanstack/react-query";

import Table from "react-bootstrap/Table";
import { Link } from "react-router-dom";
import Button from "react-bootstrap/esm/Button";

import { FetchProjects } from "../utils/Repositories";
import { ProjectContext } from "../App";

export default function Projects() {
  const [projects, setProjects] = useState([]);
  const { setCurrentProject } = useContext(ProjectContext);
  const { data, isLoading, error } = FetchProjects();
  const queryClient = useQueryClient();
  const trashIcon = (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="16"
      height="16"
      fill="currentColor"
      className="bi bi-trash3"
      viewBox="0 0 16 16"
    >
      <path d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5ZM11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H2.506a.58.58 0 0 0-.01 0H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1h-.995a.59.59 0 0 0-.01 0H11Zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5h9.916Zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47ZM8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5Z" />
    </svg>
  );
  const editIcon = (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="16"
      height="16"
      fill="currentColor"
      className="bi bi-pencil-square"
      viewBox="0 0 16 16"
    >
      <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
      <path
        fillRule="evenodd"
        d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"
      />
    </svg>
  );


  const remove = async (id) => {
    await fetch(`/api/projects/${id}`, {
      method: "DELETE",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      credentials: "include",
    }).then(() => {
      let updatedProjects = [...projects].filter((i) => i.id !== id);
      setProjects(updatedProjects);
    });
    queryClient.invalidateQueries({ queryKey: ['projects'] });
  };

  if (isLoading) {
    return <p>Loading...</p>;
  }

  const projectList = data._embedded.projectList.map((project) => {
    const id = project.id;
    const name = project.name;
    console.log(id, name)
    return (
      <tr className="fs-5" key={project.id}>
        <td className="fs-5 text-muted" valign="bottom">
          {project.key}
        </td>
        <td as={Link} valign="bottom">
          <Link
            onClick={() => setCurrentProject(project)}
            to={{
              pathname: `/boards`,             
              }}
              state= { project }
    
            className="fs-5 link-dark link-offset-3"
          >
            {project.name}
          </Link>
        </td>
        <td valign="bottom" className="fs-5">
          {project.manager}
        </td>
        <td valign="bottom" className="fs-5">
          {project.startDate}
        </td>
        <td>
          <Button
            as={Link}
            to={"/projects/" + project.id}
            variant="link "
            className="link-secondary"
          >
            {editIcon}
          </Button>
          <Button
            variant="link "
            className="link-secondary "
            onClick={() => remove(project.id)}
          >
            {trashIcon}
          </Button>
        </td>
      </tr>
    );
  });

  return (
    <Table hover className="fs-5">
      <thead>
        <tr>
          <th className="fs-5" width="80px">
            Key
          </th>
          <th className="fs-5" width="">
            Name
          </th>
          <th className="fs-5" width="">
            Manager
          </th>
          <th className="fs-5" width="">
            Date Opened
          </th>
          <th className="fs-5" width="100px"></th>
        </tr>
      </thead>
      <tbody>{projectList}</tbody>
    </Table>
  );
}
