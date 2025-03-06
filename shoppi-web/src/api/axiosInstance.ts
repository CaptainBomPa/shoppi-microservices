import axios from "axios";
import authService from "./authService";

const api = axios.create({
    baseURL: "http://localhost:8080/api/v1",
    headers: {"Content-Type": "application/json"},
    withCredentials: true,
});

let isRefreshing = false;
let refreshSubscribers: ((token: string) => void)[] = [];

api.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            if (isRefreshing) {
                return new Promise((resolve) => {
                    refreshSubscribers.push((token) => {
                        originalRequest.headers.Authorization = `Bearer ${token}`;
                        resolve(api(originalRequest));
                    });
                });
            }

            originalRequest._retry = true;
            isRefreshing = true;

            try {
                const newToken = await authService.refreshToken();
                originalRequest.headers.Authorization = `Bearer ${newToken}`;
                isRefreshing = false;
                return api(originalRequest);
            } catch (err) {
                isRefreshing = false;
                return Promise.reject(err);
            }
        }

        return Promise.reject(error);
    }
);

export default api;
