import React from "react";
import {AnimatePresence, motion} from "framer-motion";

type Props = {
    open: boolean;
    onClose: () => void;
    onConfirm: () => void;
};

const ConfirmDeleteDialog: React.FC<Props> = ({open, onClose, onConfirm}) => {
    return (
        <AnimatePresence mode="wait">
            {open && (
                <motion.div
                    initial={{opacity: 0}}
                    animate={{opacity: 1}}
                    exit={{opacity: 0}}
                    className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
                >
                    <motion.div
                        initial={{scale: 0.8, opacity: 0}}
                        animate={{scale: 1, opacity: 1}}
                        exit={{scale: 0.8, opacity: 0}}
                        transition={{duration: 0.25}}
                        className="bg-white dark:bg-gray-900 rounded-xl shadow-lg p-6 w-full max-w-sm"
                    >
                        <h2 className="text-lg font-bold mb-4 text-primary dark:text-light">
                            Czy na pewno chcesz usunąć ofertę?
                        </h2>
                        <div className="flex justify-end space-x-3">
                            <button
                                onClick={onClose}
                                className="px-4 py-2 rounded-lg border border-gray-400 dark:border-gray-600 text-gray-700 dark:text-gray-200 hover:bg-gray-200 dark:hover:bg-gray-700 transition"
                            >
                                Nie
                            </button>
                            <button
                                onClick={onConfirm}
                                className="px-4 py-2 rounded-lg bg-red-600 text-white hover:bg-red-700 transition"
                            >
                                Tak, usuń
                            </button>
                        </div>
                    </motion.div>
                </motion.div>
            )}
        </AnimatePresence>
    );
};

export default ConfirmDeleteDialog;
