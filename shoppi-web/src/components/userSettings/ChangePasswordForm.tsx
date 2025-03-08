import React from "react";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import * as yup from "yup";
import {useUser} from "../../context/UserContext";
import {useAuth} from "../../context/AuthContext";

const passwordSchema = yup.object({
    oldPassword: yup.string().required("Podaj obecne hasło."),
    newPassword: yup.string().min(6, "Hasło musi mieć co najmniej 6 znaków.").required("Nowe hasło jest wymagane."),
    confirmPassword: yup.string().oneOf([yup.ref("newPassword")], "Hasła nie są identyczne.").required("Potwierdzenie hasła jest wymagane."),
});

const ChangePasswordForm: React.FC<{ showAlert: (message: string, type: "success" | "error") => void }> = ({showAlert}) => {
    const {changePassword} = useUser();
    const {logout} = useAuth();

    const {register, handleSubmit, formState: {errors, dirtyFields}, trigger} = useForm<{ oldPassword: string; newPassword: string; confirmPassword: string }>({
        resolver: yupResolver(passwordSchema),
    });

    const onSubmit = async (data: { oldPassword: string; newPassword: string }) => {
        try {
            const success = await changePassword(data.oldPassword, data.newPassword);
            if (success) {
                showAlert("Hasło zostało zmienione! Zaloguj się ponownie.", "success");
                setTimeout(() => {
                    logout();
                }, 2000);
            } else {
                showAlert("Błąd podczas zmieniania hasła. Spróbuj ponownie.", "error");
            }
        } catch (error) {
            showAlert("Nie udało się zmienić hasła.", "error");
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="bg-gray-100 dark:bg-gray-800 p-4 rounded-lg">
            <h3 className="text-xl font-semibold mb-4">Zmiana hasła</h3>

            <input
                type="password"
                {...register("oldPassword")}
                onBlur={() => trigger("oldPassword")}
                className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                placeholder="Obecne hasło"
            />
            <div className="h-6 transition-opacity duration-300">
                {dirtyFields.oldPassword && errors.oldPassword && (
                    <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.oldPassword.message}</p>
                )}
            </div>

            <input
                type="password"
                {...register("newPassword")}
                onBlur={() => trigger("newPassword")}
                className="w-full p-3 mt-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                placeholder="Nowe hasło"
            />
            <div className="h-6 transition-opacity duration-300">
                {dirtyFields.newPassword && errors.newPassword && (
                    <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.newPassword.message}</p>
                )}
            </div>

            <input
                type="password"
                {...register("confirmPassword")}
                onBlur={() => trigger("confirmPassword")}
                className="w-full p-3 mt-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                placeholder="Potwierdź nowe hasło"
            />
            <div className="h-6 transition-opacity duration-300">
                {dirtyFields.confirmPassword && errors.confirmPassword && (
                    <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.confirmPassword.message}</p>
                )}
            </div>

            <button type="submit" className="w-full mt-4 p-3 bg-gradient-to-r from-[#4D55CC] to-[#7A73D1] text-white rounded-lg transition duration-300 hover:opacity-90">
                Zmień hasło
            </button>
        </form>
    );
};

export default ChangePasswordForm;
