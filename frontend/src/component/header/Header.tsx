import React from 'react'
import Logo from '../logo/Logo'
import HeaderMenu from '../menu/HeaderMenu'
import { Button } from '../ui/button'
import './Header.css'
import { Link } from 'react-router-dom'
import { useToast } from '../ui/use-toast'
import { GitHubLogoIcon } from '@radix-ui/react-icons'
import { ModeToggle } from '../home/components/mode-toggle'

const Header: React.FC = () => {
    const { toast } = useToast()
    return (
        <div id="header" className="z-10">
            <div className="header-left">
                <Link to="/" className="font-bold text-xl flex items-center">
                    <Logo />
                    <span style={{ color: '#D247BF' }}>Ana</span>
                    <span style={{ color: '#03a3d7' }}>Dep</span>
                </Link>
                <HeaderMenu />
            </div>
            <div className="header-right">
                <ModeToggle />
                <Button
                    onClick={() => {
                        toast({
                            description: 'Your message has been sent.',
                        })
                    }}
                >
                    <GitHubLogoIcon className="mr-2 w-5 h-5" />
                    Login with GitHub
                </Button>
            </div>
        </div>
    )
}

export default Header
