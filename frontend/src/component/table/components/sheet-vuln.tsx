import React from 'react'
import { Button } from '../../ui/button'
import { Input } from '../../ui/input'
import {
    SheetClose,
    SheetContent,
    SheetDescription,
    SheetFooter,
    SheetHeader,
    SheetTitle,
} from '../../ui/sheet'
import { Label } from '../../ui/label'
import { LibraryScanUI } from '../../../model/library'
import { Link } from 'react-router-dom'
import { Badge } from '../../ui/badge'
import { severities } from '../../../data/helper'
import { ScrollArea } from '../../ui/scroll-area'
import ReactMarkdown from 'react-markdown'

const VulnerabilityDetailSheetContent: React.FC<{ lib: LibraryScanUI }> = ({
    lib,
}) => {
    const color = severities.find(
        (s) => s.value === lib?.vuln?.severity[0]?.ranking
    )?.color

    return (
        <SheetContent
            className="w-[540px] !max-w-[540px] p-2"
            key={lib.vuln.id}
        >
            <ScrollArea className="h-full p-4">
                <SheetHeader>
                    <SheetTitle className="font-bold">
                        {lib.info.name}:{lib.info.version}
                    </SheetTitle>
                    {/* <SheetDescription>
                        Make changes to your profile here. Click save when you're done.
                    </SheetDescription> */}
                </SheetHeader>
                <div className="grid gap-4 py-4">
                    <div className="grid grid-cols-4 gap-4">
                        <span className="items-start font-bold">Ecosystem</span>
                        <span className="col-span-3">{lib.info.ecosystem}</span>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">
                            Database Id
                        </span>
                        <Link
                            to={`/vuln/${lib.vuln.id}`}
                            target="_blank"
                            className="col-span-3 underline"
                        >
                            {lib.vuln.id}
                        </Link>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">Summary</span>
                        <span className="col-span-3">{lib.vuln.summary}</span>
                    </div>
                    <div className="grid grid-cols-4 gap-4">
                        <span className="items-start font-bold">Details</span>
                        <span className="col-span-3">
                            <ReactMarkdown>
                                {lib.vuln.details}
                            </ReactMarkdown>
                        </span>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">Aliases</span>
                        <span className="col-span-3">
                            {lib.vuln?.aliases?.map((alias, index) => (
                                <Badge key={index} className="mx-1">
                                    {alias}
                                </Badge>
                            ))}
                        </span>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">Published</span>
                        <span className="col-span-3">{lib.vuln.published}</span>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">Modified</span>
                        <span className="col-span-3">{lib.vuln.modified}</span>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">Score</span>
                        <span className="col-span-3">
                            {lib.vuln.severity[0]?.baseScore}
                        </span>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">Severity</span>
                        <span className="col-span-3">
                            <Badge
                                className="mx-1"
                                style={{
                                    backgroundColor: `${color ? color : 'hsl(var(--foreground))'}`,
                                }}
                            >
                                {lib.vuln.severity[0]?.ranking}
                            </Badge>
                        </span>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">
                            Fix version
                        </span>
                        <span className="col-span-3">{lib.vuln.fixed}</span>
                    </div>
                    <div className="grid grid-cols-4 items-start gap-4">
                        <span className="items-start font-bold">
                            References
                        </span>
                        <div className="col-span-3">
                            {lib.vuln?.references?.map((refer, index) => (
                                <Link
                                    key={index}
                                    to={refer?.url || '#'}
                                    target="_blank"
                                    className="col-span-3 underline"
                                >
                                    <div>{refer.url}</div>
                                </Link>
                            ))}
                        </div>
                    </div>
                </div>
                <SheetFooter>
                    {/* <SheetClose asChild>
                        <Button type="submit">Save changes</Button>
                    </SheetClose> */}
                </SheetFooter>
            </ScrollArea>
        </SheetContent>
    )
}

export default VulnerabilityDetailSheetContent
