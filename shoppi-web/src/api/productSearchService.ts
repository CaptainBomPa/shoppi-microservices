import api from "./axiosInstance";

export type CachedProduct = {
    id: number;
    title: string;
    description: string;
    price: number;
    currency: string;
    categoryId: number;
    userId: number;
};

export type ProductSearchRequest = {
    text?: string;
    minPrice?: number;
    maxPrice?: number;
    currency?: string;
    categoryId?: number;
    userId?: number;
};

const SEARCH_URL = "/cache/products/search";

const productSearchService = {
    searchProducts: async (filters: ProductSearchRequest): Promise<CachedProduct[]> => {
        const params = new URLSearchParams();

        Object.entries(filters).forEach(([key, value]) => {
            if (value !== undefined && value !== null && value !== "") {
                params.append(key, value.toString());
            }
        });

        const response = await api.get(`${SEARCH_URL}?${params.toString()}`);
        return response.data;
    },
};

export default productSearchService;
