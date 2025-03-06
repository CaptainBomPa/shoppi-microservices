import React, {useEffect, useState} from "react";
import {Switch} from "@headlessui/react";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import * as yup from "yup";
import GradientButton from "./GradientButton";
import {useAuth} from "../context/AuthContext";

type RegisterDialogProps = {
    onClose: () => void;
    openLogin: () => void;
    showAlert: (message: string, type: "success" | "error") => void;
};

const schema = yup.object().shape({
    email: yup.string().email("Nieprawidłowy adres email.").required("Email jest wymagany."),
    firstName: yup.string().min(2, "Imię musi mieć co najmniej 2 znaki.").required("Imię jest wymagane."),
    lastName: yup.string().min(2, "Nazwisko musi mieć co najmniej 2 znaki.").required("Nazwisko jest wymagane."),
    password: yup.string().min(6, "Hasło musi mieć co najmniej 6 znaków.").required("Hasło jest wymagane."),
    confirmPassword: yup
        .string()
        .oneOf([yup.ref("password")], "Hasła nie są identyczne.")
        .required("Potwierdzenie hasła jest wymagane."),
});

const RegisterDialog: React.FC<RegisterDialogProps> = ({onClose, openLogin, showAlert}) => {
    const [isVisible, setIsVisible] = useState(false);
    const [accountType, setAccountType] = useState<"INDYWIDUALNY" | "FIRMA">("INDYWIDUALNY");

    const {register: registerUser} = useAuth();

    const {
        register,
        handleSubmit,
        formState: {errors, isValid, dirtyFields},
        trigger,
        reset
    } = useForm({
        resolver: yupResolver(schema),
        mode: "onBlur",
    });

    useEffect(() => {
        setIsVisible(true);
    }, []);

    const handleClose = () => {
        setIsVisible(false);
        setTimeout(onClose, 200);
    };

    const handleRegister = async (data: any) => {
        try {
            await registerUser({
                ...data,
                accountType,
            });

            showAlert("Rejestracja zakończona sukcesem! Możesz się teraz zalogować.", "success");

            handleClose();
            openLogin();
            reset();
        } catch (error) {
            showAlert("Błąd rejestracji: Spróbuj ponownie.", "error");
        }
    };

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 transition-opacity duration-200">
            <div
                className={`bg-white dark:bg-gray-800 p-8 rounded-xl shadow-lg w-[500px] max-w-full transition-all duration-200 transform ${
                    isVisible ? "scale-100 opacity-100" : "scale-95 opacity-0"
                }`}
            >
                <h2 className="text-2xl font-bold text-primary dark:text-light mb-6 text-center">
                    Rejestracja
                </h2>

                <div className="w-full flex justify-center mb-6">
                    <Switch.Group>
                        <div className="w-full flex justify-between bg-gray-200 dark:bg-gray-700 p-2 rounded-lg">
                            <Switch
                                checked={accountType === "INDYWIDUALNY"}
                                onChange={() => setAccountType("INDYWIDUALNY")}
                                className={`w-1/2 py-2 text-lg font-medium text-center rounded-lg transition ${
                                    accountType === "INDYWIDUALNY"
                                        ? "bg-[#4D55CC] text-white"
                                        : "text-gray-600 dark:text-gray-300"
                                }`}
                            >
                                Indywidualny
                            </Switch>
                            <Switch
                                checked={accountType === "FIRMA"}
                                onChange={() => setAccountType("FIRMA")}
                                className={`w-1/2 py-2 text-lg font-medium text-center rounded-lg transition ${
                                    accountType === "FIRMA"
                                        ? "bg-[#4D55CC] text-white"
                                        : "text-gray-600 dark:text-gray-300"
                                }`}
                            >
                                Firma
                            </Switch>
                        </div>
                    </Switch.Group>
                </div>

                <form onSubmit={handleSubmit(handleRegister)}>
                    <input
                        type="email"
                        placeholder="Adres email"
                        {...register("email")}
                        onBlur={() => trigger("email")}
                        className="w-full p-3 border border-light dark:border-accent rounded-lg bg-gray-100 dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                    />
                    <div className="h-6 transition-opacity duration-300">
                        {dirtyFields.email && errors.email && (
                            <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.email.message}</p>
                        )}
                    </div>

                    <div className="flex space-x-2">
                        <input
                            type="text"
                            placeholder="Imię"
                            {...register("firstName")}
                            onBlur={() => trigger("firstName")}
                            className="w-1/2 p-3 border border-light dark:border-accent rounded-lg bg-gray-100 dark:bg-gray-700 text-primary dark:text-light"
                        />
                        <input
                            type="text"
                            placeholder="Nazwisko"
                            {...register("lastName")}
                            onBlur={() => trigger("lastName")}
                            className="w-1/2 p-3 border border-light dark:border-accent rounded-lg bg-gray-100 dark:bg-gray-700 text-primary dark:text-light"
                        />
                    </div>
                    <div className="h-6 transition-opacity duration-300">
                        {(dirtyFields.firstName || dirtyFields.lastName) &&
                            (errors.firstName || errors.lastName) && (
                                <p className="text-red-500 text-sm opacity-100 transform scale-100">
                                    {errors.firstName?.message || errors.lastName?.message}
                                </p>
                            )}
                    </div>

                    <input
                        type="password"
                        placeholder="Hasło"
                        {...register("password")}
                        onBlur={() => trigger("password")}
                        className="w-full p-3 border border-light dark:border-accent rounded-lg bg-gray-100 dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                    />
                    <div className="h-6 transition-opacity duration-300">
                        {dirtyFields.password && errors.password && (
                            <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.password.message}</p>
                        )}
                    </div>

                    <input
                        type="password"
                        placeholder="Potwierdź hasło"
                        {...register("confirmPassword")}
                        onBlur={() => trigger("confirmPassword")}
                        className="w-full p-3 border border-light dark:border-accent rounded-lg bg-gray-100 dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                    />
                    <div className="h-6 transition-opacity duration-300">
                        {dirtyFields.confirmPassword && errors.confirmPassword && (
                            <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.confirmPassword.message}</p>
                        )}
                    </div>

                    <div className="mt-6">
                        <GradientButton text="Zarejestruj" onClick={handleSubmit(handleRegister)} disabled={!isValid}/>
                    </div>
                </form>

                <p className="mt-4 text-center text-lg text-gray-600 dark:text-gray-300">
                    Masz już konto?{" "}
                    <button onClick={openLogin} className="text-blue-500 hover:underline">
                        Wróć do logowania
                    </button>
                </p>
            </div>
        </div>
    );
};

export default RegisterDialog;
