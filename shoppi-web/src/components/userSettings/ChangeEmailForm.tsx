import React from "react";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import * as yup from "yup";
import {useUser} from "../../context/UserContext";

const emailSchema = yup.object({
    email: yup.string().email("Nieprawidłowy adres email.").required("Email jest wymagany."),
    password: yup.string().required("Podaj hasło, aby zmienić email."),
});

const ChangeEmailForm: React.FC<{ showAlert: (message: string, type: "success" | "error") => void }> = ({showAlert}) => {
    const {user, changeEmail} = useUser();
    const {register, handleSubmit, formState: {errors, dirtyFields}, trigger} = useForm<{ email: string; password: string }>({
        resolver: yupResolver(emailSchema),
        defaultValues: {
            email: user?.email || "",
            password: "",
        },
    });

    const onSubmit = async (data: { email: string; password: string }) => {
        try {
            const success = await changeEmail(data.email, data.password);
            if (success) {
                showAlert("E-mail został zmieniony! Zaloguj się ponownie.", "success");
            } else {
                showAlert("Nie udało się zmienić e-maila. Spróbuj ponownie.", "error");
            }
        } catch (error) {
            showAlert("Nie udało się zmienić e-maila.", "error");
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="bg-gray-100 dark:bg-gray-800 p-4 rounded-lg mb-6">
            <h3 className="text-xl font-semibold mb-4">Zmiana adresu e-mail</h3>

            <input
                type="email"
                {...register("email")}
                onBlur={() => trigger("email")}
                className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                placeholder="Nowy adres e-mail"
            />
            <div className="h-6 transition-opacity duration-300">
                {dirtyFields.email && errors.email && (
                    <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.email.message}</p>
                )}
            </div>

            <input
                type="password"
                {...register("password")}
                onBlur={() => trigger("password")}
                className="w-full p-3 mt-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                placeholder="Podaj hasło"
            />
            <div className="h-6 transition-opacity duration-300">
                {dirtyFields.password && errors.password && (
                    <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.password.message}</p>
                )}
            </div>

            <button type="submit" className="w-full mt-4 p-3 bg-gradient-to-r from-[#4D55CC] to-[#7A73D1] text-white rounded-lg transition duration-300 hover:opacity-80">
                Zapisz
            </button>
        </form>
    );
};

export default ChangeEmailForm;
