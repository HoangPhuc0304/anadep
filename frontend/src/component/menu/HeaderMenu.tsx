import React from 'react'

import { cn } from '../../lib/utils'
import {
    NavigationMenu,
    NavigationMenuContent,
    NavigationMenuItem,
    NavigationMenuLink,
    NavigationMenuList,
    NavigationMenuTrigger,
    navigationMenuTriggerStyle,
} from '../ui/navigation-menu'
import './HeaderMenu.css'
import { Link } from 'react-router-dom'

const components: { title: string; href: string; description: string }[] = [
    {
        title: 'Client-Centric Services',
        href: '/#services',
        description:
            'Our platform offers a suite of robust features designed to streamline your development process.',
    },
    {
        title: 'How it works',
        href: '/#architecture',
        description: 'Organizes separate components, each supplying a mission.',
    },
    {
        title: 'Use the API',
        href: '/#api',
        description:
            'An easy-to-use API is available to query for all known vulnerabilities by either a commit hash, or a package version.',
    },
    {
        title: 'Use at local',
        href: '/#download',
        description: 'Install AnaDep locally using Docker Compose.',
    },
    {
        title: 'Dependency Schema',
        href: '/#format',
        description:
            'An example response showcasing the retrieval of a vulnerable dependency.',
    },
    {
        title: 'Get Unlimited Access',
        href: '/#pricing',
        description: '',
    },
    {
        title: 'Frequently Asked Questions',
        href: '/#faq',
        description: '',
    },
]

const NavMenu: React.FC = () => {
    return (
        <div className="header-menu">
            <NavigationMenu>
                <NavigationMenuList className="p-0">
                    <NavigationMenuItem>
                        <NavigationMenuTrigger>
                            Getting started
                        </NavigationMenuTrigger>
                        <NavigationMenuContent>
                            <ul className="grid w-[400px] gap-3 p-4 grid-cols-2 z-10">
                                {components.map((component) => (
                                    <ListItem
                                        key={component.title}
                                        title={component.title}
                                        href={component.href}
                                    >
                                        {component.description}
                                    </ListItem>
                                ))}
                            </ul>
                        </NavigationMenuContent>
                    </NavigationMenuItem>
                    <NavigationMenuItem className="p-0">
                        <NavigationMenuLink
                            className={navigationMenuTriggerStyle()}
                            asChild
                        >
                            <Link to="/namespace">Namespace</Link>
                        </NavigationMenuLink>
                    </NavigationMenuItem>
                    <NavigationMenuItem></NavigationMenuItem>
                </NavigationMenuList>
            </NavigationMenu>
        </div>
    )
}

const ListItem = React.forwardRef<
    React.ElementRef<'a'>,
    React.ComponentPropsWithoutRef<'a'>
>(({ className, title, children, ...props }, ref) => {
    return (
        <li>
            <NavigationMenuLink asChild>
                <a
                    ref={ref}
                    className={cn(
                        'block select-none space-y-1 rounded-md p-3 leading-none no-underline outline-none transition-colors hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground',
                        className
                    )}
                    {...props}
                >
                    <div className="text-sm font-medium leading-none">
                        {title}
                    </div>
                    <p className="line-clamp-2 text-sm leading-snug text-muted-foreground">
                        {children}
                    </p>
                </a>
            </NavigationMenuLink>
        </li>
    )
})
ListItem.displayName = 'ListItem'

export default NavMenu
