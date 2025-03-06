import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

type ProtectedRouteProps = {
    allowedAccountType: "USER" | "SELLER";
    redirectPath: string;
};

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedAccountType, redirectPath }) => {
    const { user } = useAuth();

    if (!user || user.accountType !== allowedAccountType) {
        return <Navigate to={redirectPath} replace />;
    }

    return <Outlet />;
};

export default ProtectedRoute;
