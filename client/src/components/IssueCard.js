import { useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";

import Dropdown from "react-bootstrap/Dropdown";
import Stack from "react-bootstrap/esm/Stack";
import { DropdownButton } from "react-bootstrap";

import { PriorityBadge } from "./PriorityBadge.js";
import { UserBadgeSmOnly } from "./UserBadge.js";
import { ThreeDots } from "react-bootstrap-icons";

function IssueCard({
  data: { id, name, key, type, priority, assignee, status, project, _links, url },
  index,
}) {
  const queryClient = useQueryClient();
  const navigate = useNavigate();


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
        <div className="me-auto p-1 mb-2">
          <span className="align-middle text-muted">
            <PriorityBadge priority={priority} />
            <small> {key}</small>
          </span>
        </div>
        <DropdownButton
            variant="outline-secondary border-0"
            className="border-0"
            title=<ThreeDots size={20} />
          >
            <Dropdown>
              <Dropdown.Item onClick={() => navigate("/issues/" + id)} className={!_links.update && "disabled"}>
                Edit
              </Dropdown.Item>
              <Dropdown.Item onClick={() => handleDelete.mutate()} className={!_links.delete && "disabled"}>
                Remove
              </Dropdown.Item>
            </Dropdown>
          </DropdownButton>
          {/* className={!project._links.delete && "disabled"} */}
        
      </Stack>
      <p className={!_links.update? "text-secondary": ""}>{name}</p>
      <Stack direction="horizontal">
        <small className="text-muted me-auto">{type}</small>
        <UserBadgeSmOnly user={assignee} />
      </Stack>
    </>
  );
}

export default IssueCard;
