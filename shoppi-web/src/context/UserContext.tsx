import React, {createContext, useContext, useEffect, useState} from "react";
import userService from "../api/userService";
import shippingService from "../api/shippingService";
import {GenderType} from "../types/Gender";

export type ShippingAddress = {
    id: number;
    firstName: string;
    lastName: string;
    postalCode: string;
    city: string;
    street: string;
    country: string;
    countryCode: string;
    phone: string;
};

export type ShippingAddressFormData = Omit<ShippingAddress, "id">;

export type User = {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    accountType: "USER" | "SELLER";
    registrationDate: string;
    avatar?: string;
    gender: GenderType;
    shippingAddresses?: ShippingAddress[];
    companyInfo?: {
        companyName: string;
        postalCode: string;
        city: string;
        street: string;
        country: string;
        countryCode: string;
        phone: string;
    };
};


type UserContextType = {
    user: User | null;
    refreshUser: () => Promise<void>;
    updateUserInfo: (updatedInfo: { firstName: string; lastName: string; gender?: GenderType }) => Promise<void>;
    changeEmail: (newEmail: string, password: string) => Promise<boolean>;
    changePassword: (oldPassword: string, newPassword: string) => Promise<boolean>;
    addShippingAddress: (address: Omit<ShippingAddress, "id">) => Promise<void>;
    updateShippingAddress: (id: number, address: Omit<ShippingAddress, "id">) => Promise<void>;
    deleteShippingAddress: (id: number) => Promise<void>;
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
        await userService.updateUserInfo(updatedInfo);
        await refreshUser();
    };

    const changeEmail = async (newEmail: string, password: string) => {
        if (!user) return false;
        const success = await userService.changeEmail(newEmail, password);
        if (success) {
            localStorage.removeItem("accessToken");
            window.location.reload();
            return true;
        }
        return false;
    };

    const changePassword = async (oldPassword: string, newPassword: string) => {
        if (!user) return false;
        const success = await userService.changePassword(oldPassword, newPassword);
        if (success) {
            localStorage.removeItem("accessToken");
            window.location.reload();
            return true;
        }
        return false;
    };

    const addShippingAddress = async (address: Omit<ShippingAddress, "id">) => {
        if (!user) return;
        await shippingService.addShippingInfo(user.id, address);
        await refreshUser();
    };

    const updateShippingAddress = async (id: number, address: Omit<ShippingAddress, "id">) => {
        if (!user) return;
        await shippingService.updateShippingInfo(user.id, id, address);
        await refreshUser();
    };

    const deleteShippingAddress = async (id: number) => {
        if (!user) return;
        await shippingService.deleteShippingInfo(user.id, id);
        await refreshUser();
    };

    return (
        <UserContext.Provider
            value={{
                user,
                refreshUser,
                updateUserInfo,
                changeEmail,
                changePassword,
                addShippingAddress,
                updateShippingAddress,
                deleteShippingAddress,
            }}
        >
            {children}
        </UserContext.Provider>
    );
};

export default UserContext;
