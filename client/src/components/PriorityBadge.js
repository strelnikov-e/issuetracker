import { Badge } from "react-bootstrap";

export const PriorityBadge = (props) => {
  const priority = props.priority;
  return (
      <Badge
        pill
        bg={
          priority === "MEDIUM"
            ? 'warning'
            : priority === "HIGH"
            ? "danger"
            : "success"
        }
      >
        {PriorityConverter(priority)}
      </Badge>
  );
};

export const PriorityConverter = (props) => {
      let priority = "Medium"
      switch (props) {
        case "HIGH":
          priority = "High";
          break;
        case "LOW":
          priority = "Low";
      }
      return priority;
  }
