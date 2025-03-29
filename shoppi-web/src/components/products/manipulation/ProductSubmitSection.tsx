import React from "react";

const ProductSubmitSection = () => {
    return (
        <div className="flex justify-end">
            <button
                type="submit"
                className="bg-gradient-to-r from-accent to-accent/80 text-white font-semibold py-2 px-6 rounded-xl hover:brightness-110 transition"
            >
                Zapisz ofertę
            </button>
        </div>
    );
};

export default ProductSubmitSection;
