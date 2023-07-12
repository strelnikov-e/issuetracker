import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import dayjs from "dayjs";

import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/esm/Button";
import ToggleButton from "react-bootstrap/ToggleButton";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Stack from "react-bootstrap/Stack";
import { Badge, Dropdown, DropdownButton, ListGroup } from "react-bootstrap";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { Modal } from "react-bootstrap";

import {
  CaretDownFill,
  ChevronLeft,
  ChevronRight,
  List,
  ThreeDots,
} from "react-bootstrap-icons";
import { CaretUpFill } from "react-bootstrap-icons";
import { CaretRightFill } from "react-bootstrap-icons";
import { CaretLeftFill } from "react-bootstrap-icons";
import { Plus } from "react-bootstrap-icons";
import { CheckCircle } from "react-bootstrap-icons";
import { Filter } from "react-bootstrap-icons";
import { ArrowDownUp } from "react-bootstrap-icons";

import { PriorityBadge } from "../components/PriorityBadge";
import { StatusBadge } from "../components/StatusBadge";
import { TypeBadgeIcon } from "../components/TypeBadge";
import { useFetchIssues } from "../utils/Repositories";
import { useFetchUsers } from "../utils/Repositories";
import { UserBadge } from "../components/UserBadge";
import { editIcon, trashIcon } from "../components/icons";
import { ProjectContext } from "../App";
import { useAuth } from "../hooks/useAuth";
import { Component } from "react";

export const issueLoader = async () => {
  const response = await axios.get("/api/issues");
  if (!response.ok) {
    throw Error("Could not fetch the issues");
  }
  return response;
};

export default function Issues() {
  const { currentProject } = useContext(ProjectContext);
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [sort, setSort] = useState("startDate");
  const [order, setOrder] = useState("desc");
  const [filter, setFilter] = useState("");

  const [filterModalShow, setFilterModalShow] = useState(false);
  const [filterModalRole, setFilterModalRole] = useState("");

  const [incomplete, setIncomplete] = useState(false);

  const url = `/api/issues?project=${currentProject?.id || 0}`;
  const queryClient = useQueryClient();
  const { data, isLoading, error } = useFetchIssues(
    currentProject?.id || 0,
    page,
    size,
    sort,
    order,
    filter,
    incomplete && "incomplete"
  );
  const {
    data: users,
    isLoading: usersLoading,
    error: usersError,
  } = useFetchUsers(currentProject?.id);
  // we use the meta data returned in the response to disable
  // the "Next" button below
  const meta = data?.page || {};

  const handleDelete = useMutation({
    mutationFn: (id) => {
      return axios.delete(`/api/issues/${id}`);
    },
    onSettled: () => {
      queryClient.invalidateQueries({
        queryKey: [
          "issues",
          currentProject.id,
          page,
          size,
          sort,
          order,
          filter,
        ],
      });
    },
  });

  const handleChange = useMutation({
    mutationFn: (param) => {
      axios.patch(`/api/issues/${param.issueId}`, { assignee: param.assignee });
    },
    onSettled: () => {
      queryClient.invalidateQueries({
        queryKey: [
          "issues",
          currentProject.id,
          page,
          size,
          sort,
          order,
          filter,
        ],
      });
    },
  });

  const handleSort = (param) => {
    if (sort !== param) {
      setSort(param);
    } else {
      setOrder(order === "desc" ? "asc" : "desc");
    }
    queryClient.invalidateQueries({
      queryKey: ["issues", currentProject.id, page, size, sort, order, filter],
    });
  };

  const handleFilter = (param) => {
    if (filter !== param) {
      setFilter(param);
    }
    queryClient.invalidateQueries({
      queryKey: ["issues", currentProject.id, page, size, sort, order, filter],
    });
  };

  const SortIcon = ({ param }) => {
    if (sort === param) {
      if (order === "desc") {
        return <CaretDownFill className="ms-auto" />;
      } else {
        return <CaretUpFill className="ms-auto" />;
      }
    }
  };

  const FilterByRoleModal = (props) => {
    return (
      <Modal {...props} size="sm" aria-labelledby="filterByRoleModal" centered>
        <Modal.Header className="bg-light">
          <Modal.Title className="h6" id="filterByRoleModal">
            Filter by {props.role}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <ListGroup as="ul">
            {users?._embedded?.userModelList.map((u) => (
              <ListGroup.Item
                as="li"
                className="mb-2 border-0"
                key={u.id}
                action
                onClick={() => {
                  props.onHide();
                  setFilter(`${props.role}=${u.id}`);
                }}
              >
                <UserBadge user={u}></UserBadge>
              </ListGroup.Item>
            ))}
          </ListGroup>
        </Modal.Body>
        <Button className="mb-2" variant="dark-outline" onClick={props.onHide}>
          Close
        </Button>
      </Modal>
    );
  };

  const DropdownOptions = ({ issue }) => {
    return (
      <Dropdown>
        <Dropdown.Item
          className={!issue._links.update && "disabled"}
          as={Link}
          to={{
            pathname: `/issues/${issue.id}`,
          }}
          state={{ projectId: issue.project.id }}
        >
          Edit
        </Dropdown.Item>
        <Dropdown.Item
          className={!issue._links.delete && "disabled"}
          onClick={() => handleDelete.mutate(issue.id)}
        >
          Remove
        </Dropdown.Item>
      </Dropdown>
    );
  };

  const IssueList = () => {
    if (isLoading) {
      return (
        <tr>
          <td>"Loading..."</td>
        </tr>
      );
    }
    if (error) {
      if (error.response?.status === 401) {
        logout();
        // setUser(null)
        return navigate("/login", { replace: true });
      } else {
        return <p>An error occured: {error.message}</p>;
      }
    }

    // dropdown to choose another assignee
    const AssigneeDropdown = ({ issueId }) => {
      if (usersLoading) return <p>Loading...</p>;

      return (
        <>
          {users?._embedded?.userModelList.map((u) => (
            <Dropdown.Item
              key={u.id}
              onClick={() => handleChange.mutate({ issueId, assignee: u.id })}
            >
              <UserBadge user={u}></UserBadge>
            </Dropdown.Item>
          ))}
        </>
      );
    };

    return (
      <>
        {data?._embedded?.issueModelList.map((issue) => (
          <tr key={issue.id}>
            <td hidden valign="middle" className="text-secondary">
              {issue.key}
            </td>
            <td hidden valign="middle" className="">
              <TypeBadgeIcon type={issue.type} />
            </td>
            <td valign="middle" as={Link} className="">
              <Link
                to={{
                  pathname: `/issues/${issue.id}`,
                }}
                state={{ projectId: issue.project.id }}
                className="link-dark text-decoration-none"
              >
                <TypeBadgeIcon type={issue.type} />
                <span className="ms-3 " valign="middle">
                  {issue.name}
                </span>
              </Link>
            </td>
            <td valign="middle" className="text-dark border-start">
              <UserBadge user={issue.assignee}></UserBadge>
              {/* <Dropdown className="mb-2">
                <Dropdown.Toggle
                  variant="link"
                  id="assignee dropdown"
                  color="light"
                  className="p-0"
                >
                  <UserBadge user={issue.assignee}></UserBadge>
                </Dropdown.Toggle>

                <Dropdown.Menu>
                  <AssigneeDropdown issueId={issue.id} />
                  <Dropdown.Item>
                    <UserBadge
                      user={{ firstName: "", lastName: "" }}
                    ></UserBadge>
                  </Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown> */}

              {/* <UserBadge user={issue.assignee} /> */}
            </td>
            <td
              valign="middle"
              className={
                issue.status === "DONE"
                  ? "text-muted border-start"
                  : Date.parse(issue.dueDate) < Date.now()
                  ? "text-danger border-start"
                  : "text-dark border-start"
              }
            >
              {dayjs(issue.dueDate).format("MMM D, YYYY")}
            </td>
            <td valign="middle" align="center" className="border-start">
              <PriorityBadge priority={issue.priority} />
            </td>
            <td valign="middle" align="center" className="border-start">
              <StatusBadge status={issue.status} />
            </td>

            <td valign="middle" className="border-start">
              <DropdownButton
                variant="outline-secondary border-0"
                className="border-0"
                title=<ThreeDots size={20} />
              >
                <DropdownOptions issue={issue} />
              </DropdownButton>
            </td>
          </tr>
        ))}
      </>
    );
  };

  return (
    <>
      <div className="border-top p-3 mb-0">
        <div className="d-flex justify-content-start">
          <Button
            valign="middle"
            variant="primary"
            onClick={() =>
              navigate("/issues/new", {
                state: { projectId: currentProject.id },
              })
            }
            className="btn me-auto justify-content-center"
          >
            <Plus size={25} /> <span className="align-middle">Add task </span>
          </Button>
          <ToggleButton
            variant="outline-dark"
            className="border-white"
            onClick={() => setIncomplete(!incomplete)}
          >
            <span
              className={
                incomplete ? "align-middle" : "align-middle text-secondary"
              }
            >
              {" "}
              <CheckCircle color={incomplete ? "green" : "grey"} />{" "}
              <span className="align-middle">Incomplete tasks </span>
            </span>
          </ToggleButton>

          <DropdownButton
            variant=""
            className="border-0"
            title=<span
              className={
                filter === ""
                  ? "align-middle text-secondary"
                  : "align-middle text-dark"
              }
            >
              <Filter size={20} /> <span className="align-middle">Filter</span>
            </span>
          >
            <Dropdown
              className="d-inline-block w-200"
              style={{ minWidth: "420px", height: "100px" }}
            >
              <Container>
                <Row className="mb-2 py-2 border-bottom">
                  <Col>
                    <Dropdown.Item
                      className="text-muted"
                      onClick={() => handleFilter("")}
                    >
                      None
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() => {
                        setFilterModalRole("assignee");
                        setFilterModalShow(true);
                      }}
                    >
                      Assignee
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() => {
                        setFilterModalRole("reporter");
                        setFilterModalShow(true);
                      }}
                    >
                      Reporter
                    </Dropdown.Item>

                    <Dropdown.Item
                      onClick={() => {
                        setIncomplete(true);
                        setFilter(
                          `dueDate=${new Date().toJSON().slice(0, 10)}`
                        );
                      }}
                    >
                      Overdue
                    </Dropdown.Item>

                    <FilterByRoleModal
                      role={filterModalRole}
                      show={filterModalShow}
                      onHide={() => setFilterModalShow(false)}
                    />
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <Dropdown.Header>Filter by status:</Dropdown.Header>
                    <Dropdown.Item onClick={() => handleFilter("status=TODO")}>
                      To do
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() => handleFilter("status=INPROGRESS")}
                    >
                      In progress
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() => handleFilter("status=INREVIEW")}
                    >
                      In testing
                    </Dropdown.Item>
                    <Dropdown.Item onClick={() => handleFilter("status=DONE")}>
                      Done
                    </Dropdown.Item>
                  </Col>
                  <Col>
                    <Dropdown.Header>Filter by priority:</Dropdown.Header>
                    <Dropdown.Item onClick={() => handleFilter("priority=LOW")}>
                      Low
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() => handleFilter("priority=MEDIUM")}
                    >
                      Medium
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() => handleFilter("priority=HIGH")}
                    >
                      High
                    </Dropdown.Item>
                  </Col>
                  <Col>
                    <Dropdown.Header>Filter by type:</Dropdown.Header>
                    <Dropdown.Item
                      onClick={() => handleFilter("type=MILESTONE")}
                    >
                      Milestone
                    </Dropdown.Item>
                    <Dropdown.Item onClick={() => handleFilter("type=TASK")}>
                      Task
                    </Dropdown.Item>
                    <Dropdown.Item onClick={() => handleFilter("type=BUG")}>
                      Bug
                    </Dropdown.Item>
                  </Col>
                </Row>
                <Row>
                  <Col></Col>
                  <Col></Col>
                  <Col></Col>
                </Row>
              </Container>
            </Dropdown>
          </DropdownButton>

          <DropdownButton
            variant=""
            className="border-light"
            title=<span
              className={
                sort === "startDate"
                  ? "align-middle text-secondary"
                  : "align-middle text-dark"
              }
            >
              <ArrowDownUp /> <span className="align-middle">Group by</span>
            </span>
          >
            <Dropdown.Item
              className="text-muted border-bottom py-2 mb-2 mt-0"
              onClick={() => handleSort("startDate")}
            >
              None
            </Dropdown.Item>
            <Dropdown.Item
              className={sort === "priority" && "text-primary"}
              onClick={() => handleSort("priority")}
            >
              Priority
            </Dropdown.Item>
            <Dropdown.Item
              className={sort === "status" && "text-primary"}
              onClick={() => handleSort("status")}
            >
              Status
            </Dropdown.Item>
            <Dropdown.Item
              className={sort === "type" && "text-primary"}
              onClick={() => handleSort("type")}
            >
              Type
            </Dropdown.Item>
          </DropdownButton>
        </div>
      </div>

      <Table responsive hover className="mb-4 overflow mb-5">
        <thead>
          <tr className="text-secondary">
            <th hidden className="border-top" width="80px">
              Key
            </th>
            <th
              hidden
              className="border-top"
              onClick={() => handleSort("type")}
            >
              <span className="">
                Type <SortIcon param="type" />
              </span>
            </th>
            <th
              className="border-top"
              width=""
              onClick={() => handleSort("name")}
            >
              <span className="col-auto fw-semibold">
                Task name{" "}
                <span className="ms-2" valign="bottom">
                  {" "}
                  <SortIcon param="name" />
                </span>
              </span>
            </th>
            <th className="border-top border-start fw-semibold">Assignee</th>
            <th
              className="border-top border-start fw-semibold"
              width="120px"
              onClick={() => handleSort("dueDate")}
            >
              <span className=" fw-semibold">
                Due date{" "}
                <span className="ms-2" valign="bottom">
                  <SortIcon param="dueDate" />{" "}
                </span>
              </span>
            </th>
            <th
              className="border-top border-start"
              onClick={() => handleSort("priority")}
            >
              <span className=" fw-semibold">
                Priority{" "}
                <span className="ms-2" valign="bottom">
                  <SortIcon param="priority" />
                </span>
              </span>
            </th>
            <th
              className="border-top border-start"
              onClick={() => handleSort("status")}
            >
              <span className=" fw-semibold">
                Status{" "}
                <span className="ms-2" valign="bottom">
                  <SortIcon param="status" />
                </span>
              </span>
            </th>
            <th className="border-top border-start" width="60px"></th>
          </tr>
        </thead>
        <tbody>
          <IssueList />
        </tbody>
      </Table>
      <Stack direction="horizontal" gap={2}>
        <div className="me-auto">
          <Button
            variant=""
            className="border-0"
            onClick={() => setPage(page - 1)}
            disabled={page <= 0}
          >
            <ChevronLeft size={24} />
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
              {1 + meta.number || 0}
            </span>
          </span>
          <Button
            variant=""
            className="border-0"
            onClick={() => setPage(page + 1)}
            disabled={meta.totalPages - meta.number - 1 <= 0}
          >
            <ChevronRight size={24} />
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
}
