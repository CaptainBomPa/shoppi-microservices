import React, {useState} from "react";
import LoginDialog from "./LoginDialog";
import RegisterDialog from "./RegisterDialog";
import CustomAlert from "./CustomAlert";

const LoginButton = () => {
    const [isLoginOpen, setIsLoginOpen] = useState(false);
    const [isRegisterOpen, setIsRegisterOpen] = useState(false);
    const [alertData, setAlertData] = useState<{ message: string; type: "success" | "error" } | null>(null);

    const showAlert = (message: string, type: "success" | "error") => {
        setAlertData({message, type});
    };

    return (
        <>
            {alertData && <CustomAlert message={alertData.message} type={alertData.type} onClose={() => setAlertData(null)}/>}

            <button
                onClick={() => setIsLoginOpen(true)}
                className="px-4 py-2 border border-light dark:border-accent text-primary dark:text-light rounded-lg transition duration-300 hover:bg-light dark:hover:bg-accent hover:text-gray-900 dark:hover:text-gray-900"
            >
                Zaloguj się
            </button>

            {isLoginOpen && (
                <LoginDialog
                    onClose={() => setIsLoginOpen(false)}
                    openRegister={() => {
                        setIsLoginOpen(false);
                        setIsRegisterOpen(true);
                    }}
                />
            )}
            {isRegisterOpen && (
                <RegisterDialog
                    onClose={() => setIsRegisterOpen(false)}
                    openLogin={() => {
                        setIsRegisterOpen(false);
                        setIsLoginOpen(true);
                    }}
                    showAlert={showAlert}
                />
            )}
        </>
    );
};

export default LoginButton;
