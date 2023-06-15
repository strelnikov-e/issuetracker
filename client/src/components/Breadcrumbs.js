import { useLocation, Link } from "react-router-dom"
import Breadcrumb from 'react-bootstrap/Breadcrumb';

export default function Breadcrumbs() {
    const location = useLocation();

    let currentLink = '';

    const crumbs =
        <Breadcrumb>
            {location.pathname === '/' ? <Breadcrumb.Item active key="home" href="/">home</Breadcrumb.Item> : <Breadcrumb.Item href="/" key="home">home</Breadcrumb.Item>}

            {location.pathname.split('/').filter(crumb => crumb !== '').map((crumb, index, row) => {
                currentLink += `/${crumb}`

                if (index + 1 !== row.length) {
                    return (
                        <Breadcrumb.Item key={crumb}
                            href={currentLink}>{crumb}
                        </Breadcrumb.Item>
                    )
                } else {
                    return (
                        <Breadcrumb.Item active key={crumb}
                            href={currentLink}>{crumb}
                        </Breadcrumb.Item>
                    )
                }
            })}
        </Breadcrumb>


    return (
        <div className="breadcrumbs mb-5">
            {crumbs}
        </div>
    )
}