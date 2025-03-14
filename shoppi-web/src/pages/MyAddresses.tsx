import React, {useState} from "react";
import {Plus} from "lucide-react";
import {motion} from "framer-motion";
import AddressCard from "../components/addresses/AddressCard";
import AddressDialog from "../components/dialog/AddressDialog";
import {ShippingAddress, useUser} from "../context/UserContext";

const MyAddresses = () => {
    const {user} = useUser();
    const addresses = user?.shippingAddresses || [];
    const canAddMore = addresses.length < 6;

    const [dialogOpen, setDialogOpen] = useState(false);
    const [editingAddress, setEditingAddress] = useState<typeof addresses[number] | null>(null);

    const handleOpenDialog = (address: ShippingAddress | null) => {
        setEditingAddress(address);
        setDialogOpen(true);
    };

    const handleCloseDialog = () => {
        setDialogOpen(false);
    }

    return (
        <motion.div
            initial={{opacity: 0, y: 50}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.4}}
            className="max-w-4xl mx-auto p-6"
        >
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-3xl font-bold">Moje adresy 🏠</h2>
                <span className="text-lg text-gray-600 dark:text-gray-300">{addresses.length}/6</span>
            </div>

            <div className="grid grid-cols-3 gap-6 p-6 bg-gray-100 dark:bg-gray-800 shadow-md rounded-lg">
                {addresses.map((address) => (
                    <AddressCard key={address.id} address={address} onEdit={() => handleOpenDialog(address)}/>
                ))}

                {canAddMore && (
                    <motion.button
                        initial={{opacity: 0, y: 10}}
                        animate={{opacity: 1, y: 0}}
                        transition={{duration: 0.25}}
                        whileHover={{scale: 1.05, opacity: 0.9}}
                        onClick={() => handleOpenDialog(null)}
                        className="p-4 h-full min-h-[150px] bg-gray-200 dark:bg-gray-700 shadow-lg rounded-lg flex items-center justify-center text-gray-500 dark:text-gray-300 hover:bg-gray-300 dark:hover:bg-gray-600 transition"
                    >
                        <Plus size={48}/>
                    </motion.button>
                )}
            </div>

            <AddressDialog isOpen={dialogOpen} onClose={() => handleCloseDialog()} address={editingAddress}/>
        </motion.div>
    );
};

export default MyAddresses;
