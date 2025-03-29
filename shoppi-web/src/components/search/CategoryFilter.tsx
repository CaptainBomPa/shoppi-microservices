import React, {useEffect, useState} from "react";
import {RichTreeView} from "@mui/x-tree-view/RichTreeView";
import categoryService, {Category} from "../../api/categoryService";

type Props = {
    selectedCategoryId?: number;
    onCategoryChange: (id?: number) => void;
};

type TreeViewItem = {
    id: string;
    label: string;
    children?: TreeViewItem[];
};

const CategoryTree: React.FC<Props> = ({selectedCategoryId, onCategoryChange}) => {
    const [treeItems, setTreeItems] = useState<TreeViewItem[]>([]);

    useEffect(() => {
        categoryService.getAllCategories().then((data) => {
            const buildTree = (categories: Category[]): TreeViewItem[] =>
                categories.map((cat) => ({
                    id: cat.id.toString(),
                    label: cat.name,
                    children: cat.subcategories ? buildTree(cat.subcategories) : undefined,
                }));

            setTreeItems(buildTree(data));
        });
    }, []);

    const handleSelection = (
        _event: React.SyntheticEvent,
        itemId: string,
        isSelected: boolean
    ) => {
        if (isSelected) {
            onCategoryChange(Number(itemId));
        } else {
            onCategoryChange(undefined);
        }
    };

    return (
        <div className="font-sans">
            <RichTreeView
                items={treeItems}
                selectedItems={selectedCategoryId ? [selectedCategoryId.toString()] : []}
                onItemSelectionToggle={handleSelection}
                multiSelect={true}
                className="rich-tree-kanit"
            />
        </div>
    );
};

export default CategoryTree;
