import React, {useEffect, useState} from "react";
import {useAuth} from "../../context/AuthContext";
import GradientButton from "../GradientButton";

type LoginDialogProps = {
    onClose: () => void;
    openRegister: () => void;
};

const LoginDialog: React.FC<LoginDialogProps> = ({onClose, openRegister}) => {
    const {login} = useAuth();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        setIsVisible(true);
    }, []);

    const handleClose = () => {
        setIsVisible(false);
        setTimeout(onClose, 200);
    };

    const handleLogin = async () => {
        try {
            await login(email, password);
            handleClose();
        } catch (error) {
            setError("Niepoprawny email lub hasło");
        }
    };

    return (
        <div className="fixed inset-0 w-screen h-screen flex items-center justify-center bg-black bg-opacity-50 transition-opacity duration-200 mx-auto">
            <div
                className={`bg-white dark:bg-gray-800 p-10 rounded-3xl shadow-lg w-[1000px] max-w-full transition-all duration-200 transform flex ${
                    isVisible ? "scale-100 opacity-100" : "scale-95 opacity-0"
                }`}
            >
                <div className="w-1/2 flex flex-col justify-center items-center text-center px-8">
                    <h2 className="text-2xl font-bold text-primary dark:text-light mb-4">Zarejestruj się!</h2>
                    <p className="text-gray-600 dark:text-gray-300 text-lg mb-6">Dlaczego warto dołączyć do naszego serwisu?</p>

                    <ul className="text-left space-y-3">
                        {["Dostęp do najlepszych ofert", "Szybkie zamówienia jednym kliknięciem", "Historia zakupów w jednym miejscu", "Darmowe wystawianie ofert"].map(
                            (item, index) => (
                                <li key={index} className="flex items-center space-x-2">
                                    <div className="w-3 h-3 bg-[#4D55CC] rounded-full"></div>
                                    <span className="text-gray-700 dark:text-gray-200 text-mb">{item}</span>
                                </li>
                            )
                        )}
                    </ul>

                    <div className="mt-6 w-full">
                        <GradientButton text="Przejdź do rejestracji" onClick={openRegister} disabled={false}/>
                    </div>
                </div>

                <div className="w-[1px] bg-gray-300 dark:bg-gray-600 mx-6"></div>

                <div className="w-1/2 flex flex-col justify-center px-8">
                    <h2 className="text-2xl font-bold text-primary dark:text-light mb-6 text-center">Logowanie</h2>

                    <div className="h-6 flex items-center justify-center">
                        <p
                            className={`text-red-500 text-center transition-opacity duration-200 ${
                                error ? "opacity-100" : "opacity-0"
                            }`}
                        >
                            {error || "‎"} {}
                        </p>
                    </div>

                    <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)}
                           className="w-full p-4 mt-2 text-lg border border-light dark:border-accent rounded-lg bg-gray-100 dark:bg-gray-700 text-primary dark:text-light"/>
                    <input type="password" placeholder="Hasło" value={password} onChange={(e) => setPassword(e.target.value)}
                           className="w-full p-4 mt-4 text-lg border border-light dark:border-accent rounded-lg bg-gray-100 dark:bg-gray-700 text-primary dark:text-light"/>

                    <div className="mt-6">
                        <GradientButton text="Zaloguj" onClick={handleLogin} disabled={false}/>
                    </div>

                    <button onClick={handleClose} className="w-full mt-4 text-lg text-gray-500 hover:underline">
                        Anuluj
                    </button>
                </div>
            </div>
        </div>
    );
};

export default LoginDialog;
