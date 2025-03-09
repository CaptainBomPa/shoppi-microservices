import React from "react";
import {Suggestion} from "../../types/Suggestion";

type Props = {
    suggestions: Suggestion[];
};

const SearchSuggestions: React.FC<Props> = ({suggestions}) => {
    return (
        <div className="absolute w-full max-w-2xl bg-white dark:bg-gray-800 border border-light dark:border-accent shadow-lg rounded-lg mt-2 z-50">
            {suggestions.map((item) => (
                <a
                    key={item.id}
                    href={item.link}
                    className="flex items-center p-3 hover:bg-light dark:hover:bg-gray-700 transition duration-300"
                >
                    <img src={item.image} alt={item.name} className="w-10 h-10 rounded-md mr-3"/>
                    <span className="text-primary dark:text-light">{item.name}</span>
                </a>
            ))}
        </div>
    );
};

export default SearchSuggestions;
