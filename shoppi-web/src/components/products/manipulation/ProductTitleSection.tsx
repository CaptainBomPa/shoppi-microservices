import React from "react";
import {useFormContext} from "react-hook-form";
import {ProductFormSchema} from "./productFormSchema";

const ProductTitleSection: React.FC = () => {
    const {
        register,
        formState: {errors},
    } = useFormContext<ProductFormSchema>();

    return (
        <div>
            <h2 className="text-xl font-bold mb-2">Tytuł</h2>
            <input
                {...register("title")}
                className="w-full p-2 border rounded dark:bg-gray-900"
                placeholder="Wprowadź tytuł oferty"
            />
            {errors.title && <p className="text-red-500 text-sm mt-1">{errors.title.message}</p>}
        </div>
    );
};

export default ProductTitleSection;
