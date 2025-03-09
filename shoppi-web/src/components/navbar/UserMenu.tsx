import React from "react";
import UserDropdown from "../userMenu/UserDropdown";
import SettingsDropdown from "../userMenu/SettingsDropdown";

const UserMenu = () => {
    return (
        <div className="relative flex items-center space-x-4">
            <UserDropdown/>
            <SettingsDropdown/>
        </div>
    );
};

export default UserMenu;
