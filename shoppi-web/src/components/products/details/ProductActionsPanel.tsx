import React, {useEffect, useState} from "react";
import {ProductResponse} from "../../../api/productService";
import {Heart, HeartOff} from "lucide-react";
import {useAuth} from "../../../context/AuthContext";

type Props = {
    product: ProductResponse;
};

const LOCAL_STORAGE_KEY = "shoppi_favourites";

const ProductActionsPanel = ({product}: Props) => {
    const {isAuthenticated} = useAuth();
    const [selectedQuantity, setSelectedQuantity] = useState(1);
    const [isFavorite, setIsFavorite] = useState(false);

    useEffect(() => {
        if (isAuthenticated) {
            const favs = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY) || "[]") as number[];
            setIsFavorite(favs.includes(product.id));
        }
    }, [product.id, isAuthenticated]);

    const toggleFavorite = () => {
        if (!isAuthenticated) return;

        const favs = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY) || "[]") as number[];
        let updated;
        if (favs.includes(product.id)) {
            updated = favs.filter(id => id !== product.id);
            setIsFavorite(false);
        } else {
            updated = [...favs, product.id];
            setIsFavorite(true);
        }
        localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(updated));
    };

    return (
        <div className="col-span-1 bg-gray-50 dark:bg-gray-800 rounded-2xl shadow p-6 h-full flex flex-col justify-between">
            <div>
                <div className="flex items-center justify-between mb-2">
                    <p className="text-xl font-semibold text-primary dark:text-light">
                        {product.price} {product.currency}
                    </p>
                    {product.promotedUntil && (
                        <span className="bg-orange-400 text-white text-xs font-semibold px-3 py-1 rounded-full shadow">
                            ⭐ Promowane
                        </span>
                    )}
                </div>

                <p className="text-sm text-gray-600 dark:text-gray-400 mb-4">
                    {product.quantity > 0
                        ? `Dostępnych: ${product.quantity} szt.`
                        : "Wyprzedane ❌"}
                </p>

                {product.quantity > 0 && (
                    <div className="mb-4">
                        <label className="block mb-1 text-sm text-gray-700 dark:text-gray-300">
                            Ilość
                        </label>
                        <select
                            value={selectedQuantity}
                            onChange={(e) => setSelectedQuantity(Number(e.target.value))}
                            className="w-full p-2 border rounded-lg dark:bg-gray-900 dark:text-light dark:border-gray-700"
                            disabled={!isAuthenticated}
                        >
                            {Array.from({length: product.quantity}, (_, i) => (
                                <option key={i + 1} value={i + 1}>
                                    {i + 1}
                                </option>
                            ))}
                        </select>
                    </div>
                )}

                <button
                    disabled={!isAuthenticated || product.quantity === 0}
                    className="w-full bg-accent text-white font-semibold py-2 rounded-xl hover:brightness-110 transition mb-3 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    Kup teraz
                </button>
                <button
                    disabled={!isAuthenticated || product.quantity === 0}
                    className="w-full border border-accent text-accent dark:text-light py-2 rounded-xl hover:bg-accent hover:text-white transition disabled:opacity-50 disabled:cursor-not-allowed mb-3"
                >
                    Dodaj do koszyka
                </button>

                <button
                    onClick={toggleFavorite}
                    disabled={!isAuthenticated}
                    className="w-full flex items-center justify-center border border-gray-300 dark:border-gray-700 rounded-xl py-2 hover:bg-gray-200 dark:hover:bg-gray-700 transition text-sm disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    {isFavorite ? (
                        <>
                            <HeartOff className="w-4 h-4 mr-2"/> Usuń z ulubionych
                        </>
                    ) : (
                        <>
                            <Heart className="w-4 h-4 mr-2"/> Dodaj do ulubionych
                        </>
                    )}
                </button>

                {!isAuthenticated && (
                    <p className="mt-4 text-sm text-red-500 text-center">
                        Aby zakupić przedmiot lub dodać do ulubionych, musisz się zalogować.
                    </p>
                )}
            </div>

            <p className="text-sm text-gray-500 dark:text-gray-400 mt-6">
                Dostawa w 2–4 dni robocze 📦
            </p>
        </div>
    );
};

export default ProductActionsPanel;
