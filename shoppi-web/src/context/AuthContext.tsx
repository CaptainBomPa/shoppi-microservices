import React, {createContext, useContext} from "react";
import authService from "../api/authService";
import userService from "../api/userService";

type AuthContextType = {
    isAuthenticated: boolean;
    login: (email: string, password: string) => Promise<void>;
    logout: () => void;
    register: (userData: {
        email: string;
        firstName: string;
        lastName: string;
        password: string;
        accountType: "INDYWIDUALNY" | "FIRMA";
    }) => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be inside AuthProvider");
    }
    return context;
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const isAuthenticated = !!localStorage.getItem("accessToken");

    const login = async (email: string, password: string) => {
        await authService.login(email, password);
        window.location.reload();
    };

    const logout = () => {
        authService.logout();
        localStorage.removeItem("accessToken");
        window.location.reload();
    };

    const register = async (userData: {
        email: string;
        firstName: string;
        lastName: string;
        password: string;
        accountType: "INDYWIDUALNY" | "FIRMA";
    }) => {
        await userService.registerUser(userData);
    };

    return (
        <AuthContext.Provider value={{isAuthenticated, login, logout, register}}>
            {children}
        </AuthContext.Provider>
    );
};
