import Table from "react-bootstrap/Table";
import { Link } from "react-router-dom";
import Button from "react-bootstrap/esm/Button";
import Stack from "react-bootstrap/Stack";
import { Dropdown } from "react-bootstrap";
import DropdownButton from "react-bootstrap/DropdownButton";

import { CaretRightFill } from "react-bootstrap-icons";
import { CaretLeftFill } from "react-bootstrap-icons";

export const PageNavigation = (meta, page, setPage, size, setSize) => {
    return(
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
        <DropdownButton
            variant="outline"
            className="border-light"
            title={size}
            >
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
)}