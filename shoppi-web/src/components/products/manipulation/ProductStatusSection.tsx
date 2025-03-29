import React from "react";
import {useFormContext} from "react-hook-form";
import {Info} from "lucide-react";
import {ProductFormSchema} from "./productFormSchema";

const ProductStatusSection: React.FC = () => {
    const {register, watch} = useFormContext<ProductFormSchema>();
    const selected = watch("status");

    return (
        <div className="mb-6">
            <h2 className="text-lg font-bold mb-2">Status oferty</h2>

            <div className="flex items-center text-sm text-gray-600 dark:text-gray-300 mb-4">
                <Info size={16} className="mr-2 text-accent"/>
                Tryb szkic pozwala przygotować ofertę i opublikować ją później. Tryb aktywny od razu wystawia ofertę publicznie.
            </div>

            <div className="flex gap-4">
                <label className={`flex items-center gap-2 px-4 py-2 rounded-lg border cursor-pointer transition ${
                    selected === "DRAFT" ? "bg-accent text-white" : "hover:bg-gray-100 dark:hover:bg-gray-700"
                }`}>
                    <input
                        type="radio"
                        value="DRAFT"
                        {...register("status")}
                        className="hidden"
                    />
                    Szkic
                </label>

                <label className={`flex items-center gap-2 px-4 py-2 rounded-lg border cursor-pointer transition ${
                    selected === "ACTIVE" ? "bg-accent text-white" : "hover:bg-gray-100 dark:hover:bg-gray-700"
                }`}>
                    <input
                        type="radio"
                        value="ACTIVE"
                        {...register("status")}
                        className="hidden"
                    />
                    Aktywny
                </label>
            </div>
        </div>
    );
};

export default ProductStatusSection;
