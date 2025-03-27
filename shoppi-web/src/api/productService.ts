import api from "./axiosInstance";

export type Currency = "PLN" | "USD" | "EUR";
export type ProductStatus = "ACTIVE" | "EXPIRED" | "SOLD" | "DRAFT";

export type ProductResponse = {
    id: number;
    title: string;
    description: string;
    createdAt: string;
    expiresAt: string;
    userId: number;
    price: number;
    currency: Currency;
    status: ProductStatus;
    quantity: number;
    promotedUntil?: string;
    category: {
        id: number;
        name: string;
    };
};

export type ProductRequest = {
    title: string;
    description: string;
    expiresAt: string;
    price: number;
    currency: Currency;
    status: ProductStatus;
    quantity: number;
    categoryId: number;
};

const PRODUCT_URL = "/products";

const productService = {
    getProductById: async (id: number): Promise<ProductResponse> => {
        const response = await api.get(`${PRODUCT_URL}/${id}`);
        return response.data;
    },

    getProductsByIds: async (ids: number[]): Promise<ProductResponse[]> => {
        const params = new URLSearchParams();
        ids.forEach((id) => params.append("ids", id.toString()));

        const response = await api.get(`${PRODUCT_URL}/batch?${params.toString()}`);
        return response.data;
    },

    createProduct: async (productData: ProductRequest): Promise<ProductResponse> => {
        const response = await api.post(PRODUCT_URL, productData);
        return response.data;
    },

    updateProduct: async (id: number, productData: ProductRequest): Promise<ProductResponse> => {
        const response = await api.put(`${PRODUCT_URL}/${id}`, productData);
        return response.data;
    },

    deleteProduct: async (id: number): Promise<void> => {
        await api.delete(`${PRODUCT_URL}/${id}`);
    },
};

export default productService;
