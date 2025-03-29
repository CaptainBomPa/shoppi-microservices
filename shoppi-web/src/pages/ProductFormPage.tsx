import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import productService, {ProductResponse} from "../api/productService";

const ProductFormPage = () => {
    const {id} = useParams();
    const isEditMode = !!id;
    const [product, setProduct] = useState<ProductResponse | null>(null);

    useEffect(() => {
        if (isEditMode) {
            productService.getProductById(Number(id))
                .then(setProduct)
                .catch((err) => console.error("Błąd ładowania produktu:", err));
        }
    }, [id]);

    return (
        <div className="p-6 max-w-4xl mx-auto">
            <h1 className="text-2xl font-bold mb-4">
                {isEditMode ? `Edycja produktu #${id}` : "Dodaj nową ofertę"}
            </h1>

            {isEditMode && !product && (
                <p className="text-red-500">Nie znaleziono produktu lub wystąpił błąd.</p>
            )}

            <div className="text-gray-500 dark:text-gray-300">
                Wkrótce tu będzie formularz ✨
            </div>
        </div>
    );
};

export default ProductFormPage;
