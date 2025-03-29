import React from "react";
import {Controller, useFormContext} from "react-hook-form";
import {ProductFormSchema} from "./productFormSchema";

const ProductPricingSection: React.FC = () => {
    const {
        register,
        control,
        formState: {errors},
    } = useFormContext<ProductFormSchema>();

    return (
        <div className="space-y-4">
            <h3 className="text-xl font-bold">Cena i ilość</h3>

            <div>
                <label className="block font-semibold mb-1">Cena (od 1.00 do 100 000.00)</label>
                <input
                    type="number"
                    step="0.01"
                    min="1"
                    max="100000"
                    {...register("price")}
                    className="w-full px-3 py-2 rounded-lg border dark:border-accent bg-white dark:bg-gray-900"
                />
                {errors.price && <p className="text-red-500 text-sm">{errors.price.message}</p>}
            </div>

            <div>
                <label className="block font-semibold mb-1">Waluta</label>
                <Controller
                    name="currency"
                    control={control}
                    render={({field}) => (
                        <select
                            {...field}
                            className="w-full px-3 py-2 rounded-lg border dark:border-accent bg-white dark:bg-gray-900"
                        >
                            <option value="PLN">PLN</option>
                            <option value="USD">USD</option>
                            <option value="EUR">EUR</option>
                        </select>
                    )}
                />
                {errors.currency && <p className="text-red-500 text-sm">{errors.currency.message}</p>}
            </div>

            <div>
                <label className="block font-semibold mb-1">Ilość sztuk (1 - 9999)</label>
                <input
                    type="number"
                    min="1"
                    max="9999"
                    {...register("quantity")}
                    className="w-full px-3 py-2 rounded-lg border dark:border-accent bg-white dark:bg-gray-900"
                />
                {errors.quantity && <p className="text-red-500 text-sm">{errors.quantity.message}</p>}
            </div>
        </div>
    );
};

export default ProductPricingSection;
