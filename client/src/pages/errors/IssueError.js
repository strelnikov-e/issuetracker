import { useRouteError, Link } from "react-router-dom";

export default function IssueError() {
    const error = useRouteError();

    return (
        <div className="issue-error">
            <h2>Error</h2>
            <p>{error.message}</p>
            <Link to='/'>Back to the Homepage</Link>
        </div>
    )
}