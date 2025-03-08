import React from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import {ThemeProvider} from "./context/ThemeContext";
import {AuthProvider} from "./context/AuthContext";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import MyOrders from "./pages/MyOrders";
import MyAddresses from "./pages/MyAddresses";
import Favourites from "./pages/Favourites";
import UserSettings from "./pages/UserSettings";
import MyOffers from "./pages/MyOffers";
import CompanySettings from "./pages/CompanySettings";
import ProtectedRoute from "./components/ProtectedRoute";
import {UserProvider} from "./context/UserContext";

function App() {
    return (
        <AuthProvider>
            <UserProvider>
                <ThemeProvider>
                    <Router>
                        <div className="min-h-screen font-sans bg-white dark:bg-gray-900 text-primary dark:text-light pt-16">
                            <Navbar/>
                            <Routes>
                            <Route path="/" element={<Home />} />

                                <Route element={<ProtectedRoute allowedAccountType="USER" redirectPath="/"/>}>
                                    <Route path="/my-orders" element={<MyOrders/>}/>
                                    <Route path="/my-addresses" element={<MyAddresses/>}/>
                                    <Route path="/favourites" element={<Favourites/>}/>
                                    <Route path="/user-settings" element={<UserSettings/>}/>
                                </Route>

                                <Route element={<ProtectedRoute allowedAccountType="SELLER" redirectPath="/"/>}>
                                    <Route path="/my-offers" element={<MyOffers/>}/>
                                    <Route path="/company-settings" element={<CompanySettings/>}/>
                                </Route>
                            </Routes>
                        </div>
                    </Router>
                </ThemeProvider>
            </UserProvider>
        </AuthProvider>
    );
}


export default App;
