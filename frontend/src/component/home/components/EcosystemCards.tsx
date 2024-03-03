import { Avatar, AvatarFallback, AvatarImage } from '../../ui/avatar'
import { Badge } from '../../ui/badge'
import { Button, buttonVariants } from '../../ui/button'
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
    CardFooter,
} from '../../ui/card'
import { Check, CheckCheckIcon, Linkedin } from 'lucide-react'
import { CheckIcon, LightBulbIcon } from './Icons'
import { GitHubLogoIcon } from '@radix-ui/react-icons'

export const EcosystemCards = () => {
    return (
        <div className="hidden lg:flex flex-row flex-wrap gap-8 relative w-[500px] h-[500px] items-center justify-center">
            <div
                className="rounded-full w-[420px] h-[420px]  flex items-center justify-center"
                style={{
                    background:
                        'linear-gradient(90deg, #D247BF 0%, #F596D3 25%, #61DAFB 50%, #1fc0f1 75%, #03a3d7 100%)',
                }}
            >
                <Card className="w-full rounded-full w-[400px] h-[400px] flex items-center justify-center drop-shadow-xl shadow-black/10 dark:shadow-white/10">
                    <div className="space-y-1 flex md:flex-row justify-start items-start gap-4">
                        <div>
                            <h1 className="m-4 text-3xl font-bold">
                                Supported Ecosystems
                            </h1>
                            <div className="text-md mt-2">
                                <div className="grid grid-cols-2 gap-2 gap-x-12">
                                    <div className="flex">
                                        <CheckIcon />
                                        <span className="ml-2">4699 Maven</span>
                                    </div>
                                    <div className="flex">
                                        <CheckIcon />
                                        <span className="ml-2">14004 Npm</span>
                                    </div>
                                    <div className="flex">
                                        <CheckIcon />
                                        <span className="ml-2">562 NuGet</span>
                                    </div>
                                    <div className="flex">
                                        <CheckIcon />
                                        <span className="ml-2">11486 PyPI</span>
                                    </div>
                                    <div className="flex">
                                        <CheckIcon />
                                        <span className="ml-2">1941 Go</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    )
}
