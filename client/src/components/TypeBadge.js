import { Component } from "react";
import { Badge } from "react-bootstrap";


export const TypeBadge = (props) => {
    const type = props.type;
    return (
        <div className={

                type === "BUG"
                    ? 'fw-semibold text-danger'
                    : type === "MILESTONE"
                        ? 'fw-semibold text-primary'
                        : type === "TASK"
                            ? 'fw-semibold text-success'
                            : 'fw-semibold text-dark'
            }
        >
            {TypeConverter(type)}
        </div>
    );
}

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
            type = "Task"
    }
    return type;
}