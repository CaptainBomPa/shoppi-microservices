import React from "react";
import {useAuth} from "../context/AuthContext";
import {Menu, Transition} from "@headlessui/react";
import {FiSettings} from "react-icons/fi";
import {Link} from "react-router-dom";

const UserMenu = () => {
    const {user, logout} = useAuth();

    return (
        <div className="relative flex items-center space-x-4">
            <Menu as="div" className="relative">
                <Menu.Button className="flex items-center space-x-2 p-2 border border-light dark:border-accent rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition">
                    <span className="text-primary dark:text-light">
                        {user?.accountType === "SELLER" ? user.companyName || user.email : `${user?.firstName} ${user?.lastName}`}
                    </span>
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
                        {user?.accountType === "USER" ? (
                            <>
                                <Menu.Item>
                                    <Link to="/my-orders" className="block px-4 py-2 text-gray-800 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700">
                                        Moje zamówienia
                                    </Link>
                                </Menu.Item>
                                <Menu.Item>
                                    <Link to="/my-addresses" className="block px-4 py-2 text-gray-800 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700">
                                        Moje adresy
                                    </Link>
                                </Menu.Item>
                                <Menu.Item>
                                    <Link to="/favourites" className="block px-4 py-2 text-gray-800 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700">
                                        Ulubione
                                    </Link>
                                </Menu.Item>
                            </>
                        ) : (
                            <Menu.Item>
                                <Link to="/my-offers" className="block px-4 py-2 text-gray-800 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700">
                                    Moje oferty
                                </Link>
                            </Menu.Item>
                        )}
                    </Menu.Items>
                </Transition>
            </Menu>

            <Menu as="div" className="relative">
                <Menu.Button className="p-2 rounded-lg border border-light dark:border-accent hover:bg-gray-100 dark:hover:bg-gray-700 transition">
                    <FiSettings className="text-xl text-primary dark:text-light"/>
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
        </div>
    );
};

export default UserMenu;
