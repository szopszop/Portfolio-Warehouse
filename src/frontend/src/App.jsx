import React, {useState, useEffect} from "react";
import './App.css'
import axios from "axios";
import Dropzone from "./components/Dropzone/Dropzone.jsx";

const UserProfiles = () => {

    const [userProfiles, setUserProfiles] = useState([]);

    const fetchUserProfiles = () => {
        axios.get("http://localhost:8080/api/v1/user-profile").then( res => {
            console.log(res);
            setUserProfiles(res.data)
        });
    }

    useEffect( () => {
        fetchUserProfiles();
    }, [])

    return userProfiles.map( (userProfile) => {

        return (<div key={userProfile.userProfileId}>
            {userProfile.userProfileImageLink ?
                <img className="profile-picture" src={`http://localhost:8080/api/v1/user-profile/${userProfile.userProfileId}/image/download`}
                     alt="profile-picture"/> : null}
            <br />
            <br />

            <h1>{userProfile.username}</h1>
            <p>{userProfile.userProfileId}</p>
            <Dropzone userProfileId={userProfile.userProfileId}/>
            <br />
        </div>)
    })
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
