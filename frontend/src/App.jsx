import React, {lazy, Suspense} from 'react'
import ReactDOM from 'react-dom/client'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import './App.css'
import ErrorPage from "./pages/Error.jsx";
import RootLayout from "./pages/Root.jsx";
import HomePage from "./pages/Home.jsx";
import UserProfilePage from "./pages/UserProfile.jsx";
import AuthenticationPage, {action as authAction} from "./pages/Authentication";
import GalleryPage from "./pages/Gallery.jsx";
import {action as logoutAction} from "./pages/Logout.jsx";
import {checkAuthLoader, tokenLoader} from "./util/auth"

const BlogPage = () => lazy(import('./pages/lazyLoading/Blog'));
const PostPage = () => lazy(import('./pages/lazyLoading/Post'));

const router = createBrowserRouter([
    {
        path: "/",
        element: <RootLayout/>,
        errorElement: <ErrorPage/>,
        id: 'root',
        loader: tokenLoader,
        children: [
            {index: true, element: <HomePage/>},
            {path: "auth", element: <AuthenticationPage/>, action: authAction},
            {path: "logout", action: logoutAction},
            {path: "profile", element: <UserProfilePage/>, loader: checkAuthLoader},
            {path: "gallery", element: <GalleryPage/>},
            {path: 'blog', children: [
                    {
                        index: true,
                        element:
                            <Suspense fallback={<p>Loading..</p>}>
                                <BlogPage/>
                            </Suspense>,
                        loader: () => import('./pages/lazyLoading/Blog').then((module) => module.loader())
                    },
                    {
                        path: ':id',
                        element: <PostPage />,
                        loader: () => import('./pages/lazyLoading/Post').then((module) => module.loader())
                    }
                ]}


        ],
    },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>
);