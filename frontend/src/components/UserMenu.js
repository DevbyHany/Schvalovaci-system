import { useState, useEffect, useRef } from "react";



function UserMenu({ currentUser, onLogout }) {
    const [isOpen, setIsOpen] = useState(false);
    const menuRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    return (
        <div className="user-menu" ref={menuRef}>
            <div className="avatar" onClick={() => setIsOpen(!isOpen)}>
                {currentUser?.name?.charAt(0)}
            </div>
            {isOpen && (
                <div className="user-dropdown">
                    <button onClick={onLogout}>Odhlásit se</button>
                </div>
            )}
        </div>
    );
}

export default UserMenu;