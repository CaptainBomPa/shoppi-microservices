import React, {useContext} from "react";
import {ThemeContext} from "../../context/ThemeContext";

const ThemeToggle = () => {
    const themeContext = useContext(ThemeContext);
    if (!themeContext) return null;

    const {theme, toggleTheme} = themeContext;

    return (
        <button
            onClick={toggleTheme}
            className="p-2 rounded-lg border border-light dark:border-accent text-secondary dark:text-light transition duration-300 hover:bg-secondary dark:hover:bg-light hover:text-white dark:hover:text-gray-900"
        >
            {theme === "light" ? "🌙" : "☀️"}
        </button>
    );
};

export default ThemeToggle;
