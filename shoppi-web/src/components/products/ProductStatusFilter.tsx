import React from "react";
import {ProductStatus} from "../../api/productService";

type Props = {
    selected: ProductStatus[];
    onChange: (statuses: ProductStatus[]) => void;
};

const STATUS_LABELS: Record<ProductStatus, string> = {
    ACTIVE: "Aktywny",
    DRAFT: "Szkic",
    EXPIRED: "Wygasłe",
    SOLD: "Sprzedane",
};

const ProductStatusFilter: React.FC<Props> = ({selected, onChange}) => {
    const toggleStatus = (status: ProductStatus) => {
        const updated = selected.includes(status)
            ? selected.filter((s) => s !== status)
            : [...selected, status];
        onChange(updated);
    };

    return (
        <div className="w-full lg:w-[280px] bg-gray-50 dark:bg-gray-800 rounded-2xl shadow p-4 space-y-4">
            <h2 className="text-xl font-bold">Status oferty</h2>
            {Object.entries(STATUS_LABELS).map(([status, label]) => {
                const isChecked = selected.includes(status as ProductStatus);
                return (
                    <label
                        key={status}
                        className={`flex items-center gap-2 px-2 py-1 rounded-lg cursor-pointer transition ${
                            isChecked ? "bg-violet-500 text-white" : ""
                        }`}
                    >
                        <input
                            type="checkbox"
                            checked={isChecked}
                            onChange={() => toggleStatus(status as ProductStatus)}
                            className="accent-violet-500"
                        />
                        {label}
                    </label>
                );
            })}
        </div>
    );
};

export default ProductStatusFilter;
