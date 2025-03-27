import api from "./axiosInstance";

export type Category = {
    id: number;
    name: string;
    subcategories?: Category[];
};

const categoryService = {
    getAllCategories: async (): Promise<Category[]> => {
        const response = await api.get("/categories");
        return response.data;
    },
};

export default categoryService;
