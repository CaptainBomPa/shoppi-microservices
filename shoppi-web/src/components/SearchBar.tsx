import React, {useEffect, useState} from "react";
import {RotateCcw, Search, X} from "lucide-react";
import SearchSuggestions from "./SearchSuggestions";
import {Suggestion} from "../types/Suggestion";

const SearchBar = () => {
    const [searchQuery, setSearchQuery] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [suggestions, setSuggestions] = useState<Suggestion[]>([]);
    const [typingTimeout, setTypingTimeout] = useState<NodeJS.Timeout | null>(null);
    const [showResetIcon, setShowResetIcon] = useState(false);

    useEffect(() => {
        if (searchQuery === "") {
            setSuggestions([]);
            setIsLoading(false);
            return;
        }

        if (typingTimeout) {
            clearTimeout(typingTimeout);
        }

        setTypingTimeout(
            setTimeout(() => {
                setIsLoading(true);
                fetchFakeResults();
            }, 100)
        );
    }, [searchQuery]);

    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent) => {
            if (event.key === "Escape") {
                clearSearch();
            }
        };

        window.addEventListener("keydown", handleKeyDown);
        return () => {
            window.removeEventListener("keydown", handleKeyDown);
        };
    }, []);

    const fetchFakeResults = () => {
        setTimeout(() => {
            setSuggestions([
                {id: 1, name: "Smartfon XYZ", image: "logo192.png", link: "#"},
                {id: 2, name: "Laptop ABC", image: "logo192.png", link: "#"},
                {id: 3, name: "Słuchawki 123", image: "logo192.png", link: "#"},
            ]);
            setIsLoading(false);
        }, 400);
    };

    const clearSearch = () => {
        setSearchQuery("");
        setSuggestions([]);
        setIsLoading(false);
        setShowResetIcon(true);

        setTimeout(() => {
            setShowResetIcon(false);
        }, 1000);
    };

    return (
        <div className="relative w-full max-w-2xl mx-4">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500 dark:text-gray-300 text-lg"/>
            <input
                type="text"
                placeholder="Szukaj produktu..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-10 py-2 rounded-full border border-light dark:border-accent bg-white dark:bg-gray-800 text-primary dark:text-light focus:outline-none focus:ring-2 focus:ring-accent dark:focus:ring-light transition duration-300"
            />

            {searchQuery && !isLoading && (
                <button
                    onClick={clearSearch}
                    className={`absolute right-3 top-1/2 transform -translate-y-1/2 transition-opacity duration-300 ${
                        showResetIcon ? "opacity-100" : "opacity-100"
                    }`}
                >
                    {showResetIcon ? (
                        <RotateCcw className="text-lg text-gray-500 dark:text-gray-300 hover:text-blue-500 transition duration-200"/>
                    ) : (
                        <X className="text-lg text-gray-500 dark:text-gray-300 hover:text-red-500 transition duration-200"/>
                    )}
                </button>
            )}

            {isLoading && (
                <div className="absolute right-3 top-1/2 transform -translate-y-1/2">
                    <div className="w-5 h-5 border-2 border-gray-300 border-t-accent dark:border-t-light rounded-full animate-spin"></div>
                </div>
            )}

            {!isLoading && searchQuery && suggestions.length > 0 && <SearchSuggestions suggestions={suggestions}/>}
        </div>
    );
};

export default SearchBar;
