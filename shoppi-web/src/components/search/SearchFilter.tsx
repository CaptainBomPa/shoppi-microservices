import React from "react";
import {ProductSearchRequest} from "../../api/productSearchService";
import CategoryFilter from "./CategoryFilter";

type Props = {
    filters: ProductSearchRequest;
    onFilterChange: (key: keyof ProductSearchRequest, value: string | undefined) => void;
};

const SearchFilters: React.FC<Props> = ({filters, onFilterChange}) => {
    return (
        <div className="w-full lg:w-[280px] bg-gray-50 dark:bg-gray-800 rounded-2xl shadow p-4 space-y-4">
            <h2 className="text-xl font-bold">Filtry</h2>

            <div>
                <label className="block text-sm font-semibold mb-1">Cena minimalna</label>
                <input
                    type="number"
                    value={filters.minPrice ?? ""}
                    onChange={(e) => onFilterChange("minPrice", e.target.value)}
                    className="w-full px-3 py-2 rounded-lg border dark:border-accent bg-white dark:bg-gray-900"
                />
            </div>

            <div>
                <label className="block text-sm font-semibold mb-1">Cena maksymalna</label>
                <input
                    type="number"
                    value={filters.maxPrice ?? ""}
                    onChange={(e) => onFilterChange("maxPrice", e.target.value)}
                    className="w-full px-3 py-2 rounded-lg border dark:border-accent bg-white dark:bg-gray-900"
                />
            </div>

            <div>
                <label className="block text-sm font-semibold mb-1">Waluta</label>
                <select
                    value={filters.currency ?? ""}
                    onChange={(e) => onFilterChange("currency", e.target.value || undefined)}
                    className="w-full px-3 py-2 rounded-lg border dark:border-accent bg-white dark:bg-gray-900"
                >
                    <option value="">Dowolna</option>
                    <option value="PLN">PLN</option>
                    <option value="USD">USD</option>
                    <option value="EUR">EUR</option>
                </select>
            </div>

            <div>
                <label className="block text-sm font-semibold mb-1">Kategoria</label>
                <CategoryFilter
                    selectedCategoryId={filters.categoryId}
                    onCategoryChange={(id) => onFilterChange("categoryId", id?.toString())}
                />
            </div>
        </div>
    );
};

export default SearchFilters;
