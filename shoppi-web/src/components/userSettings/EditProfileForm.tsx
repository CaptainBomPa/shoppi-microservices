import React, {useState} from "react";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import * as yup from "yup";
import {Switch} from "@headlessui/react";
import {useUser} from "../../context/UserContext";
import {GenderType} from "../../types/Gender";

const nameSchema = yup.object({
    firstName: yup.string().min(2, "Imię musi mieć co najmniej 2 znaki.").required("Imię jest wymagane."),
    lastName: yup.string().min(2, "Nazwisko musi mieć co najmniej 2 znaki.").required("Nazwisko jest wymagane."),
    gender: yup.string().oneOf(["MALE", "FEMALE", "UNKNOWN"]).required(),
});

const EditProfileForm: React.FC<{ showAlert: (message: string, type: "success" | "error") => void }> = ({showAlert}) => {
    const {user, updateUserInfo} = useUser();
    const [selectedGender, setSelectedGender] = useState<GenderType>(user?.gender || "UNKNOWN");

    const {register, handleSubmit, formState: {errors, dirtyFields}, trigger} = useForm<{ firstName: string; lastName: string; gender: GenderType }>({
        resolver: yupResolver(nameSchema),
        defaultValues: {
            firstName: user?.firstName || "",
            lastName: user?.lastName || "",
            gender: user?.gender || "UNKNOWN",
        },
    });

    const onSubmit = async (data: { firstName: string; lastName: string; gender: GenderType }) => {
        try {
            await updateUserInfo({...data, gender: selectedGender});
            showAlert("Dane zostały zaktualizowane!", "success");
        } catch (error) {
            showAlert("Nie udało się zaktualizować danych.", "error");
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="bg-gray-100 dark:bg-gray-800 p-4 rounded-lg mb-6">
            <h3 className="text-xl font-semibold mb-4">Dane podstawowe</h3>

            <input
                type="text"
                {...register("firstName")}
                onBlur={() => trigger("firstName")}
                className="w-full p-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                placeholder="Imię"
            />
            <div className="h-6 transition-opacity duration-300">
                {dirtyFields.firstName && errors.firstName && (
                    <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.firstName.message}</p>
                )}
            </div>

            <input
                type="text"
                {...register("lastName")}
                onBlur={() => trigger("lastName")}
                className="w-full p-3 mt-3 border border-light dark:border-accent rounded-lg bg-white dark:bg-gray-700 text-primary dark:text-light focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
                placeholder="Nazwisko"
            />
            <div className="h-6 transition-opacity duration-300">
                {dirtyFields.lastName && errors.lastName && (
                    <p className="text-red-500 text-sm opacity-100 transform scale-100">{errors.lastName.message}</p>
                )}
            </div>

            <div className="mt-3">
                <Switch.Group>
                    <div className="w-full flex justify-between bg-gray-200 dark:bg-gray-700 p-2 rounded-lg">
                        {(["MALE", "FEMALE", "UNKNOWN"] as GenderType[]).map((gender) => (
                            <Switch
                                key={gender}
                                checked={selectedGender === gender}
                                onChange={() => setSelectedGender(gender)}
                                className={`w-1/3 py-2 text-lg font-medium text-center rounded-lg transition ${
                                    selectedGender === gender ? "bg-[#4D55CC] text-white" : "text-gray-600 dark:text-gray-300"
                                }`}
                            >
                                {gender === "MALE" ? "Mężczyzna" : gender === "FEMALE" ? "Kobieta" : "Nie podano"}
                            </Switch>
                        ))}
                    </div>
                </Switch.Group>
            </div>

            <button type="submit" className="w-full mt-4 p-3 bg-gradient-to-r from-[#4D55CC] to-[#7A73D1] text-white rounded-lg transition duration-300 hover:opacity-80">
                Zapisz
            </button>
        </form>
    );
};

export default EditProfileForm;
