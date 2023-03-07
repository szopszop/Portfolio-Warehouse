import { Link } from 'react-router-dom';
import {getAuthToken} from "../util/auth.jsx";

function HomePage() {
    return (
        <>
            <h1>Home Page</h1>

            {/*<button onClick={response}>BUTTON</button>*/}

        </>
    );
}

export default HomePage;


// const response = await fetch('http://localhost:8080' + '/api/v1/test', {
//     method: "GET",
//     credentials: "include",
//     headers: {
//         'Authorization': 'Bearer ' + getAuthToken(),
//         Accept: "application/json",
//         "Content-Type": "application/json",
//         "Access-Control-Allow-Origin": "*",
//     }
// }).then(res => console.log(res)).catch((err) => console.log("err" + err + "\n TOKEN" + getAuthToken()));