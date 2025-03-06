import api from "./axiosInstance";

const COMPANY_INFO_URL = "/company-info";

const companyService = {
    getCompanyInfo: async (userId: number) => {
        const response = await api.get(`${COMPANY_INFO_URL}/${userId}`);
        return response.data;
    },
};

export default companyService;
