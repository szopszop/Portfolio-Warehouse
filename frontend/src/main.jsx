import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import './main.css'
import ErrorPage from "./pages/Error.jsx";
import RootLayout from "./pages/Root.jsx";
import HomePage from "./pages/Home.jsx";
import UserProfilePage from "./pages/UserProfile.jsx";
import AuthenticationPage, { action as authAction } from "./pages/Authentication";
import GalleryPage from "./pages/Gallery.jsx";


const router = createBrowserRouter([
    {
        path: "/",
        element: <RootLayout />,
        errorElement: <ErrorPage />,
        children: [
            { index: true, element: <HomePage/> },
            { path: "/auth", element: <AuthenticationPage />, action: authAction },
            { path: "/profile", element: <UserProfilePage /> },
            { path: "/gallery", element: <GalleryPage /> },

        ],
    },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <RouterProvider router={router} />
    </React.StrictMode>
);