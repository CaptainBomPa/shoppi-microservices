import React, {useEffect, useState} from "react";
import {useFormContext} from "react-hook-form";
import categoryService, {Category} from "../../../api/categoryService";
import {RichTreeView} from "@mui/x-tree-view/RichTreeView";
import {ProductFormSchema} from "./productFormSchema";

type TreeItem = {
    id: string;
    label: string;
    children?: TreeItem[];
};

const ProductCategorySection: React.FC = () => {
    const {setValue, watch} = useFormContext<ProductFormSchema>();
    const selectedCategoryId = watch("categoryId");
    const [treeItems, setTreeItems] = useState<TreeItem[]>([]);

    useEffect(() => {
        categoryService.getAllCategories().then((data) => {
            const mapCategories = (categories: Category[]): TreeItem[] =>
                categories.map((cat) => ({
                    id: cat.id.toString(),
                    label: cat.name,
                    children: cat.subcategories ? mapCategories(cat.subcategories) : undefined,
                }));

            setTreeItems(mapCategories(data));
        });
    }, []);

    return (
        <div className="mb-8">
            <h2 className="text-lg font-bold mb-3">Kategoria</h2>
            <div className="border dark:border-accent rounded-lg p-3 bg-white dark:bg-gray-900">
                <RichTreeView
                    items={treeItems}
                    multiSelect={false}
                    selectedItems={selectedCategoryId ? selectedCategoryId.toString() : null}
                    onItemSelectionToggle={(_, itemId) =>
                        setValue("categoryId", Number(itemId), {shouldValidate: true})
                    }
                    className="rich-tree-kanit"
                    slotProps={{
                        root: {className: "font-[Kanit]"},
                    }}
                />
            </div>
        </div>
    );
};

export default ProductCategorySection;
