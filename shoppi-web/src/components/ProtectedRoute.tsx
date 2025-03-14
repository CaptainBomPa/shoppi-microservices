import React from "react";
import {Navigate, Outlet} from "react-router-dom";
import {useUser} from "../context/UserContext";

type ProtectedRouteProps = {
    allowedAccountTypes: ("USER" | "SELLER")[];
    redirectPath: string;
};

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({allowedAccountTypes, redirectPath}) => {
    const {user} = useUser();

    if (!user || !allowedAccountTypes.includes(user.accountType)) {
        return <Navigate to={redirectPath} replace/>;
    }

    return <Outlet/>;
};

export default ProtectedRoute;
