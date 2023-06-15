import axios from "axios";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import useAuth from "./hooks/useAuth";

export function FetchIssues(key, url) {
  const { isLoading, error, data } = useQuery({
    queryKey: [key],
    queryFn: () =>
      axios.get(url).then((response) => {
        const result = processData(response.data);
        return result;
      }),
  });
  return { isLoading, error, data };
}

export const FetchProjects = () => {
  const { auth } = useAuth();
  // if (auth?.username ? true : false) {
  //   return {data: {}, isLoading: false, error: {}}
  // }
  const { isLoading, error, data } = useQuery({
    queryKey: ["projects"],
    queryFn: async () => {
      const response = 
        await axios.get("/api/projects")
        // : ({ data: {_embedded: {projectList: []}}, error: {}, isLoading: true });
      console.log(response);
      return response.data;
    },
  });

  return { isLoading, error, data };
};

export function processData(issues) {
  let todo = [];
  let inProgress = [];
  let inReview = [];
  let done = [];
  let issueList = {};

  const result = {};

  const statuses = {
    TODO: { id: "column-1", title: "TODO", issueIds: [] },
    INPROGRESS: { id: "column-2", title: "INPROGRESS", issueIds: [] },
    INREVIEW: { id: "column-3", title: "INREVIEW", issueIds: [] },
    DONE: { id: "column-4", title: "DONE", issueIds: [] },
  };

  result["columns"] = statuses;
  result["columnOrder"] = ["TODO", "INPROGRESS", "INREVIEW", "DONE"];

  if ("_embedded" in issues && "issueList" in issues._embedded) {
    issues._embedded.issueList.forEach((issue) => {
      issueList[issue.id] = issue;
      switch (issue.status) {
        case "INPROGRESS":
          inProgress.push(issue.id);
          break;
        case "INREVIEW":
          inReview.push(issue.id);
          break;
        case "DONE":
          done.push(issue.id);
          break;
        default:
          todo.push(issue.id);
      }
    });
  }
  result["issues"] = issueList ? issueList : [];
  result["columns"]["TODO"]["issueIds"] = todo;
  result["columns"]["INPROGRESS"]["issueIds"] = inProgress;
  result["columns"]["INREVIEW"]["issueIds"] = inReview;
  result["columns"]["DONE"]["issueIds"] = done;

  return result;
}
