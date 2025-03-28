import api from "./axiosInstance";

export type CachedProduct = {
    id: number;
    title: string;
    description: string;
    price: number;
    currency: string;
    categoryId: number;
    userId: number;
    promotedUntil: Date | null;
};

export type ProductSearchRequest = {
    text?: string;
    minPrice?: number;
    maxPrice?: number;
    currency?: string;
    categoryId?: number;
    userId?: number;
};

const productSearchService = {
    searchProducts: async (filters: ProductSearchRequest): Promise<CachedProduct[]> => {
        const params = new URLSearchParams();

        Object.entries(filters).forEach(([key, value]) => {
            if (value !== undefined && value !== null && value !== "") {
                params.append(key, value.toString());
            }
        });

        const response = await api.get(`/cache/products/search?${params.toString()}`);
        return parseCachedProducts(response.data);
    },

    getPromotedForHome: async (): Promise<CachedProduct[]> => {
        const response = await api.get("/cache/products/home/promoted");
        return parseCachedProducts(response.data);
    },

    getPopularCategoryProductsForHome: async (): Promise<CachedProduct[]> => {
        const response = await api.get("/cache/products/home/popular");
        return parseCachedProducts(response.data);
    },
};

const parseCachedProducts = (data: any[]): CachedProduct[] =>
    data.map((item) => ({
        ...item,
        promotedUntil: item.promotedUntil ? new Date(item.promotedUntil) : null,
    }));

export default productSearchService;
