import React from "react";
import {useFormContext} from "react-hook-form";
import {ProductFormSchema} from "./productFormSchema";

const options = [
    {days: 7, label: "7 dni (darmowe)", disabled: false},
    {days: 14, label: "14 dni (14.99 PLN)", disabled: true},
    {days: 31, label: "31 dni (24.99 PLN)", disabled: true},
];

const ProductDurationSection: React.FC = () => {
    const {watch, setValue, formState: {errors}} = useFormContext<ProductFormSchema>();
    const selected = watch("expiresAt");

    const handleChange = (value: number) => {
        setValue("expiresAt", value, {shouldValidate: true});
    };

    return (
        <div className="mb-6">
            <h2 className="text-lg font-bold mb-2">Długość oferty</h2>
            <div className="space-y-2">
                {options.map((opt) => (
                    <label
                        key={opt.days}
                        className={`flex items-center gap-2 p-2 rounded-lg border cursor-pointer transition ${
                            opt.disabled
                                ? "bg-gray-100 dark:bg-gray-700 opacity-60 cursor-not-allowed"
                                : "hover:bg-gray-100 dark:hover:bg-gray-700"
                        }`}
                    >
                        <input
                            type="radio"
                            value={opt.days}
                            checked={selected === opt.days}
                            onChange={() => handleChange(opt.days)}
                            disabled={opt.disabled}
                        />
                        {opt.label}
                    </label>
                ))}
                {errors.expiresAt && (
                    <p className="text-red-500 text-sm">{errors.expiresAt.message}</p>
                )}
            </div>
        </div>
    );
};

export default ProductDurationSection;
