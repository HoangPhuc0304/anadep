import { Switch } from '../../ui/switch'
import { Button } from '../../ui/button'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from '../../ui/dropdown-menu'
import { useTheme } from './theme-provider'
import { Moon, Sun } from 'lucide-react'
import { Label } from '../../ui/label'

export function ModeToggle() {
    const { setTheme } = useTheme()

    return (
        <div className="flex items-center space-x-2 mx-2">
            <Switch id="dark-mode" />
        </div>
        // <DropdownMenu>
        //   <DropdownMenuTrigger asChild>
        //     <Button
        //       variant="ghost"
        //       size="icon"
        //       className="ghost mx-2"
        //     >
        //       <Sun className="h-[1.1rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
        //       <Moon className="absolute h-[1.1rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
        //       <span className="sr-only">Toggle theme</span>
        //     </Button>
        //   </DropdownMenuTrigger>
        //   <DropdownMenuContent align="end">
        //     <DropdownMenuItem onClick={() => setTheme("light")}>
        //       Light
        //     </DropdownMenuItem>
        //     <DropdownMenuItem onClick={() => setTheme("dark")}>
        //       Dark
        //     </DropdownMenuItem>
        //   </DropdownMenuContent>
        // </DropdownMenu>
    )
}
