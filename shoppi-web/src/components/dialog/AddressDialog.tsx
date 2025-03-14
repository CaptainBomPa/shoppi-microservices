import React, {useEffect} from "react";
import {Dialog, Transition} from "@headlessui/react";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import * as yup from "yup";
import {ShippingAddress, ShippingAddressFormData, useUser} from "../../context/UserContext";
import countries from "world-countries";
import GradientButton from "../GradientButton";

const countryList = countries.map((c) => ({label: c.name.common, value: c.cca2}));
const countryCodes = countries.map((c) => ({
    label: `${c.cca2} (${c.idd.root}${c.idd.suffixes ? c.idd.suffixes[0] : ""})`,
    value: `${c.idd.root}${c.idd.suffixes ? c.idd.suffixes[0] : ""}`,
    key: `${c.idd.root}${c.cca2}`,
}));

const addressSchema = yup.object({
    firstName: yup.string().min(2, "Imię musi mieć co najmniej 2 znaki.").required("Imię jest wymagane."),
    lastName: yup.string().min(2, "Nazwisko musi mieć co najmniej 2 znaki.").required("Nazwisko jest wymagane."),
    postalCode: yup.string().matches(/^\d{2}-\d{3}$/, "Kod pocztowy musi mieć format XX-XXX").required("Kod pocztowy jest wymagany."),
    city: yup.string().min(2, "Miasto musi mieć co najmniej 2 znaki.").required("Miasto jest wymagane."),
    street: yup.string().min(2, "Ulica musi mieć co najmniej 2 znaki.").required("Ulica jest wymagana."),
    country: yup.string().required("Wybór kraju jest wymagany."),
    countryCode: yup.string().required("Kod kraju jest wymagany."),
    phone: yup.string().matches(/^\d{9,15}$/, "Numer telefonu musi mieć 9-15 cyfr.").required("Numer telefonu jest wymagany."),
});

type AddressDialogProps = {
    isOpen: boolean;
    onClose: () => void;
    address?: ShippingAddress | null;
};

const AddressDialog: React.FC<AddressDialogProps> = ({isOpen, onClose, address}) => {
    const {addShippingAddress, updateShippingAddress, deleteShippingAddress} = useUser();
    const isEditing = !!address;

    const {register, handleSubmit, setValue, reset, formState: {errors, isValid}} = useForm<ShippingAddressFormData>({
        resolver: yupResolver(addressSchema),
        defaultValues: {
            firstName: "",
            lastName: "",
            postalCode: "",
            city: "",
            street: "",
            country: "",
            countryCode: "+48",
            phone: "",
        },
    });

    useEffect(() => {
        if (address) {
            Object.entries(address).forEach(([key, value]) => {
                setValue(key as keyof ShippingAddressFormData, String(value));
            });
        } else {
            reset();
        }
    }, [address, reset, setValue]);

    const onSubmit = async (data: ShippingAddressFormData) => {
        try {
            if (address?.id) {
                await updateShippingAddress(address.id, data);
            } else {
                await addShippingAddress(data);
            }
            handleCloseDialog();
        } catch (error) {
            console.error("Błąd zapisu adresu:", error);
        }
    };

    const handleCloseDialog = () => {
        reset();
        onClose();
    }

    const onDelete = async () => {
        try {
            if (address?.id) {
                await deleteShippingAddress(address.id);
            } else {
                throw Error();
            }
            handleCloseDialog();
        } catch (error) {
            console.error("Błąd podczas usuwania adresu:", error);
        }
    }


    return (
        <Transition appear show={isOpen} as={React.Fragment}>
            <Dialog as="div" className="fixed inset-0 z-50 overflow-y-auto" onClose={handleCloseDialog}>
                <div className="min-h-screen px-4 text-center bg-black bg-opacity-50 flex items-center justify-center">
                    <Transition.Child
                        as={React.Fragment}
                        enter="transition ease-out duration-300"
                        enterFrom="opacity-0 scale-90"
                        enterTo="opacity-100 scale-100"
                        leave="transition ease-in duration-200"
                        leaveFrom="opacity-100 scale-100"
                        leaveTo="opacity-0 scale-90"
                    >
                        <Dialog.Panel className="w-full max-w-lg p-6 bg-white dark:bg-gray-900 shadow-lg rounded-lg text-left">
                            <Dialog.Title className="text-2xl font-bold text-gray-900 dark:text-light mb-4">
                                {isEditing ? "Edytuj adres" : "Dodaj nowy adres"}
                            </Dialog.Title>

                            <form className="space-y-3">
                                <div className="flex gap-2">
                                    <div className="w-1/2">
                                        <input type="text" {...register("firstName")} placeholder="Imię"
                                               className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"/>
                                        <div className="h-5 text-red-500 text-sm">{errors.firstName?.message}</div>
                                    </div>
                                    <div className="w-1/2">
                                        <input type="text" {...register("lastName")} placeholder="Nazwisko"
                                               className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"/>
                                        <div className="h-5 text-red-500 text-sm">{errors.lastName?.message}</div>
                                    </div>
                                </div>

                                <input type="text" {...register("postalCode")} placeholder="Kod pocztowy (XX-XXX)"
                                       className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"/>
                                <div className="h-5 text-red-500 text-sm">{errors.postalCode?.message}</div>

                                <input type="text" {...register("city")} placeholder="Miasto"
                                       className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"/>
                                <div className="h-5 text-red-500 text-sm">{errors.city?.message}</div>

                                <input type="text" {...register("street")} placeholder="Ulica"
                                       className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"/>
                                <div className="h-5 text-red-500 text-sm">{errors.street?.message}</div>

                                <select {...register("country")}
                                        className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300">
                                    <option value="">Wybierz kraj...</option>
                                    {countryList.map((c) => (
                                        <option key={c.value} value={c.value}>{c.label}</option>
                                    ))}
                                </select>
                                <div className="h-5 text-red-500 text-sm">{errors.country?.message}</div>

                                <div className="flex gap-2">
                                    <select {...register("countryCode")}
                                            className="w-1/4 p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300">
                                        {countryCodes.map((c) => (
                                            <option key={c.key} value={c.value}>{c.label}</option>
                                        ))}
                                    </select>
                                    <input type="text" {...register("phone")} placeholder="Numer telefonu"
                                           className="w-3/4 p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"/>
                                </div>
                                <div className="h-5 text-red-500 text-sm">{errors.phone?.message}</div>

                                <div className="mt-6">
                                    <GradientButton text={isEditing ? "Zapisz zmiany" : "Dodaj adres"} onClick={handleSubmit(onSubmit)} disabled={!isValid}/>
                                </div>
                                <button
                                    onClick={handleCloseDialog}
                                    type="button"
                                    className="w-full py-4 text-lg font-semibold bg-gradient-to-r from-gray-700 to-gray-600 text-white rounded-lg transition hover:opacity-90"
                                >
                                    {"Anuluj"}
                                </button>
                                {isEditing && <button
                                    onClick={onDelete}
                                    className="w-full py-4 text-lg font-semibold bg-gradient-to-r from-red-500 to-red-400 text-white rounded-lg transition hover:opacity-90"
                                >
                                    {"Usuń adres"}
                                </button>}
                            </form>
                        </Dialog.Panel>
                    </Transition.Child>
                </div>
            </Dialog>
        </Transition>
    );
};

export default AddressDialog;
