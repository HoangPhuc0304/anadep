import { Link } from 'react-router-dom'
import { LogoIcon } from './Icons'

export const Footer = () => {
    return (
        <footer id="footer">
            <hr className="w-full mx-auto" />
            <section className="container pb-10 text-center">
                <h3>
                    &copy; 2024 Copyright by{' '}
                    <Link
                        to="https://hcmus.edu.vn/"
                        target="_blank"
                        className="text-primary transition-all border-primary hover:border-b-2"
                    >
                        HCMUS
                    </Link>
                </h3>
            </section>
        </footer>
    )
}
