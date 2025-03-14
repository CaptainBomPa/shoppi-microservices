import React from "react";
import {Pencil} from "lucide-react";
import {motion} from "framer-motion";

type AddressProps = {
    address: {
        id: number;
        firstName: string;
        lastName: string;
        postalCode: string;
        city: string;
        street: string;
        country: string;
        countryCode: string;
        phone: string;
    };
    onEdit: () => void;
};

const AddressCard: React.FC<AddressProps> = ({address, onEdit}) => {
    return (
        <motion.div
            initial={{opacity: 0, y: 10}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.25}}
            whileHover={{scale: 1.05, opacity: 0.9}}
            onClick={onEdit}
            className="relative p-4 bg-white dark:bg-gray-900 shadow-lg rounded-lg border border-gray-300 dark:border-gray-700 transition cursor-pointer"
        >
            <motion.div
                initial={{opacity: 0, scale: 0.8}}
                whileHover={{opacity: 1, scale: 1}}
                className="absolute inset-0 flex items-center justify-center text-gray-500 dark:text-gray-300"
            >
                <Pencil size={48}/>
            </motion.div>

            <h3 className="text-lg font-semibold text-center">{address.firstName} {address.lastName}</h3>
            <p className="text-gray-600 dark:text-gray-300 text-center">{address.street}</p>
            <p className="text-gray-600 dark:text-gray-300 text-center">{address.postalCode}, {address.city}</p>
            <p className="text-gray-600 dark:text-gray-300 text-center">{address.country}</p>
            <p className="text-gray-600 dark:text-gray-300 text-center">{address.countryCode} {address.phone}</p>
        </motion.div>
    );
};

export default AddressCard;
