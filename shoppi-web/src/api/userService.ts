import api from "./axiosInstance";
import {GenderType} from "../types/Gender";

const REGISTER_URL = "/users";

const userService = {
    getMe: async () => {
        const userId = localStorage.getItem("userId");
        if (!userId) throw new Error("User ID not found. User might be logged out.");

        const response = await api.get(`/users/${userId}`);
        return response.data;
    },

    getUser: async (id: number) => {
        const response = await api.get(`/users/${id}`);
        return response.data;
    },

    registerUser: async (userData: {
        email: string;
        firstName: string;
        lastName: string;
        password: string;
        accountType: "INDYWIDUALNY" | "FIRMA";
    }) => {
        const transformedData = {
            ...userData,
            accountType: userData.accountType === "INDYWIDUALNY" ? "USER" : "SELLER",
        };

        const response = await api.post(REGISTER_URL, transformedData);
        return response.data;
    },

    updateUserInfo: async (updatedInfo: { firstName: string; lastName: string; gender?: GenderType }) => {
        const userId = localStorage.getItem("userId");
        if (!userId) throw new Error("User ID not found. User might be logged out.");

        const response = await api.put(`/users/${userId}/update-info`, updatedInfo);
        return response.data;
    },

    changeEmail: async (newEmail: string, password: string) => {
        const userId = localStorage.getItem("userId");
        if (!userId) throw new Error("User ID not found. User might be logged out.");

        const response = await api.put(`/users/${userId}/change-email`, {userId, newEmail, password});
        return response?.status === 200;
    },

    changePassword: async (oldPassword: string, newPassword: string) => {
        const userId = localStorage.getItem("userId");
        if (!userId) throw new Error("User ID not found. User might be logged out.");

        const response = await api.put(`/users/${userId}/password`, {oldPassword, newPassword});
        return response?.status === 200;
    },
};

export default userService;
