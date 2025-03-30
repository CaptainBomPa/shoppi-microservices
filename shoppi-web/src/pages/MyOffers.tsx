import React, {useEffect, useState} from "react";
import {useUser} from "../context/UserContext";
import productService, {ProductResponse, ProductStatus} from "../api/productService";
import MyOfferCard from "../components/products/MyOfferCard";
import {useLocation, useNavigate} from "react-router-dom";
import ProductStatusFilter from "../components/products/ProductStatusFilter";
import ConfirmDeleteDialog from "../components/products/ConfirmDeleteDialog";
import {motion} from "framer-motion";
import CustomAlert from "../components/CustomAlert";

const MyOffers = () => {
    const {user} = useUser();
    const [products, setProducts] = useState<ProductResponse[]>([]);
    const [filteredStatuses, setFilteredStatuses] = useState<ProductStatus[]>([
        "ACTIVE",
        "DRAFT",
        "EXPIRED",
        "SOLD"
    ]);
    const navigate = useNavigate();
    const [productToDelete, setProductToDelete] = useState<number | null>(null);
    const location = useLocation();
    const successMessage = location.state?.success;
    const [alertVisible, setAlertVisible] = useState(!!successMessage);

    useEffect(() => {
        if (user) {
            productService.getProductsByUserId(user.id).then(setProducts).catch(console.error);
        }
    }, [user]);

    const handleDelete = async (id: number) => {
        try {
            await productService.deleteProduct(id);
            setProducts(prev => prev.filter(p => p.id !== id));
        } catch (err) {
            console.error("Błąd przy usuwaniu produktu:", err);
        } finally {
            setProductToDelete(null);
        }
    };

    const handleEdit = (id: number) => {
        navigate(`/my-offers/edit/${id}`);
    };

    const filtered = products.filter(p => filteredStatuses.includes(p.status));

    return (
        <motion.div
            initial={{opacity: 0, y: 30}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.4}}
            className="p-6 max-w-[1600px] mx-auto"
        >
            {alertVisible && successMessage && (
                <CustomAlert message={successMessage} type="success" onClose={() => setAlertVisible(false)}/>
            )}

            <div className="flex flex-col lg:flex-row gap-6">
                <ProductStatusFilter
                    selected={filteredStatuses}
                    onChange={setFilteredStatuses}
                />

                <div className="flex-grow bg-gray-50 dark:bg-gray-800 rounded-2xl shadow p-6">
                    <div className="flex justify-between items-center mb-4">
                        <h1 className="text-2xl font-bold">Moje oferty</h1>
                        <button
                            onClick={() => navigate("/my-offers/add")}
                            className="bg-accent text-white px-4 py-2 rounded-xl font-semibold hover:brightness-110 transition"
                        >
                            Dodaj ofertę
                        </button>
                    </div>

                    {filtered.length === 0 ? (
                        <p className="text-gray-500 dark:text-gray-400">Brak pasujących ofert.</p>
                    ) : (
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-6">
                            {filtered.map(product => (
                                <MyOfferCard
                                    key={product.id}
                                    product={product}
                                    onEdit={handleEdit}
                                    onDelete={() => setProductToDelete(product.id)}
                                />
                            ))}
                        </div>
                    )}
                </div>
            </div>
            <ConfirmDeleteDialog
                open={productToDelete !== null}
                onClose={() => setProductToDelete(null)}
                onConfirm={() => {
                    if (productToDelete !== null) handleDelete(productToDelete);
                }}
            />

        </motion.div>
    );
};

export default MyOffers;
