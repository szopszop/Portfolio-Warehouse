import classes from './MainNavigation.module.css';
import {Form, NavLink, useRouteLoaderData} from "react-router-dom";

function MainNavigation() {

    const token = useRouteLoaderData('root');

    return (
        <header className={classes.header}>
            <nav>
                <ul className={classes.list}>
                    <li>
                        <NavLink to="/" className={({isActive}) =>
                            isActive ? classes.active : undefined} end>
                            Home
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to="/gallery" className={({isActive}) =>
                            isActive ? classes.active : undefined}>
                            Gallery
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to="/blog" className={({isActive}) =>
                            isActive ? classes.active : undefined}>
                            Fake Posts for Lazy Loading
                        </NavLink>
                    </li>
                    {!token && (
                        <li>
                            <NavLink to='/auth?mode=login' className={({isActive}) =>
                                isActive ? classes.active : undefined}>
                                Login / Register
                            </NavLink>
                        </li>
                    )}
                    {token && (
                        <>
                            <li>
                                <NavLink to='/profile' className={({isActive}) =>
                                    isActive ? classes.active : undefined}>
                                    User Page
                                </NavLink>
                            </li>
                            <li>
                                <Form action='/logout' method='post'>
                                    <button>Logout</button>
                                </Form>
                            </li>
                        </>
                    )}
                </ul>
            </nav>
        </header>
    );
}

export default MainNavigation;
