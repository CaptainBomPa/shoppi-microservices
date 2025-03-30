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
    const [expandedItems, setExpandedItems] = useState<string[]>([]);

    useEffect(() => {
        const buildTree = (categories: Category[]): TreeItem[] =>
            categories.map((cat) => ({
                id: cat.id.toString(),
                label: cat.name,
                children: cat.subcategories ? buildTree(cat.subcategories) : undefined,
            }));

        categoryService.getAllCategories().then((data) => {
            const tree = buildTree(data);
            setTreeItems(tree);
        });
    }, []);

    useEffect(() => {
        if (!selectedCategoryId || treeItems.length === 0) return;

        const findPathToCategory = (items: TreeItem[], targetId: string, path: string[] = []): string[] | null => {
            for (const item of items) {
                const newPath = [...path, item.id];
                if (item.id === targetId) return newPath;
                if (item.children) {
                    const result = findPathToCategory(item.children, targetId, newPath);
                    if (result) return result;
                }
            }
            return null;
        };

        const path = findPathToCategory(treeItems, selectedCategoryId.toString());
        if (path) {
            path.pop();
            setExpandedItems(path);
        }
    }, [selectedCategoryId, treeItems]);


    return (
        <div className="mb-8">
            <h2 className="text-lg font-bold mb-3">Kategoria</h2>
            <div className="border dark:border-accent rounded-lg p-3 bg-white dark:bg-gray-900">
                <RichTreeView
                    items={treeItems}
                    selectedItems={selectedCategoryId ? selectedCategoryId.toString() : null}
                    onItemSelectionToggle={(_, itemId) =>
                        setValue("categoryId", Number(itemId), {shouldValidate: true})
                    }
                    className="rich-tree-kanit"
                    multiSelect={false}
                    expandedItems={expandedItems}
                    onExpandedItemsChange={(_, items) => setExpandedItems(items)}
                    slotProps={{
                        root: {className: "font-[Kanit]"},
                    }}
                />
            </div>
        </div>
    );
};

export default ProductCategorySection;
