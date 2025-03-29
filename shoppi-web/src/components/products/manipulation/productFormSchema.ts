import * as yup from "yup";
import {Currency, ProductStatus} from "../../../api/productService";

export const productFormSchema = yup.object({
    title: yup.string().required("Tytuł jest wymagany").min(3).max(255),
    status: yup.mixed<ProductStatus>().oneOf(["DRAFT", "ACTIVE"]).required("Status oferty jest wymagany"),
    description: yup.string().required("Opis jest wymagany").min(10),
    expiresAt: yup.number().oneOf([7, 14, 31]),
    price: yup.number().min(1).max(100000).required(),
    currency: yup.mixed<Currency>().oneOf(["PLN", "USD", "EUR"]).required(),
    quantity: yup.number().min(1).max(9999).required(),
    categoryId: yup.number().required(),
    userId: yup.number().required(),
});

export type ProductFormSchema = yup.InferType<typeof productFormSchema>;

