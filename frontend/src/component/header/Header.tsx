import React from 'react'
import Logo from '../logo/Logo'
import HeaderMenu from '../menu/HeaderMenu'
import { Button } from '../ui/button'
import './Header.css'
import { Link, useNavigate } from 'react-router-dom'
import { GitHubLogoIcon } from '@radix-ui/react-icons'
import { ModeToggle } from '../home/components/mode-toggle'
import { User } from '../../model/library'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuShortcut,
    DropdownMenuTrigger,
} from '../ui/dropdown-menu'
import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'
import { remove } from '../../redux/slice/userSlice'

const Header: React.FC = () => {
    const user: User = useSelector((state: RootState) => state.user.currentUser)
    const dispatch = useDispatch()
    const navigate = useNavigate()

    const handlingLogout = () => {
        dispatch(remove(user))
        navigate('/')
    }

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
                            className="w-56"
                            align="end"
                            forceMount
                        >
                            <DropdownMenuLabel className="font-normal">
                                <div className="flex flex-col space-y-1">
                                    <p className="text-sm font-medium leading-none">
                                        {user.name || user.login}
                                    </p>
                                    <p className="text-xs leading-none text-muted-foreground">
                                        {user.githubUrl}
                                    </p>
                                </div>
                            </DropdownMenuLabel>
                            <DropdownMenuSeparator />
                            <DropdownMenuGroup>
                                <DropdownMenuItem>
                                    Profile
                                    <DropdownMenuShortcut>
                                        ⇧⌘P
                                    </DropdownMenuShortcut>
                                </DropdownMenuItem>
                                <DropdownMenuItem>
                                    Billing
                                    <DropdownMenuShortcut>
                                        ⌘B
                                    </DropdownMenuShortcut>
                                </DropdownMenuItem>
                                <DropdownMenuItem>
                                    Settings
                                    <DropdownMenuShortcut>
                                        ⌘S
                                    </DropdownMenuShortcut>
                                </DropdownMenuItem>
                                <DropdownMenuItem>New Team</DropdownMenuItem>
                            </DropdownMenuGroup>
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
                            to={`https://github.com/login/oauth/authorize?scope=${process.env.REACT_APP_AUTH_SCOPE}&client_id=${process.env.REACT_APP_AUTH_CLIENT_ID}`}
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
