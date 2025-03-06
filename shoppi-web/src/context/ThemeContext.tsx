import React, {createContext, ReactNode, useEffect, useState} from "react";

type ThemeContextType = {
    theme: "light" | "dark";
    toggleTheme: () => void;
};

export const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

interface ThemeProviderProps {
    children: ReactNode;
}

export function ThemeProvider({children}: ThemeProviderProps) {
    const getInitialTheme = (): "light" | "dark" => {
        if (localStorage.getItem("theme")) {
            return localStorage.getItem("theme") as "light" | "dark";
        }
        return window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
    };

    const [theme, setTheme] = useState<"light" | "dark">(getInitialTheme);

    useEffect(() => {
        if (theme === "dark") {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }
        localStorage.setItem("theme", theme);
    }, [theme]);

    const toggleTheme = () => {
        setTheme((prevTheme) => (prevTheme === "light" ? "dark" : "light"));
    };

    return (
        <ThemeContext.Provider value={{theme, toggleTheme}}>
            {children}
        </ThemeContext.Provider>
    );
}
