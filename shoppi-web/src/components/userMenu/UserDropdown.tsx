import React from "react";
import {Menu, Transition} from "@headlessui/react";
import {Link} from "react-router-dom";
import {useUser} from "../../context/UserContext";

const UserDropdown = () => {
    const {user} = useUser();

    return (
        <Menu as="div" className="relative">
            <Menu.Button className="flex items-center space-x-2 p-2 border border-light dark:border-accent rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition">
                <span className="text-primary dark:text-light">
                    {user?.accountType === "SELLER" ? user.companyInfo?.companyName || user.email : `${user?.firstName} ${user?.lastName}`}
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
    );
};

export default UserDropdown;
