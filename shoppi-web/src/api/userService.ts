import api from "./axiosInstance";
import {GenderType} from "../types/Gender";

const USER_ME_URL = "/users/me";
const REGISTER_URL = "/users";

const userService = {
    getMe: async () => {
        const response = await api.get(USER_ME_URL);
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

    updateUserInfo: async (userId: number, updatedInfo: { firstName: string; lastName: string; gender?: GenderType }) => {
        const response = await api.put(`/users/${userId}/update-info`, updatedInfo);
        return response.data;
    },

    changeEmail: async (userId: number, newEmail: string, password: string) => {
        const response = await api.put(`/users/${userId}/change-email`, {userId, newEmail, password});
        return response?.status === 200;

    },

    changePassword: async (userId: number, oldPassword: string, newPassword: string) => {
        const response = await api.put(`/users/${userId}/password`, {oldPassword, newPassword});
        return response?.status === 200;
    },
};

export default userService;
