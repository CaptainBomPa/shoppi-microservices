import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {motion} from "framer-motion";
import {ArrowLeft} from "lucide-react";
import productService, {ProductResponse} from "../api/productService";
import userService from "../api/userService";
import {User} from "../context/UserContext";
import ProductMainInfo from "../components/products/details/ProductMainInfo";
import ProductActionsPanel from "../components/products/details/ProductActionsPanel";
import ProductSellerInfo from "../components/products/details/ProductSellerInfo";

const ProductDetailsPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const [product, setProduct] = useState<ProductResponse | null>(null);
    const [seller, setSeller] = useState<User | null>(null);
    const [imageLoaded, setImageLoaded] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const productData = await productService.getProductById(Number(id));
                setProduct(productData);
                const sellerData = await userService.getUser(productData.userId);
                setSeller(sellerData);
            } catch (err) {
                console.error("Błąd podczas pobierania danych:", err);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        if (product && seller) {
            setLoading(false);
        }
    }, [product, seller, imageLoaded]);

    const goBack = () => navigate(-1);

    if (loading) {
        return (
            <div className="w-full h-[calc(100vh-4rem)] flex items-center justify-center">
                <div className="w-12 h-12 border-4 border-gray-300 border-t-accent dark:border-t-light rounded-full animate-spin"/>
            </div>
        );
    }

    if (!product || !seller) {
        return <div className="p-6 text-red-500">Nie znaleziono produktu.</div>;
    }

    return (
        <motion.div
            initial={{opacity: 0, y: 40}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.4}}
            className="p-6 max-w-6xl mx-auto"
        >
            <button
                onClick={goBack}
                className="flex items-center space-x-2 text-gray-600 dark:text-gray-300 hover:text-primary dark:hover:text-light mb-6"
            >
                <ArrowLeft size={20}/>
                <span>Wstecz</span>
            </button>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 items-stretch">
                <ProductMainInfo product={product} onImageLoad={() => setImageLoaded(true)}/>
                <ProductActionsPanel product={product}/>
            </div>

            <ProductSellerInfo seller={seller}/>
        </motion.div>
    );
};

export default ProductDetailsPage;
