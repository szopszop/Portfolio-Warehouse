import { Link } from 'react-router-dom';

function HomePage() {
    return (
        <>
            <h1>Home Page</h1>
            <p>
                <Link to='/profile'> User Profile Page</Link>
                <hr/>
                <Link to='/login'> Login Page</Link>
            </p>

        </>
    );
}

export default HomePage;