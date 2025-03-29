import React, {useEffect} from "react";
import {FormProvider, useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import {ProductFormSchema, productFormSchema} from "./productFormSchema";
import {useUser} from "../../../context/UserContext";
import ProductTitleSection from "./ProductTitleSection";
import ProductDescriptionSection from "./ProductDescriptionSection";
import ProductCategorySection from "./ProductCategorySection";
import ProductDurationSection from "./ProductDurationSection";
import ProductPricingSection from "./ProductPricingSection";
import ProductSubmitSection from "./ProductSubmitSection";
import ProductStatusSection from "./ProductStatusSection";

type Props = {
    defaultValues?: Partial<ProductFormSchema>;
    onSubmit: (data: ProductFormSchema) => void;
};

const ProductForm: React.FC<Props> = ({defaultValues, onSubmit}) => {
    const {user} = useUser();

    const methods = useForm<ProductFormSchema>({
        resolver: yupResolver(productFormSchema),
        defaultValues: {
            title: "",
            description: "",
            expiresAt: 7,
            price: 1,
            currency: "PLN",
            status: "DRAFT",
            quantity: 1,
            categoryId: undefined,
            userId: user?.id,
            ...defaultValues,
        },
    });

    const {setValue, handleSubmit} = methods;

    useEffect(() => {
        if (user?.id) setValue("userId", user.id);
    }, [user, setValue]);

    return (
        <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-8">
                <ProductTitleSection/>
                <ProductStatusSection/>
                <ProductDescriptionSection/>
                <ProductCategorySection/>
                <ProductDurationSection/>
                <ProductPricingSection/>
                <ProductSubmitSection/>
            </form>
        </FormProvider>
    );
};


export default ProductForm;
