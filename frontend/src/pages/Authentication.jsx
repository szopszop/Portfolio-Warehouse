import AuthForm from '../components/AuthForm';
import {json, redirect} from "react-router-dom";

function AuthenticationPage() {
    return <AuthForm/>;
}

export default AuthenticationPage;

export async function action({request}) {
    const searchParams = new URL(request.url).searchParams;
    const mode = searchParams.get('mode') || 'login';

    if (mode !== 'login' && mode !== 'register') {
        throw json({message: 'Unsupported mode'}, {status: 422})
    }

    const data = await request.formData();
    const authData = {
        email: data.get('email'),
        password: data.get('password')
    };

    const registerPath = '/api/v1/auth/register';
    const response = await fetch(`${process.env.REACT_APP_BACKEND_HOST}` + registerPath, {
        method: "POST",
        credentials: "include",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
        },
        body: JSON.stringify(authData)
    });

    if (response.status === 422 || response.status === 401) {
        return response;
    }

    if (!response.ok) {
        throw json({message: 'Could not authenticate user.'}, {status: 500});
    }

    // mange token
    return redirect('/');
}