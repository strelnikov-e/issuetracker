import axios from "axios";
import { useQuery } from "@tanstack/react-query";

export const useFetchProjects = (page, size) => {
  const { isLoading, error, data } = useQuery({
    queryKey: ["projects", page, size],
    queryFn: async () => {
      const response = await axios.get(
        `/api/projects?page=${page}&size=${size}`
      );
      // console.log("FETCH PROJECTS RESPONSE ", response);
      return response.data;
    },
    keepPreviousData: true,
  });

  return { isLoading, error, data };
};

export const useFetchIssues = (
  project,
  page,
  size,
  sort,
  order,
  filter,
  incomplete
) => {
  const { isLoading, error, data } = useQuery({
    queryKey: ["issues", project, page, size, sort, order, filter, incomplete],
    queryFn: async () => {
      const response = await axios.get(
        `/api/issues?project=${project}&page=${page}&size=${size}&sort=${sort},${order}&${filter}&${incomplete}`
      );
      // console.log("FETCH ISSUES RESPONSE ", response);
      return (response).data;
    },
    keepPreviousData: true,
  });
  return { isLoading, error, data };
};

export const useFetchMyWork = (
  project,
  assignee,
  page,
  size,
  sort,
  order
) => {
  const { isLoading, error, data } = useQuery({
    queryKey: ["mywork", project, assignee, page, size, sort, order],
    queryFn: async () => {
      const response = await axios.get(
        `/api/issues?project=${project}&assignee=${assignee}&page=${page}&size=${size}&sort=${sort},${order}`
      );
      // console.log("FETCH MY WORK RESPONSE ", response);
      return (response).data;
    },
    keepPreviousData: true,
  });
  return { isLoading, error, data };
};


export const useFetchBoards = (key, url) => {
  const { isLoading, error, data } = useQuery({
    queryKey: [key],
    queryFn: () =>
      axios.get(url).then((response) => {
        const result = processData(response?.data);
        return result;
      }),
  });
  // console.log("FETCH BOARDS WITH PROJECT ID", data);
  return { isLoading, error, data };
};

export const useChangeProject = async (id) => {
  const response = await axios.patch(`/api/users`, { currentProject: id });
  // console.log("CHANGE PROJECT PATCH REQUEST ", response);
  return response.data;
};

export const useFetchUserDetails = async () => {
  const responce = await axios.get("http://localhost:8080/api/users/details");
  // console.log(responce.data);
  return responce.data;
};

export const useFetchUsers = (projectId) => {
  const { isLoading, error, data } = useQuery({
    queryKey: ["users", projectId],
    queryFn: () =>
      axios.get(`/api/users?project=${projectId}`).then((response) => {
        return response?.data;
      }),
  });
  // console.log("FETCH USERS WITH PROJECT ID", data);
  return { isLoading, error, data };
};

export const useFetchCurrentProject = (projectId) => {
  const { isLoading, error, data } = useQuery({
    queryKey: ["currentProject", projectId],
    queryFn: async () => {
      const response = await axios.get(`/api/projects/${projectId}`);
      // console.log("FETCH CURRENT PROJECT RESPONSE ", response);
      return response.data;
    },
  });

  return { isLoading, error, data };
};

export const useFetchUserRoles = (projectId, page, size, sort, order, filter) => {
  const { isLoading, error, data } = useQuery({
    queryKey: ["roles", projectId, page, size, sort, order, filter],
    queryFn: () =>
      axios.get(`/api/roles/project?project=${projectId}&page=${page}&size=${size}&sort=${sort},${order}&${filter}`).then((response) => {
        return response?.data;
      }),
  });
  // console.log("FETCH USERS ROLES WITH PROJECT ID", data);
  return { isLoading, error, data };
};

export const processData = (issues) => {
  // console.log("PROCESS DATA", issues)
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

  if ("_embedded" in issues && "issueModelList" in issues._embedded) {
    issues._embedded.issueModelList.forEach((issue) => {
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
};
