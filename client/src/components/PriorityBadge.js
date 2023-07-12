import { Badge } from "react-bootstrap";

export const PriorityBadge = (props) => {
  const priority = props.priority;
  return (
      <Badge className=""
        pill
        bg={
          priority === "MEDIUM"
            ? 'medium'
            : priority === "HIGH"
            ? "high"
            : "low"
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
