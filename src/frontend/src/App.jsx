import React, {useState, useEffect} from "react";
import './App.css'
import axios from "axios";

const UserProfiles = () => {
    const fetchUserProfiles = () => {
        axios.get("http://localhost:8080/api/v1/user-profile").then( res => {
            console.log(res);
        });
    }

    useEffect( () => {
        fetchUserProfiles();
    }, [])
}


function App() {

    return (
        <div className="App">
            <div>
                <UserProfiles />
            </div>
        </div>
    )
}

export default App
