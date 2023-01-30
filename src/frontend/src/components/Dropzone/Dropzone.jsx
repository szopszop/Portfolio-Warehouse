import React, {useCallback} from 'react'
import {useDropzone} from 'react-dropzone'
import axios from "axios";

export default function Dropzone({userProfileId}) {

    const onDrop = useCallback(acceptedFiles => {
        const imageFile = acceptedFiles[0];
        const formData = new FormData();
        formData.append("imageFile", imageFile);
        const URL_UPLOAD_IMAGE = `http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`;
        axios.post(
            URL_UPLOAD_IMAGE,
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            }).then(() => {
            console.log("file uploaded successfully");

            }).catch( err => {
                console.log(err);
        });

    }, [])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

    return (
        <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop image here ...</p> :
                    <p>Drag 'n' drop profile image</p>
            }
        </div>
    )
}