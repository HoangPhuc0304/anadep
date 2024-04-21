import { VulnerabilityResponse } from '../../model/library'
import { useEffect, useState } from 'react'
import { Badge } from '../../component/ui/badge'
import { Link, useParams } from 'react-router-dom'
import {
    Accordion,
    AccordionContent,
    AccordionItem,
    AccordionTrigger,
} from '../../component/ui/accordion'
import { getVulnerabilityFromId } from '../../api/apiCall'
import { ToastAction } from '../../component/ui/toast'
import { useToast } from '../../component/ui/use-toast'
import { DEFAULT_ERROR_MESSAGE, ERROR_LABEL } from '../../common/common'
import { Skeleton } from '../../component/ui/skeleton'
import ReactMarkdown from 'react-markdown'

export default function DetailsPage() {
    const [vuln, setVuln] = useState<VulnerabilityResponse>()
    const [loading, setLoading] = useState<boolean>(true)
    const { toast } = useToast()
    const { id } = useParams()

    useEffect(() => {
        id && fetchData(id)
    }, [id])

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const fetchData = async (id: string) => {
        const data = await getVulnerabilityFromId(id)
        if (typeof data === 'string') {
            handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
        } else {
            setVuln(data)
        }
        setLoading(false)
    }

    return (
        <div className="vulnerability-scan-container mb-4">
            <section className="hidden md:block">
                <div className="overflow-hidden rounded-lg border bg-background shadow-lg">
                    <div className="hidden flex-col md:flex p-4">
                        <h1 className="font-bold text-3xl">{vuln?.id}</h1>
                        <div className="grid gap-4 py-4">
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    Source
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <Link
                                        to={`https://osv.dev/vulnerability/${vuln?.id}`}
                                        target="_blank"
                                        className="col-span-3 underline"
                                    >
                                        <div>
                                            https://osv.dev/vulnerability/
                                            {vuln?.id}
                                        </div>
                                    </Link>
                                )}
                            </div>
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    Aliases
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <span className="col-span-3">
                                        {vuln?.aliases.map((alias, index) => (
                                            <Badge key={index} className="m-1">
                                                {alias}
                                            </Badge>
                                        ))}
                                    </span>
                                )}
                            </div>
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    Summary
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <span className="col-span-3">
                                        {vuln?.summary}
                                    </span>
                                )}
                            </div>
                            <div className="grid grid-cols-4 gap-4">
                                <span className="items-start font-bold">
                                    Details
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <span className="col-span-3">
                                        <ReactMarkdown>
                                            {vuln?.details}
                                        </ReactMarkdown>
                                    </span>
                                )}
                            </div>
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    Published
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <span className="col-span-3">
                                        {vuln?.published}
                                    </span>
                                )}
                            </div>
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    Modified
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <span className="col-span-3">
                                        {vuln?.modified}
                                    </span>
                                )}
                            </div>
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    Score
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <span className="col-span-3">
                                        {vuln?.severity[0].score}
                                    </span>
                                )}
                            </div>
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    Severity
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <span className="col-span-3">
                                        <Badge>
                                            {vuln?.database_specific?.severity}
                                        </Badge>
                                    </span>
                                )}
                            </div>
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    References
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <div className="col-span-3">
                                        {vuln?.references.map(
                                            (refer, index) => (
                                                <Link
                                                    key={index}
                                                    to={refer?.url || '#'}
                                                    target="_blank"
                                                    className="col-span-3 underline"
                                                >
                                                    <div>{refer.url}</div>
                                                </Link>
                                            )
                                        )}
                                    </div>
                                )}
                            </div>
                            <div className="grid grid-cols-4 items-start gap-4">
                                <span className="items-start font-bold">
                                    Affected
                                </span>
                                {loading ? (
                                    <Skeleton className="h-[24px] w-full" />
                                ) : (
                                    <div className="col-span-3">
                                        <Accordion
                                            type="single"
                                            collapsible
                                            className="w-full"
                                        >
                                            {vuln?.affected?.map(
                                                (affected, index) => (
                                                    <AccordionItem
                                                        value={`${index}`}
                                                    >
                                                        <AccordionTrigger>{`${affected?.package?.ecosystem} / ${affected?.package?.name}`}</AccordionTrigger>
                                                        <AccordionContent>
                                                            <div className="grid grid-cols-4 items-start gap-4 mb-2">
                                                                <span className="items-start font-bold">
                                                                    Affected
                                                                    ranges
                                                                </span>
                                                                {affected.ranges &&
                                                                    affected.ranges.map(
                                                                        (
                                                                            range,
                                                                            index
                                                                        ) => (
                                                                            <div
                                                                                key={
                                                                                    index
                                                                                }
                                                                                className="col-span-3"
                                                                            >
                                                                                <div className="grid grid-cols-4 items-start gap-4">
                                                                                    <span className="items-start font-bold">
                                                                                        Type
                                                                                    </span>
                                                                                    <span className="col-span-3">
                                                                                        {
                                                                                            range.type
                                                                                        }
                                                                                    </span>
                                                                                </div>
                                                                                <div className="grid grid-cols-4 items-start gap-4">
                                                                                    <span className="items-start font-bold">
                                                                                        Events
                                                                                    </span>
                                                                                    <span className="col-span-3">
                                                                                        {range?.events?.map(
                                                                                            (
                                                                                                event,
                                                                                                index
                                                                                            ) => (
                                                                                                <div
                                                                                                    key={
                                                                                                        index
                                                                                                    }
                                                                                                >
                                                                                                    {event.introduced && (
                                                                                                        <div>
                                                                                                            <span className="items-start font-bold">
                                                                                                                Introduced
                                                                                                            </span>
                                                                                                            <span className="mx-2">
                                                                                                                {
                                                                                                                    event.introduced
                                                                                                                }
                                                                                                            </span>
                                                                                                        </div>
                                                                                                    )}
                                                                                                    {event.fixed && (
                                                                                                        <div>
                                                                                                            <span className="items-start font-bold">
                                                                                                                Fixed
                                                                                                            </span>
                                                                                                            <span className="mx-2">
                                                                                                                {
                                                                                                                    event.fixed
                                                                                                                }
                                                                                                            </span>
                                                                                                        </div>
                                                                                                    )}
                                                                                                    {event.last_affected && (
                                                                                                        <div>
                                                                                                            <span className="items-start font-bold">
                                                                                                                Last
                                                                                                                Affected
                                                                                                            </span>
                                                                                                            <span className="mx-2">
                                                                                                                {
                                                                                                                    event.last_affected
                                                                                                                }
                                                                                                            </span>
                                                                                                        </div>
                                                                                                    )}
                                                                                                    {event.limit && (
                                                                                                        <div>
                                                                                                            <span className="items-start font-bold">
                                                                                                                Limit
                                                                                                            </span>
                                                                                                            <span className="mx-2">
                                                                                                                {
                                                                                                                    event.limit
                                                                                                                }
                                                                                                            </span>
                                                                                                        </div>
                                                                                                    )}
                                                                                                </div>
                                                                                            )
                                                                                        )}
                                                                                    </span>
                                                                                </div>
                                                                            </div>
                                                                        )
                                                                    )}
                                                            </div>
                                                            <div className="grid grid-cols-4 items-start gap-4">
                                                                <span className="items-start font-bold">
                                                                    Affected
                                                                    versions
                                                                </span>
                                                                <span className="col-span-3">
                                                                    {affected?.versions?.map(
                                                                        (
                                                                            version,
                                                                            index
                                                                        ) => (
                                                                            <Badge
                                                                                key={
                                                                                    index
                                                                                }
                                                                                className="m-1"
                                                                            >
                                                                                {
                                                                                    version
                                                                                }
                                                                            </Badge>
                                                                        )
                                                                    )}
                                                                </span>
                                                            </div>
                                                        </AccordionContent>
                                                    </AccordionItem>
                                                )
                                            )}
                                        </Accordion>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    )
}
