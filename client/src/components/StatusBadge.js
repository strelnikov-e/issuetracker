import { Badge } from "react-bootstrap";

export const StatusBadge = (props) => {
    const status = props.status;
  return (
      <div className={status === "DONE" ? "text-secondary" : ""}>
        {StatusConverter(status)}
        </div>

  );
}

// export const StatusBadge = (props) => {
//   const status = props.status;
// return (
//     <Badge
//       pill
//       text=""
//       bg="light"
//       bg={
//         status === "TODO"
//           ? 'todo'
//           : status === "INPROGRESS"
//           ? "progress" 
//           : status === "INREVIEW"
//           ? "review"
//           : "secondary"
//       }
//     >
//       {StatusConverter(status)}
//     </Badge>
// );
// }

export const StatusConverter = (props) => {
let status = null;
    switch (props) {
      case "INPROGRESS":
        status = "In progress";
        break;
      case "DONE":
        status = "Done";
        break;
      case "INREVIEW":
        status = "In review";
        break;
      default: 
        status = "To do";
    }
    return status;
}