import React from "react";

type GradientButtonProps = {
    text: string;
    onClick: () => void;
    disabled: boolean;
};

const GradientButton: React.FC<GradientButtonProps> = ({text, onClick, disabled}) => {
    return (
        <button
            onClick={onClick}
            disabled={disabled}
            className="w-full py-4 text-lg font-semibold bg-gradient-to-r from-[#4D55CC] to-[#7A73D1] text-white rounded-lg transition hover:opacity-90"
        >
            {text}
        </button>
    );
};

export default GradientButton;
