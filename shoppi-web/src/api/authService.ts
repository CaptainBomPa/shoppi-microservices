import api from "./axiosInstance";

const LOGIN_URL = "/auth/login";
const REFRESH_URL = "/auth/refresh-token";

const authService = {
    login: async (email: string, password: string) => {
        const response = await api.post(LOGIN_URL, {email, password});
        localStorage.setItem("accessToken", response.data.accessToken);
        localStorage.setItem("refreshToken", response.data.refreshToken);
        return response.data;
    },

    refreshToken: async () => {
        try {
            const refreshToken = localStorage.getItem("refreshToken");
            if (!refreshToken) throw new Error("Missing refresh token. Logging out");

            const response = await api.post(REFRESH_URL, {refreshToken});
            localStorage.setItem("accessToken", response.data.accessToken);
            return response.data.accessToken;
        } catch (error) {
            authService.logout();
            return Promise.reject(error);
        }
    },

    logout: () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.location.href = "/login";
    },
};

export default authService;
