import React, {createContext, useContext, useEffect, useState} from "react";
import userService from "../api/userService";
import companyService from "../api/companyService";
import {GenderType} from "../types/Gender";

export type User = {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    accountType: "USER" | "SELLER";
    registrationDate: string;
    avatar?: string;
    gender: GenderType;
    companyName?: string;
};

type UserContextType = {
    user: User | null;
    refreshUser: () => Promise<void>;
    updateUserInfo: (updatedInfo: { firstName: string; lastName: string; gender?: GenderType }) => Promise<void>;
    changeEmail: (newEmail: string, password: string) => Promise<boolean>;
    changePassword: (oldPassword: string, newPassword: string) => Promise<boolean>;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

export const useUser = () => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUser must be inside UserProvider");
    }
    return context;
};

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const [user, setUser] = useState<User | null>(null);

    const refreshUser = async () => {
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

    useEffect(() => {
        if (localStorage.getItem("accessToken")) {
            refreshUser();
        }
    }, []);

    const updateUserInfo = async (updatedInfo: { firstName: string; lastName: string; gender?: GenderType }) => {
        if (!user) return;
        await userService.updateUserInfo(user.id, updatedInfo);
        await refreshUser();
    };

    const changeEmail = async (newEmail: string, password: string) => {
        if (!user) return false;
        const success = await userService.changeEmail(user.id, newEmail, password);
        if (success) {
            localStorage.removeItem("accessToken");
            window.location.reload();
            return true;
        }
        return false;

    };

    const changePassword = async (oldPassword: string, newPassword: string) => {
        if (!user) return false;
        const success = await userService.changePassword(user.id, oldPassword, newPassword);
        if (success) {
            localStorage.removeItem("accessToken");
            window.location.reload();
            return true;
        } else {
            return false;
        }
    };

    return (
        <UserContext.Provider value={{user, refreshUser, updateUserInfo, changeEmail, changePassword}}>
            {children}
        </UserContext.Provider>
    );
};

export default UserContext;
