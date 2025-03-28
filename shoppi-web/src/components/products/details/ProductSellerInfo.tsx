import React from "react";
import {User} from "../../../context/UserContext";

type Props = {
    seller: User;
};

const ProductSellerInfo = ({seller}: Props) => {
    const company = seller.companyInfo;

    return (
        <div className="w-full bg-gray-50 dark:bg-gray-800 rounded-2xl shadow p-6 mt-6">
            <h2 className="text-xl font-bold mb-4">Informacje o sprzedawcy</h2>
            <p><strong>Imię i nazwisko:</strong> {seller.firstName} {seller.lastName}</p>
            {seller.accountType === "SELLER" && (
                <>
                    <p><strong>Nazwa firmy:</strong> {company?.companyName || "-"}</p>
                    <p><strong>Miasto:</strong> {company?.city || "-"}</p>
                    <p><strong>Ulica:</strong> {company?.street || "-"}</p>
                    <p><strong>Kod pocztowy:</strong> {company?.postalCode || "-"}</p>
                    <p><strong>Kraj:</strong> {company?.country || "-"}</p>
                    <p><strong>Telefon:</strong> {company?.phone || "-"}</p>
                </>
            )}
        </div>
    );
};

export default ProductSellerInfo;
