import React, {useEffect, useState} from "react";
import {motion} from "framer-motion";
import productSearchService, {CachedProduct} from "../api/productSearchService";
import ProductCard from "../components/products/ProductCard";
import ScrollableSection from "../components/ScrollableSection";

const Home = () => {
    const [promoted, setPromoted] = useState<CachedProduct[]>([]);
    const [popular, setPopular] = useState<CachedProduct[]>([]);

    useEffect(() => {
        productSearchService.getPromotedForHome().then(setPromoted);
        productSearchService.getPopularCategoryProductsForHome().then(setPopular);
    }, []);

    const renderSection = (
        title: string,
        emoji: string,
        gradientFrom: string,
        gradientTo: string,
        products: CachedProduct[]
    ) => (
        <motion.div
            className="mb-12 w-full bg-white dark:bg-gray-900 rounded-xl shadow-inner"
            initial={{opacity: 0, y: 20}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.4}}
        >
            <h2
                className={`text-3xl lg:text-4xl font-bold mb-6 ml-6 bg-gradient-to-r ${gradientFrom} ${gradientTo} text-transparent bg-clip-text`}
            >
                {title} {emoji}
            </h2>
            <ScrollableSection>
                {products.map((product) => (
                    <ProductCard key={product.id} product={product}/>
                ))}
            </ScrollableSection>
        </motion.div>
    );

    return (
        <div className="w-full py-8 space-y-16 pb-24">
            {renderSection("Promowane", "⭐", "from-[#4D55CC]", "to-[#7A73D1]", promoted)}
            {renderSection("Popularne", "🔥", "from-red-500", "to-yellow-400", popular)}
        </div>
    );
};

export default Home;
