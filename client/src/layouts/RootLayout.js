import { NavLink, Outlet } from "react-router-dom"
import AppNavbarBase from "../components/AppNavbarBase"
import Breadcrumbs from "../components/Breadcrumbs"

export default function RootLayout() {

    return (
        <div className="root-layout">
            <header className="mb-4">
                <AppNavbarBase/>
            </header>
            <main className="container container-lg">
                <Breadcrumbs/>
                <Outlet />
            </main>
        </div>
    )
}