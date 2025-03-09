import React from "react";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import * as yup from "yup";
import countries from "world-countries";
import {useUser} from "../../context/UserContext";
import companyService from "../../api/companyService";

const countryList = countries.map((c) => ({label: c.name.common, value: c.cca2}));
const countryCodes = countries.map((c) => ({
    label: `${c.cca2} (${c.idd.root}${c.idd.suffixes ? c.idd.suffixes[0] : ""})`,
    value: `${c.idd.root}${c.idd.suffixes ? c.idd.suffixes[0] : ""}`,
    key: `${c.idd.root}${c.cca2}`,
}));


const companySchema = yup.object({
    companyName: yup.string().min(2, "Nazwa firmy musi mieć co najmniej 2 znaki.").required("Nazwa firmy jest wymagana."),
    postalCode: yup.string().matches(/^\d{2}-\d{3}$/, "Kod pocztowy musi mieć format XX-XXX").required("Kod pocztowy jest wymagany."),
    city: yup.string().min(2, "Miasto musi mieć co najmniej 2 znaki.").max(50, "Miasto może mieć maksymalnie 50 znaków.").required("Miasto jest wymagane."),
    street: yup.string().min(2, "Ulica musi mieć co najmniej 2 znaki.").max(100, "Ulica może mieć maksymalnie 100 znaków.").required("Ulica jest wymagana."),
    country: yup.string().required("Wybór kraju jest wymagany."),
    countryCode: yup.string().required("Kod kraju jest wymagany."),
    phone: yup.string().matches(/^\d{9}$/, "Numer telefonu musi mieć dokładnie 9 cyfr.").required("Numer telefonu jest wymagany."),
});

const EditCompanyForm: React.FC<{ showAlert: (message: string, type: "success" | "error") => void }> = ({showAlert}) => {
    const {user, refreshUser} = useUser();
    const {register, handleSubmit, formState: {errors, dirtyFields}, trigger} = useForm({
        resolver: yupResolver(companySchema),
        defaultValues: {
            companyName: user?.companyInfo?.companyName || "",
            postalCode: user?.companyInfo?.postalCode || "",
            city: user?.companyInfo?.city || "",
            street: user?.companyInfo?.street || "",
            country: user?.companyInfo?.country || "",
            countryCode: user?.companyInfo?.countryCode || "+48",
            phone: user?.companyInfo?.phone || "",
        },
    });

    const onSubmit = async (data: any) => {
        try {
            if (!user?.companyInfo) {
                await companyService.addCompanyInfo(user!.id, data);
            } else {
                await companyService.updateCompanyInfo(user!.id, data);
            }

            await refreshUser();
            showAlert("Dane firmy zostały zaktualizowane!", "success");
        } catch (error) {
            showAlert("Nie udało się zaktualizować danych firmy.", "error");
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="bg-gray-100 dark:bg-gray-800 p-4 rounded-lg mb-6">
            <h3 className="text-xl font-semibold mb-4">Dane firmy</h3>

            <input
                type="text"
                {...register("companyName")}
                onBlur={() => trigger("companyName")}
                className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                placeholder="Nazwa firmy"
            />
            <div className="h-6">{dirtyFields.companyName && errors.companyName && <p className="text-red-500 text-sm">{errors.companyName.message}</p>}</div>

            <input
                type="text"
                {...register("postalCode")}
                onBlur={() => trigger("postalCode")}
                className="w-full p-3 mt-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light"
                placeholder="Kod pocztowy (XX-XXX)"
            />
            <div className="h-6">{dirtyFields.postalCode && errors.postalCode && <p className="text-red-500 text-sm">{errors.postalCode.message}</p>}</div>

            <input
                type="text"
                {...register("city")}
                onBlur={() => trigger("city")}
                className="w-full p-3 mt-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light"
                placeholder="Miasto"
            />
            <div className="h-6">{dirtyFields.city && errors.city && <p className="text-red-500 text-sm">{errors.city.message}</p>}</div>

            <input
                type="text"
                {...register("street")}
                onBlur={() => trigger("street")}
                className="w-full p-3 mt-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light"
                placeholder="Ulica"
            />
            <div className="h-6">{dirtyFields.street && errors.street && <p className="text-red-500 text-sm">{errors.street.message}</p>}</div>

            <select {...register("country")} onBlur={() => trigger("country")} className="w-full p-3 mt-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700">
                <option value="">Wybierz kraj...</option>
                {countryList.map((c) => (
                    <option key={c.value} value={c.value}>{c.label}</option>
                ))}
            </select>
            <div className="h-6">{dirtyFields.country && errors.country && <p className="text-red-500 text-sm">{errors.country.message}</p>}</div>

            <div className="flex mt-3 space-x-2">
                <select {...register("countryCode")} onBlur={() => trigger("countryCode")} className="p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700">
                    {countryCodes.map((c) => (
                        <option key={c.key} value={c.value}>{c.label}</option>
                    ))}
                </select>
                <input
                    type="text"
                    {...register("phone")}
                    onBlur={() => trigger("phone")}
                    className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700"
                    placeholder="Numer telefonu"
                />
            </div>
            <div className="h-6">{dirtyFields.phone && errors.phone && <p className="text-red-500 text-sm">{errors.phone.message}</p>}</div>

            <button type="submit" className="w-full mt-4 p-3 bg-gradient-to-r from-[#4D55CC] to-[#7A73D1] text-white rounded-lg transition duration-300 hover:opacity-80">
                Zapisz
            </button>
        </form>
    );
};

export default EditCompanyForm;
