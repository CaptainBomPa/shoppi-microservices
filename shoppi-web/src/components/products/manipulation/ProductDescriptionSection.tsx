import React, {useContext} from "react";
import MDEditor from "@uiw/react-md-editor";
import {Controller, useFormContext} from "react-hook-form";
import {ProductFormSchema} from "./productFormSchema";
import {ThemeContext} from "../../../context/ThemeContext";

const ProductDescriptionSection: React.FC = () => {
    const {
        control,
        formState: {errors},
    } = useFormContext<ProductFormSchema>();

    const themeContext = useContext(ThemeContext);
    const currentTheme = themeContext?.theme || "light";

    return (
        <div>
            <h2 className="text-xl font-bold mb-2">Opis</h2>
            <Controller
                control={control}
                name="description"
                render={({field}) => (
                    <div data-color-mode={currentTheme}>
                        <MDEditor
                            {...field}
                            height={300}
                            preview="edit"
                            className="custom-md-editor"
                        />
                    </div>
                )}
            />
            {errors.description && (
                <p className="text-red-500 text-sm mt-1">{errors.description.message}</p>
            )}
        </div>
    );
};

export default ProductDescriptionSection;
