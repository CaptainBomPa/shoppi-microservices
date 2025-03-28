import React, {useEffect, useState} from "react";
import {useSearchParams} from "react-router-dom";
import productSearchService, {CachedProduct, ProductSearchRequest,} from "../api/productSearchService";
import ProductCard from "../components/products/ProductCard";
import SearchFilters from "../components/search/SearchFilter";

const ITEMS_PER_PAGE = 48;

const SearchPage = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const [results, setResults] = useState<CachedProduct[]>([]);
    const [loading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);

    const filters: ProductSearchRequest = {
        text: searchParams.get("title") || undefined,
        minPrice: searchParams.get("minPrice") ? Number(searchParams.get("minPrice")) : undefined,
        maxPrice: searchParams.get("maxPrice") ? Number(searchParams.get("maxPrice")) : undefined,
        currency: searchParams.get("currency") || undefined,
        categoryId: searchParams.get("categoryId") ? Number(searchParams.get("categoryId")) : undefined,
        userId: searchParams.get("userId") ? Number(searchParams.get("userId")) : undefined,
    };

    useEffect(() => {
        const fetchResults = async () => {
            setLoading(true);
            try {
                const data = await productSearchService.searchProducts(filters);
                const sorted = [...data].sort((a, b) => {
                    if (a.promotedUntil && !b.promotedUntil) return -1;
                    if (!a.promotedUntil && b.promotedUntil) return 1;
                    return 0;
                });
                setResults(sorted);
                setCurrentPage(1);
            } catch (err) {
                console.error("Błąd podczas pobierania wyników:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchResults();
    }, [searchParams]);

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        const container = document.getElementById("scroll-container");
        if (container) {
            container.scrollTo({top: 0, behavior: "smooth"});
        }
    };

    const handleFilterChange = (key: keyof ProductSearchRequest, value: string | undefined) => {
        const newParams = new URLSearchParams(searchParams.toString());
        if (value) {
            newParams.set(key, value);
        } else {
            newParams.delete(key);
        }
        setSearchParams(newParams);
    };

    const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
    const paginatedResults = results.slice(
        (currentPage - 1) * ITEMS_PER_PAGE,
        currentPage * ITEMS_PER_PAGE
    );

    return (
        <div className="p-6 max-w-[1600px] mx-auto">
            <div className="flex flex-col lg:flex-row gap-6">
                <SearchFilters filters={filters} onFilterChange={handleFilterChange}/>

                <div className="flex-grow bg-gray-50 dark:bg-gray-800 rounded-2xl shadow p-6">
                    <h1 className="text-2xl font-bold mb-4">Wyniki wyszukiwania</h1>

                    {loading ? (
                        <div className="w-full h-64 flex justify-center items-center">
                            <div className="w-12 h-12 border-4 border-gray-300 border-t-accent dark:border-t-light rounded-full animate-spin"/>
                        </div>
                    ) : results.length === 0 ? (
                        <p className="text-gray-500 dark:text-gray-400">Brak wyników pasujących do zapytania.</p>
                    ) : (
                        <>
                            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
                                {paginatedResults.map((product) => (
                                    <ProductCard key={product.id} product={product}/>
                                ))}
                            </div>

                            {totalPages > 1 && (
                                <div className="flex justify-center space-x-2">
                                    {Array.from({length: totalPages}, (_, i) => (
                                        <button
                                            key={i}
                                            onClick={() => handlePageChange(i + 1)}
                                            className={`px-4 py-2 rounded-lg font-semibold transition ${
                                                currentPage === i + 1
                                                    ? "bg-accent text-white"
                                                    : "bg-gray-200 dark:bg-gray-700 text-gray-800 dark:text-light hover:bg-accent hover:text-white"
                                            }`}
                                        >
                                            {i + 1}
                                        </button>
                                    ))}
                                </div>
                            )}
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

export default SearchPage;
