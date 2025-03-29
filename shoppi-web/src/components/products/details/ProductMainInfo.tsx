import React, {useContext} from "react";
import {ProductResponse} from "../../../api/productService";
import Markdown from "@uiw/react-markdown-preview";
import {ThemeContext} from "../../../context/ThemeContext";
import '@uiw/react-markdown-preview/markdown.css';

type Props = {
    product: ProductResponse;
    onImageLoad: () => void;
};

const ProductMainInfo = ({product, onImageLoad}: Props) => {
    const themeContext = useContext(ThemeContext);
    const currentMode = themeContext?.theme === "dark" ? "dark" : "light";

    return (
        <div className="col-span-2 bg-gray-50 dark:bg-gray-800 rounded-2xl shadow p-6 h-full">
            <h1 className="text-3xl font-bold mb-2">{product.title}</h1>
            <p className="text-gray-600 dark:text-gray-300 mb-4">
                {product.category.name} • {new Date(product.createdAt).toLocaleDateString()}
            </p>

            <img
                src={`https://picsum.photos/600/300?random=${product.id}`}
                alt={product.title}
                className="w-full rounded-lg mb-6"
                onLoad={onImageLoad}
            />

            <div className="mb-6">
                <Markdown
                    source={product.description}
                    wrapperElement={{
                        'data-color-mode': currentMode,
                    }}
                    className="bg-transparent font-[Kanit]"
                    style={{backgroundColor: "transparent"}}
                />
            </div>

            <div className="text-sm text-gray-500 dark:text-gray-400 space-y-1">
                <p><strong>Status:</strong> {product.status}</p>
                <p><strong>Wygasa:</strong> {new Date(product.expiresAt).toLocaleDateString()}</p>
                {product.promotedUntil && (
                    <p><strong>Promowane do:</strong> {new Date(product.promotedUntil).toLocaleDateString()}</p>
                )}
            </div>
        </div>
    );
};

export default ProductMainInfo;
