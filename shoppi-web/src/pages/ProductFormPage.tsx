import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import productService, {ProductRequest, ProductResponse} from "../api/productService";
import ProductForm from "../components/products/manipulation/ProductForm";
import {ProductFormSchema} from "../components/products/manipulation/productFormSchema";
import {motion} from "framer-motion";

const ProductFormPage = () => {
    const {id} = useParams();
    const isEditMode = !!id;
    const [product, setProduct] = useState<ProductResponse | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (isEditMode) {
            productService
                .getProductById(Number(id))
                .then(setProduct)
                .catch((err) => console.error("Błąd ładowania produktu:", err));
        }
    }, [id]);

    const handleSubmit = async (data: ProductFormSchema) => {
        const today = new Date();
        const expiresAt = new Date();
        expiresAt.setDate(today.getDate() + (data.expiresAt || 7));

        const payload: ProductRequest = {
            ...data,
            expiresAt: expiresAt.toISOString().split(".")[0],
        };

        try {
            if (isEditMode && id) {
                await productService.updateProduct(Number(id), payload);
            } else {
                await productService.createProduct(payload);
            }
            navigate("/my-offers");
        } catch (err) {
            console.error("Błąd podczas zapisu produktu:", err);
        }
    };


    return (
        <motion.div
            initial={{opacity: 0, y: 30}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.4}}
            className="p-6 max-w-[1600px] mx-auto"
        >
            <div className="bg-gray-50 dark:bg-gray-800 rounded-2xl shadow p-6">
                <h1 className="text-2xl font-bold mb-6">
                    {isEditMode ? `Edycja produktu #${id}` : "Dodaj nową ofertę"}
                </h1>

                {isEditMode && !product ? (
                    <p className="text-red-500">Nie znaleziono produktu lub wystąpił błąd.</p>
                ) : (
                    <ProductForm
                        defaultValues={
                            isEditMode
                                ? {
                                    ...product,
                                    expiresAt:
                                        new Date(product!.expiresAt).getDate() -
                                        new Date().getDate(), // to można potem ulepszyć
                                }
                                : undefined
                        }
                        onSubmit={handleSubmit}
                    />
                )}
            </div>
        </motion.div>

    );
};

export default ProductFormPage;
