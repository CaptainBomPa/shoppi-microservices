import React, {useEffect, useState} from "react";

type CustomAlertProps = {
    message: string;
    type: "success" | "error";
    onClose: () => void;
};

const CustomAlert: React.FC<CustomAlertProps> = ({message, type, onClose}) => {
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        setVisible(true);
        const timer = setTimeout(() => {
            setVisible(false);
            setTimeout(onClose, 300);
        }, 5000);

        return () => clearTimeout(timer);
    }, [onClose]);

    return (
        <div
            className={`fixed top-5 left-1/2 transform -translate-x-1/2 z-50 w-[500px] max-w-full px-4 py-3 rounded-lg shadow-lg text-white text-center font-medium transition-all duration-300 ${
                visible ? "opacity-100 scale-100" : "opacity-0 scale-90"
            } ${type === "success" ? "bg-green-500" : "bg-red-500"}`}
        >
            {message}
        </div>
    );
};

export default CustomAlert;
