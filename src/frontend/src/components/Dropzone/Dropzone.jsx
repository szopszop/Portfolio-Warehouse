import React, {useCallback} from 'react'
import {useDropzone} from 'react-dropzone'

export default function Dropzone() {

    const onDrop = useCallback(acceptedFiles => {
        const file = acceptedFiles[0];
        console.log(file)
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