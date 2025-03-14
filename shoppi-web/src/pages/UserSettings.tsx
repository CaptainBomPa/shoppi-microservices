import React, {useRef, useState} from "react";
import CustomAlert from "../components/CustomAlert";
import EditProfileForm from "../components/userSettings/EditProfileForm";
import ChangeEmailForm from "../components/userSettings/ChangeEmailForm";
import ChangePasswordForm from "../components/userSettings/ChangePasswordForm";
import {motion} from "framer-motion";

const UserSettings = () => {
    const [alertMessage, setAlertMessage] = useState<{ message: string; type: "success" | "error" } | null>(null);
    const alertRef = useRef<HTMLDivElement>(null);

    const showAlert = (message: string, type: "success" | "error") => {
        setAlertMessage({message, type});
        setTimeout(() => setAlertMessage(null), 5300);
    };

    return (
        <motion.div
            initial={{opacity: 0, y: 30}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.35}}
            className="max-w-2xl mx-auto p-6 bg-white dark:bg-gray-900 shadow-lg rounded-lg text-primary dark:text-light">
            {alertMessage && <CustomAlert message={alertMessage.message} type={alertMessage.type} onClose={() => setAlertMessage(null)}/>}

            <h2 className="text-3xl font-bold text-center mb-6" ref={alertRef}>Ustawienia konta</h2>

            <EditProfileForm showAlert={showAlert}/>
            <ChangeEmailForm showAlert={showAlert}/>
            <ChangePasswordForm showAlert={showAlert}/>
        </motion.div>
    );
};

export default UserSettings;
