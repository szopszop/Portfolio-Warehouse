import { Link } from 'react-router-dom';

function HomePage() {
    return (
        <>
            <h1>Home Page</h1>
            <button onClick={() => {
            fetch("http://localhost:8080/string").then(data => console.log(data)).catch(err => console.log(err))}
            }>BUTTON</button>

        </>
    );
}

export default HomePage;