import React, {createContext, useContext, useEffect, useState} from "react";
import authService from "../api/authService";
import userService from "../api/userService";
import companyService from "../api/companyService";

export type User = {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    accountType: "USER" | "SELLER";
    registrationDate: string;
    avatar?: string;
    companyName?: string;
};

type AuthContextType = {
    user: User | null;
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
    const [user, setUser] = useState<User | null>(null);
    const isAuthenticated = !!user;

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const userData = await userService.getMe();

                if (userData.accountType === "SELLER") {
                    try {
                        const companyData = await companyService.getCompanyInfo(userData.id);
                        userData.companyName = companyData.companyName;
                    } catch (error) {
                        console.error("Could not fetch information about the company", error);
                    }
                }

                setUser(userData);
            } catch (error) {
                console.error("Could not fetch information about the user", error);
            }
        };

        if (localStorage.getItem("accessToken")) {
            fetchUser();
        }
    }, []);

    const login = async (email: string, password: string) => {
        await authService.login(email, password);
        const userData = await userService.getMe();

        if (userData.accountType === "SELLER") {
            try {
                const companyData = await companyService.getCompanyInfo(userData.id);
                userData.companyName = companyData.companyName;
            } catch (error) {
                console.error("Could not fetch information about the company", error);
            }
        }

        setUser(userData);
    };

    const logout = () => {
        authService.logout();
        setUser(null);
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
        <AuthContext.Provider value={{user, isAuthenticated, login, logout, register}}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;
