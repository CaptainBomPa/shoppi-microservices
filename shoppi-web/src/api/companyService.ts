import api from "./axiosInstance";

const COMPANY_INFO_URL = "/company-info";

const companyService = {
    addCompanyInfo: async (userId: number, companyData: {
        companyName: string;
        postalCode: string;
        city: string;
        street: string;
        country: string;
        countryCode: string;
        phone: string;
    }) => {
        try {
            const response = await api.post(`${COMPANY_INFO_URL}/${userId}`, companyData);
            return response.data;
        } catch (error: any) {
            console.error("[companyService] addCompanyInfo Error:", error.response?.data || error.message);
            throw error;
        }
    },

    updateCompanyInfo: async (userId: number, companyData: {
        companyName: string;
        postalCode: string;
        city: string;
        street: string;
        country: string;
        countryCode: string;
        phone: string;
    }) => {
        try {
            const response = await api.put(`${COMPANY_INFO_URL}/${userId}`, companyData);
            return response.data;
        } catch (error: any) {
            console.error("[companyService] updateCompanyInfo Error:", error.response?.data || error.message);
            throw error;
        }
    },
};

export default companyService;
