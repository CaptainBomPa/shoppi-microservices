import React from "react";
import {Link} from "react-router-dom";
import SearchBar from "./SearchBar";
import ThemeToggle from "./ThemeToggle";
import {useAuth} from "../../context/AuthContext";
import UserMenu from "./UserMenu";
import LoginButton from "./LoginButton";

const Navbar = () => {
    const {isAuthenticated} = useAuth();

    return (
        <nav className="fixed top-0 left-0 w-full z-50 p-4 bg-white dark:bg-gray-900 shadow-md flex justify-between items-center h-16">
            <Link to="/" className="font-bold text-primary dark:text-light text-2xl md:text-3xl">
                Shoppi
            </Link>

            <SearchBar/>

            <div className="flex items-center space-x-4">
                <ThemeToggle/>
                {isAuthenticated ? <UserMenu/> : <LoginButton/>}
            </div>
        </nav>
    );

};

export default Navbar;
