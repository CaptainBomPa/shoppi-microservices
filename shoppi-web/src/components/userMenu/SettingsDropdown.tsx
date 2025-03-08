import React from "react";
import {Menu, Transition} from "@headlessui/react";
import {Settings} from "lucide-react";
import {Link} from "react-router-dom";
import {useAuth} from "../../context/AuthContext";
import {useUser} from "../../context/UserContext";

const SettingsDropdown = () => {
    const {logout} = useAuth();
    const {user} = useUser();

    return (
        <Menu as="div" className="relative">
            <Menu.Button className="p-2 rounded-lg border border-light dark:border-accent hover:bg-gray-100 dark:hover:bg-gray-700 transition">
                <Settings className="text-xl text-primary dark:text-light"/>
            </Menu.Button>

            <Transition
                enter="transition ease-out duration-100"
                enterFrom="transform opacity-0 scale-95"
                enterTo="transform opacity-100 scale-100"
                leave="transition ease-in duration-75"
                leaveFrom="transform opacity-100 scale-100"
                leaveTo="transform opacity-0 scale-95"
            >
                <Menu.Items className="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 shadow-lg rounded-lg overflow-hidden z-50">
                    <Menu.Item>
                        <Link to={user?.accountType === "USER" ? "/user-settings" : "/company-settings"} className="block px-4 py-2 text-gray-800 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700">
                            Ustawienia konta
                        </Link>
                    </Menu.Item>
                    <Menu.Item>
                        <button onClick={logout} className="block w-full text-left px-4 py-2 text-red-500 hover:bg-gray-100 dark:hover:bg-gray-700">
                            Wyloguj
                        </button>
                    </Menu.Item>
                </Menu.Items>
            </Transition>
        </Menu>
    );
};

export default SettingsDropdown;
