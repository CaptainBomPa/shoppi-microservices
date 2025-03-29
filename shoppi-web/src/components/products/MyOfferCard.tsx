import React from "react";
import {ProductResponse, ProductStatus} from "../../api/productService";
import {motion} from "framer-motion";
import {Pencil, RotateCcw, Trash2} from "lucide-react";

type Props = {
    product: ProductResponse;
    onEdit: (id: number) => void;
    onDelete: (id: number) => void;
};

const statusColors: Record<ProductStatus, string> = {
    ACTIVE: "bg-green-500",
    DRAFT: "bg-blue-500",
    EXPIRED: "bg-red-500",
    SOLD: "bg-yellow-500",
};

const statusLabels: Record<ProductStatus, string> = {
    ACTIVE: "Aktywny",
    DRAFT: "Szkic",
    EXPIRED: "Wygasłe",
    SOLD: "Sprzedane",
};

const MyOfferCard: React.FC<Props> = ({product, onEdit, onDelete}) => {
    const isPromoted = product.promotedUntil && new Date() < new Date(product.promotedUntil);
    const isSold = product.status === "SOLD";
    const isExpired = product.status === "EXPIRED";

    const handleRefresh = () => {
        console.log(`Odświeżanie produktu o ID: ${product.id}`);
    };

    const handleFinalize = () => {
        console.log(`Finalizacja sprzedaży: ID ${product.id}`);
    };

    return (
        <motion.div
            initial={{opacity: 0, y: 20}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.3}}
            className="relative w-72 min-w-[288px] h-[360px] p-4 bg-white dark:bg-gray-900 rounded-xl shadow-lg border border-gray-200 dark:border-gray-700 flex-shrink-0 flex flex-col justify-between"
        >
            <div className="absolute top-2 right-2 flex gap-2 z-10">
                {isPromoted && (
                    <span className="bg-orange-500 text-white text-xs font-semibold px-3 py-1 rounded-full shadow-md">
                        ⭐ Promowane
                    </span>
                )}
                <span className={`${statusColors[product.status]} text-white text-xs font-semibold px-3 py-1 rounded-full shadow-md`}>
                    {statusLabels[product.status]}
                </span>
            </div>

            <img
                src={`https://picsum.photos/300/200?random=${product.id}`}
                alt={product.title}
                className="w-full h-40 object-cover rounded-md mb-3"
            />

            <div className="mb-3">
                <h3 className="text-xl font-semibold truncate">{product.title}</h3>
                <p className="text-sm text-gray-600 dark:text-gray-300 line-clamp-2">{product.description}</p>
            </div>

            <p className="font-bold text-accent dark:text-light mb-3">
                {product.price} {product.currency}
            </p>

            <div className="flex justify-between gap-2">
                {isSold ? (
                    <button
                        onClick={handleFinalize}
                        className="w-full bg-gradient-to-r from-green-500 to-green-600 text-white font-semibold py-2 rounded-xl hover:brightness-110 transition"
                    >
                        Finalizacja sprzedaży
                    </button>
                ) : isExpired ? (
                    <>
                        <button
                            onClick={handleRefresh}
                            className="flex-1 bg-gradient-to-r from-blue-500 to-blue-600 text-white font-semibold py-2 rounded-xl hover:brightness-110 transition"
                        >
                            <RotateCcw size={16} className="inline-block mr-1"/>
                            Odśwież
                        </button>
                        <button
                            onClick={() => onDelete(product.id)}
                            className="flex-1 bg-gradient-to-r from-red-500 to-red-600 text-white font-semibold py-2 rounded-xl hover:brightness-110 transition"
                        >
                            <Trash2 size={16} className="inline-block mr-1"/>
                            Usuń
                        </button>
                    </>
                ) : (
                    <>
                        <button
                            onClick={() => onEdit(product.id)}
                            className="flex-1 bg-gradient-to-r from-accent to-accent/80 text-white font-semibold py-2 rounded-xl hover:brightness-110 transition"
                        >
                            <Pencil size={16} className="inline-block mr-1"/>
                            Edytuj
                        </button>
                        <button
                            onClick={() => onDelete(product.id)}
                            className="flex-1 bg-gradient-to-r from-red-500 to-red-600 text-white font-semibold py-2 rounded-xl hover:brightness-110 transition"
                        >
                            <Trash2 size={16} className="inline-block mr-1"/>
                            Usuń
                        </button>
                    </>
                )}
            </div>
        </motion.div>
    );
};

export default MyOfferCard;
