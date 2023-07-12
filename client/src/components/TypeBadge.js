import { Component } from "react";
import { Badge } from "react-bootstrap";
import { Clipboard, Gear } from "react-bootstrap-icons";
import { Diagram3 } from "react-bootstrap-icons";
import { Circle } from "react-bootstrap-icons";

export const TypeBadge = (props) => {
  const type = props.type;
  return (
    <div
      className={
        type === "BUG"
          ? "fw-semibold text-danger"
          : type === "MILESTONE"
          ? "fw-semibold text-primary"
          : type === "TASK"
          ? "fw-semibold text-success"
          : "fw-semibold text-dark"
      }
    >
      {TypeConverter(type)}
    </div>
  );
};

export const TypeBadgeIcon = (props) => {
  const type = props.type;
  return (
    <>
      {type === "BUG" ? (
        <Gear size={18} color="RebeccaPurple"/>
      ) : type === "MILESTONE" ? (
        <Diagram3 size={18} color="green"/>
      ) : type == "TASK" ? (
        <Clipboard size={18} color="navy"/>
      ) : (
        <></>
      )}
    </>

    // <div className={

    //         type === "BUG"
    //             ? 'fw-semibold text-danger'
    //             : type === "MILESTONE"
    //                 ? 'fw-semibold text-primary'
    //                 : type === "TASK"
    //                     ? 'fw-semibold text-success'
    //                     : 'fw-semibold text-dark'
    //     }
    // >
    //     {TypeConverter(type)}
    // </div>
  );
};

export const TypeConverter = (props) => {
  let type = null;
  switch (props) {
    case "BUG":
      type = "Bug";
      break;
    case "MILESTONE":
      type = "Milestone";
      break;
    default:
      type = "Task";
  }
  return type;
};
