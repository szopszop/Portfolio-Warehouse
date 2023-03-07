import classes from './MainNavigation.module.css';
import {Form, NavLink} from "react-router-dom";

function MainNavigation() {
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
                        <NavLink to='/auth?mode=login' className={({isActive}) =>
                            isActive ? classes.active : undefined}>
                            Login / Register
                        </NavLink>
                    </li>
                    <li>
                        <Form action='/logout' method='post'>
                            <button>Logout</button>
                        </Form>
                    </li>
                </ul>
            </nav>
        </header>
    );
}

export default MainNavigation;
