import api from "./axiosInstance";
import userService from "./userService";

const SHIPPING_INFO_URL = "/shipping-info";

const shippingService = {
    addShippingInfo: async (userId: number, shippingData: {
        firstName: string;
        lastName: string;
        postalCode: string;
        city: string;
        street: string;
        country: string;
        countryCode: string;
        phone: string;
    }) => {
        const response = await api.post(`${SHIPPING_INFO_URL}/${userId}`, shippingData);
        await userService.getMe();
        return response.data;
    },

    updateShippingInfo: async (userId: number, shippingInfoId: number, updatedData: {
        firstName: string;
        lastName: string;
        postalCode: string;
        city: string;
        street: string;
        country: string;
        countryCode: string;
        phone: string;
    }) => {
        const response = await api.put(`${SHIPPING_INFO_URL}/${userId}/${shippingInfoId}`, updatedData);
        await userService.getMe();
        return response.data;
    },

    deleteShippingInfo: async (userId: number, shippingInfoId: number) => {
        await api.delete(`${SHIPPING_INFO_URL}/${userId}/${shippingInfoId}`);
        await userService.getMe();
    },
};

export default shippingService;
