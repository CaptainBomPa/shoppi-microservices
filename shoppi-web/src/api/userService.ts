import api from "./axiosInstance";

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
};

export default userService;
