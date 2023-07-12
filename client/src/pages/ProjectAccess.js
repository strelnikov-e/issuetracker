import { useQueryClient } from "@tanstack/react-query";
import { useNavigate, useParams } from "react-router-dom";

import axios from "axios";
import { useMutation } from "@tanstack/react-query";

import Table from "react-bootstrap/Table";
import { Link } from "react-router-dom";
import Button from "react-bootstrap/esm/Button";
import Stack from "react-bootstrap/Stack";
import { Dropdown, FormControl } from "react-bootstrap";
import DropdownButton from "react-bootstrap/DropdownButton";
import Modal from "react-bootstrap/Modal";
import { Form } from "react-bootstrap";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Toast from "react-bootstrap/Toast";

import {
  CaretRightFill,
  ExclamationCircle,
  ExclamationCircleFill,
  ExclamationDiamondFill,
  ExclamationTriangleFill,
} from "react-bootstrap-icons";
import { CaretLeftFill } from "react-bootstrap-icons";
import { Plus } from "react-bootstrap-icons";
import { InfoCircleFill } from "react-bootstrap-icons";

import { useEffect, useState, useContext } from "react";
import { useAuth } from "../hooks/useAuth";
import { UserBadge } from "../components/UserBadge";
import { useFetchUserRoles } from "../utils/Repositories";
import { ProjectContext } from "../App";

const EMAIL_REGEX = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,50}$/;

export const ProjectAccess = () => {
  const { project } = useContext(ProjectContext);
  const { id } = useParams();
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [sort, setSort] = useState("id");
  const [order, setOrder] = useState("desc");
  const [filter, setFilter] = useState("");

  const [showErrorToast, setShowErrorToast] = useState(true);
  const toggleShowErrorToast = () => setShowErrorToast(!showErrorToast);

  const [userModalShow, setUserModalShow] = useState(false);

  const { data, isLoading, error } = useFetchUserRoles(
    id,
    page,
    size,
    sort,
    order,
    filter
  );

  const navigate = useNavigate();
  const { logout } = useAuth();

  const queryClient = useQueryClient();

  // we use the meta data returned in the response to disable
  // the "Next" button below
  const meta = data?.page || {};

  const handleChangeRole = useMutation({
    mutationFn: (param) => {
      if (param.role === "NONE") {
        return axios.delete(`/api/roles/${param.id}`);
      } else {
        return axios.patch(`/api/roles/${param.id}/role`, { role: param.role });
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["roles", id, page, size, sort, order, filter],
      });
    },
  });

  useEffect(() => {
    queryClient.invalidateQueries({
      queryKey: ["roles", id, page, size, sort, order, filter],
    });
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

  // add users in this modal. By email? List of users? Invitation?
  const AddUserModal = (props) => {
    const [email, setEmail] = useState("");
    const [validEmail, setValidEmail] = useState(false);
    const [role, setRole] = useState("");
    const [errMsg, setErrMsg] = useState("");
    const [draftUser, setDaftUser] = useState({
      role: "VIEWER",
      user: { email: "" },
      project: { id: id },
    });

    useEffect(() => {
      setErrMsg("");
    }, [email]);

    useEffect(() => {
      const result = EMAIL_REGEX.test(email);
      setValidEmail(result);
    }, [email]);

    const addUser = useMutation({
      mutationFn: (event) => {
        return axios.post(`/api/roles`, {
          role: draftUser.role,
          user: { email: email },
          project: { id: id },
        });
      },
      onSuccess: (data, error) => {
        queryClient.invalidateQueries({
          queryKey: ["roles", id, page, size, sort, order, filter],
        });
        setUserModalShow(false)
      },
      onError: (error, event) => {

        const e = error.response;
        if (e.status === 400) {
          if (e.data.includes("Duplicate entry")) {
            setErrMsg("User already exists");
          } else {
            setErrMsg("Server error");
          }
        } 
        else if (e.status === 403) {
          setErrMsg("You don't have permission to perform this action");
        }
        else if (e.status === 404) {
          setErrMsg("Email not registered");
        }
        else if (e.status === 409) {
          setErrMsg(e.data)
        }
        event.stopPropagation();
      },
    });

    function validateForm() {
      return EMAIL_REGEX.test(email);
    }

    const handleChange = (event) => {
      const { name, value } = event.target;
      setDaftUser({ ...draftUser, [name]: value });
    };

    return (
      <Modal
        {...props}
        size="md"
        aria-labelledby="addUserModal"
        animation={false}
        centered
      >
        <Modal.Header className="">
          <Modal.Title className="h5" id="addUserModal">
            Add people
          </Modal.Title>
        </Modal.Header>
        <Modal.Body className="p-4">
        
          <Form
            onSubmit={(event) => {
              event.preventDefault();
              addUser.mutate();
            }}
          >
            <Form.Group className="mb-3">
              <Form.Label>Email</Form.Label>
              <Form.Control
                size="md"
                type="email"
                value={email}
                autoComplete="off"
                className={
                  email.length > 0
                    ? validEmail
                      ? "is-valid"
                      : "is-invalid"
                    : ""
                }
                placeholder="e.g maria@mail.com"
                required
                onChange={(e) => setEmail(e.target.value)}
              />
              {errMsg !== "" && (
              <div className="mt-1">
                <span className="me-2">
                  {" "}
                  <ExclamationCircle color="indianred" size={16} />{" "}
                </span>
                <span className="align-middle text-secondary">
                  {errMsg}
                </span>{" "}
              </div>
            )}
            </Form.Group>
            <Form.Group controlId="form-group-id">
              <Form.Label>Role</Form.Label>
              <Form.Select
                size="md"
                aria-label="selectRoleForAdd"
                name="role"
                value={draftUser.role || "VIEWER"}
                onChange={handleChange}
              >
                <option value="VIEWER">Viewer</option>
                <option value="MANAGER">Manager</option>
                <option value="ADMIN">Administrator</option>
              </Form.Select>
            </Form.Group>
            
            <div className="d-flex justify-content-end mt-4">
              <Button className="me-2 justify-content-end"
              variant="outline"
              onClick={() => setUserModalShow(false)}>
                
                Cancel
              </Button>
              <Button
                type="submit"
                valign="middle"
                variant="primary"
                disabled={!validateForm()}
                className="btn btn-md justify-content-center"
              >
                <span className="align-middle px-2">Add</span>
              </Button>
            </div>
          </Form>
        </Modal.Body>
      </Modal>
    );
  };

  const RoleConverter = (role) => {
    return role === "ADMIN"
      ? "Administrator"
      : role === "MANAGER"
      ? "Manager"
      : role === "VIEWER" && "Viewer";
  };

  const userList = data._embedded?.projectRoleModelList?.map((entry) => {
    return (
      <tr className="" key={entry.id}>
        <td className="" valign="middle">
          <UserBadge user={entry.user} />
        </td>
        <td as={Link} className="" valign="middle">
          {entry.user.email}
        </td>
        <td valign="bottom" className="">
          <Dropdown className="">
            <Dropdown.Toggle
              variant="primary"
              valign="middle"
              color="light"
              className="btn-sm text-decoration-none w-75"
            >
              {RoleConverter(entry.role)}
            </Dropdown.Toggle>

            <Dropdown.Menu>
              <Dropdown.Item
                className="py-0"
                disabled={entry._links.delete? false : true}
                onClick={() =>
                  handleChangeRole.mutate({ id: entry.id, role: "NONE" })
                }
              >
                None
              </Dropdown.Item>
              <Dropdown.Divider />
              <Dropdown.Item
                disabled={entry._links.setAdmin? false : true}
                onClick={() =>
                  handleChangeRole.mutate({ id: entry.id, role: "ADMIN" })
                }
              >
                Administrator
              </Dropdown.Item>
              <Dropdown.Item
                disabled={entry._links.setManager? false : true}
                onClick={() =>
                  handleChangeRole.mutate({ id: entry.id, role: "MANAGER" })
                }
              >
                Manager
              </Dropdown.Item>
              <Dropdown.Item
              disabled={entry._links.setViewer? false : true}
                onClick={() =>
                  handleChangeRole.mutate({ id: entry.id, role: "VIEWER" })
                }
              >
                Viewer
              </Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
        </td>
      </tr>
    );
  });

  return (
    <>
      <Stack direction="horizontal" gap={2} className="mb-4">
        <h4>Access</h4>
        <AddUserModal
          show={userModalShow}
          onHide={() => setUserModalShow(false)}
        />
        <Button
          valign="middle"
          variant="primary"
          className="btn btn-sm ms-auto justify-content-center"
          onClick={() => {
            setUserModalShow(true);
          }}
        >
          <Plus size={25} /> <span className="align-middle">Add people</span>
        </Button>
      </Stack>
      <h6>Project access</h6>
      <Stack direction="horizontal" gap={3} className="px-3 mb-5">
        <div className="">
          <InfoCircleFill size={20} color="RebeccaPurple" />
        </div>{" "}
        <div>
          On this page you can customize this project's permissions. To add new
          user to the project please submit his or her email in a form below.
        </div>
      </Stack>
      {/* <AddUserForm /> */}
      <Table hover className="mb-3">
        <thead className="text-secondary">
          <tr>
            <th className="fw-semibold" width="">
              Name
            </th>
            <th className="fw-semibold" width="">
              Email
            </th>
            <th className="fw-semibold" width="">
              Role
            </th>
          </tr>
        </thead>
        <tbody>{userList}</tbody>
      </Table>
      <Stack direction="horizontal" gap={2}>
        <div className="me-auto">
          <Button
            variant="dark-outline border-dark"
            className="me-3"
            onClick={() => setPage(page - 1)}
            disabled={page <= 0}
          >
            {" "}
            <CaretLeftFill size={16} />
            {" previous"}
          </Button>
          <Button
            variant="dark-outline border-dark"
            onClick={() => setPage(page + 1)}
            disabled={meta.totalPages - meta.number - 1 <= 0}
          >
            {"next "}
            <CaretRightFill size={16} />{" "}
          </Button>
        </div>
        <div className="text-muted border-end px-2">
          Page {page + 1} of {meta.totalPages}
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
