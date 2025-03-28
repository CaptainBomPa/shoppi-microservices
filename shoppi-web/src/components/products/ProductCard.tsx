import React from "react";
import {CachedProduct} from "../../api/productSearchService";
import {motion} from "framer-motion";
import {useNavigate} from "react-router-dom";

type Props = {
    product: CachedProduct;
};

const ProductCard: React.FC<Props> = ({product}) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/products/${product.id}`);
    };

    return (
        <motion.div
            initial={{opacity: 0, y: 20}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.3}}
            whileHover={{scale: 1.05, opacity: 0.95}}
            onClick={handleClick}
            className="relative w-72 min-w-[288px] h-[320px] p-2 my-4 ml-5 bg-white dark:bg-gray-900 rounded-xl shadow-lg border border-gray-200 dark:border-gray-700 flex-shrink-0 flex flex-col
            justify-between transform-gpu will-change-transform cursor-pointer transition hover:bg-gray-100 dark:hover:bg-gray-800 select-none"
        >
            {product.promotedUntil && new Date() < new Date(product.promotedUntil) && (
                <span className="absolute top-2 right-2 z-10 bg-orange-500 text-white text-xs font-semibold px-3 py-1 rounded-full shadow-md">
                    ⭐ Promowane
                </span>
            )}

            <div className="relative rounded-md mb-3 z-0">
                <motion.img
                    whileHover={{scale: 1.05}}
                    transition={{duration: 0.3}}
                    src={`https://picsum.photos/300/200?random=${product.id}`}
                    alt={product.title}
                    className="w-full h-40 object-cover rounded-md"
                />
            </div>

            <div>
                <h3 className="text-xl font-semibold truncate">{product.title}</h3>
                <p className="text-sm text-gray-600 dark:text-gray-300 line-clamp-2">{product.description}</p>
            </div>

            <div className="mt-2">
                <p className="font-bold text-accent dark:text-light">
                    {product.price} {product.currency}
                </p>
            </div>
        </motion.div>
    );
};

export default ProductCard;
