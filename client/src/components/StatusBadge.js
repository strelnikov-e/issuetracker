import { Badge } from "react-bootstrap";

export const StatusBadge = (props) => {
    const status = props.status;
  return (
      <Badge
        pill
        text=""
        bg={
          status === "TODO"
            ? 'primary'
            : status === "INPROGRESS"
            ? "danger" 
            : status === "INREVIEW"
            ? "success"
            : "dark"
        }
      >
        {StatusConverter(status)}
      </Badge>
  );
}

export const StatusConverter = (props) => {
let status = null;
    switch (props) {
      case "INPROGRESS":
        status = "In progress";
        break;
      case "DONE":
        status = "Done";
        break;
      case "REVIEW":
        status = "In review";
        break;
      default: 
        status = "To do";
    }
    return status;
}