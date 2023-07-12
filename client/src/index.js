import React, { StrictMode } from "react";
import ReactDOM from "react-dom/client";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import App from "./App";

import "bootstrap/dist/css/bootstrap.min.css";
import "./css/style.css";

const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 1000,
      },
    },
  });

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
//   <StrictMode>
<QueryClientProvider client={queryClient}>
    <App />
    </QueryClientProvider>
//   </StrictMode>
);
