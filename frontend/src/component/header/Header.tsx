import React, { useEffect, useState } from 'react'
import Logo from '../logo/Logo'
import HeaderMenu from '../menu/HeaderMenu'
import { Button } from '../ui/button'
import './Header.css'
import { Link, useNavigate } from 'react-router-dom'
import { GitHubLogoIcon } from '@radix-ui/react-icons'
import { User } from '../../model/library'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuShortcut,
    DropdownMenuTrigger,
} from '../ui/dropdown-menu'
import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'
import { remove } from '../../redux/slice/userSlice'
import { Switch } from '../ui/switch'
import { isInstallGithubApp } from '../../api/apiCall'
import { ExternalLink } from 'lucide-react'

const Header: React.FC = () => {
    const user: User = useSelector((state: RootState) => state.user.currentUser)
    const dispatch = useDispatch()
    const navigate = useNavigate()
    const [isInstallApp, setIsInstallApp] = useState<boolean>(true)

    const handlingLogout = () => {
        dispatch(remove(user))
        navigate('/')
    }

    const fetchInstall = async () => {
        if (user.githubToken && user.githubUserId) {
            const isInstalled = await isInstallGithubApp(
                user.githubToken,
                user.githubUserId
            )
            if (isInstalled === false) {
                setIsInstallApp(false)
            }
        }
    }

    useEffect(() => {
        fetchInstall()
    }, [])

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
                {user.id ? (
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button
                                variant="ghost"
                                className="relative h-8 w-8 rounded-full"
                            >
                                <Avatar className="h-9 w-9">
                                    <AvatarImage
                                        src={user.avatarUrl}
                                        alt={user.name}
                                    />
                                    <AvatarFallback>SC</AvatarFallback>
                                </Avatar>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent
                            className="w-64"
                            align="end"
                            forceMount
                        >
                            <DropdownMenuLabel className="font-normal">
                                <div className="flex flex-col space-y-1">
                                    <p className="text-sm font-medium leading-none">
                                        {user.name ? `${user.login} (${user.name})` : user.login}
                                    </p>
                                    <p className="text-xs leading-none text-muted-foreground">
                                        {user.githubUrl}
                                    </p>
                                </div>
                            </DropdownMenuLabel>
                            <DropdownMenuSeparator />
                            <DropdownMenuLabel className="flex item-center justify-between">
                                <div className='flex item-center'>
                                    <div>Installed GitHub App</div>
                                    <Link to={`${process.env.REACT_APP_AUTH_GITHUB_APP}/installations/new` || ""} className='mx-1' target='_blank'>
                                        <ExternalLink/>
                                    </Link>
                                </div>
                                <Switch checked={isInstallApp} disabled />
                            </DropdownMenuLabel>
                            <DropdownMenuLabel className="flex item-center justify-between">
                                <div>Using Anadep Database</div>
                                <Switch
                                    checked={
                                        process.env
                                            .REACT_APP_ENABLE_ANADEP_DB ===
                                        'true'
                                    }
                                    disabled
                                />
                            </DropdownMenuLabel>
                            <DropdownMenuLabel className="flex item-center justify-between">
                                <div>Dark Mode</div>
                                <Switch checked={false} disabled />
                            </DropdownMenuLabel>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem onClick={handlingLogout}>
                                Log out
                                <DropdownMenuShortcut>⇧⌘Q</DropdownMenuShortcut>
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                ) : (
                    <Button asChild>
                        <Link
                            to={`https://github.com/login/oauth/authorize?client_id=${process.env.REACT_APP_AUTH_CLIENT_ID}`}
                        >
                            <GitHubLogoIcon className="mr-2 w-5 h-5" />
                            Login with GitHub
                        </Link>
                    </Button>
                )}
            </div>
        </div>
    )
}

export default Header
