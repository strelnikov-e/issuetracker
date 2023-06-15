import { useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";

import Badge from "react-bootstrap/Badge";
import Dropdown from "react-bootstrap/Dropdown";
import Stack from "react-bootstrap/esm/Stack";

import { PriorityBadge } from "./PriorityBadge.js";

function IssueCard({
  data: { id, name, key, type, priority, assignee, status, project },
  index,
}) {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const url = `/api/issues?projectId=${project.id}`;

  // const issue = data.data;
  const threeDots = (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="16"
      height="16"
      fill="currentColor"
      className="bi bi-three-dots"
      viewBox="0 0 16 16"
    >
      <path d="M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z" />
    </svg>
  );

  const handleDelete = useMutation({
    mutationFn: () => {
      return axios.delete(`/api/issues/${id}`);
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: [url] });
    },
  });

  return (
    <>
      <Stack direction="horizontal">
        <div className="me-auto">
          <span className="align-middle text-muted">
            <PriorityBadge priority={priority} />
            <small> {key}</small>
          </span>
        </div>
        <Dropdown>
          <Dropdown.Toggle
            variant="light"
            id="dropdown-basic"
          ></Dropdown.Toggle>

          <Dropdown.Menu>
            <Dropdown.Item
              onClick={() =>
                navigate(`/issues/${id}`, { state: { projectId: project.id } })
              }
            >
              Edit
            </Dropdown.Item>
            <Dropdown.Item onClick={() => handleDelete.mutate()}>
              Delete
            </Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
      </Stack>
      <p className="">{name}</p>
      <Stack direction="horizontal">
        <small className="text-muted me-auto">{type}</small>
        <Badge pill bg="secondary">
          {assignee}
        </Badge>
      </Stack>
    </>
  );
}

export default IssueCard;
