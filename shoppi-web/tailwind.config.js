/** @type {import('tailwindcss').Config} */
module.exports = {
    darkMode: "class",
    content: [
        "./src/**/*.{js,jsx,ts,tsx}",
    ],
    theme: {
        extend: {
            colors: {
                primary: "#211C84",
                secondary: "#4D55CC",
                accent: "#7A73D1",
                light: "#B5A8D5",
            },
            fontFamily: {
                sans: ["Kanit", "sans-serif"],
            },
        },
    },
    plugins: [],
};
