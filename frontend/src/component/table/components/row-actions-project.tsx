'use client'

import { DotsHorizontalIcon } from '@radix-ui/react-icons'
import { Row } from '@tanstack/react-table'

import { Button } from '../../ui/button'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuShortcut,
    DropdownMenuTrigger,
} from '../../ui/dropdown-menu'

import {
    downloadFileFormGitHubUrl,
    generateReport,
    getAuthAnalysisUIResult,
    removeRepoById,
} from '../../../api/apiCall'
import { ReportForm, Repository, User } from '../../../model/library'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '../../../redux/store'
import { ToastAction } from '../../../component/ui/toast'
import { useToast } from '../../../component/ui/use-toast'
import {
    DEFAULT_ERROR_MESSAGE,
    ERROR_LABEL,
    GENERATE_REPORT_SUCCESS_MESSAGE,
    REMOVE_PROJECT_SUCCESS_MESSAGE,
    SUCCESS_LABEL,
    VULN_ANALYSIS_SUCCESS_MESSAGE,
} from '../../../common/common'
import { update } from '../../../redux/slice/vulnScanSlice'
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from '../../../component/ui/dialog'
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '../../../component/ui/form'
import { Input } from '../../../component/ui/input'
import { RadioGroup, RadioGroupItem } from '../../../component/ui/radio-group'
import { z } from 'zod'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { ScrollArea } from '../../../component/ui/scroll-area'

interface DataTableRowActionsProps<TData> {
    row: Row<Repository>
}

const formSchema = z.object({
    projectName: z.string().max(100, {
        message: 'Project name must not be longer than 100 characters.',
    }),
    author: z.string().max(100, {
        message: 'Author name must not be longer than 100 characters.',
    }),
    type: z.enum(['vulns', 'sbom'], {
        required_error: 'You need to select a type.',
    }),
    format: z.enum(['pdf', 'excel', 'json'], {
        required_error: 'You need to select a format.',
    }),
})

const defaultValues: Partial<ReportForm> = {}

export function DataTableRowProjectActions<TData>({
    row,
}: DataTableRowActionsProps<TData>) {
    const { toast } = useToast()
    const dispatch = useDispatch()
    const user: User = useSelector((state: RootState) => state.user.currentUser)
    const form = useForm<ReportForm>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            projectName: row.original.fullName,
            author: user.login,
        },
    })

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const handlingBuild = async () => {
        handlingToastAction(
            'Build Processing',
            'This action can take some minutes'
        )

        if (row.original.githubUrl && user.githubToken) {
            const downloadData = await downloadFileFormGitHubUrl(
                row.original.githubUrl,
                user.githubToken
            )
            if (typeof downloadData === 'string') {
                handlingToastAction(
                    ERROR_LABEL,
                    downloadData || DEFAULT_ERROR_MESSAGE
                )
                return
            }

            const blob = new Blob([downloadData], { type: 'application/zip' })
            const file = new File([blob], 'source.zip')
            if (file && row.original.id) {
                const analysisData = await getAuthAnalysisUIResult(
                    file,
                    row.original.id,
                    user.githubToken
                )
                if (typeof analysisData === 'string') {
                    handlingToastAction(
                        ERROR_LABEL,
                        analysisData || DEFAULT_ERROR_MESSAGE
                    )
                } else {
                    dispatch(update(analysisData))
                    handlingToastAction(
                        SUCCESS_LABEL,
                        VULN_ANALYSIS_SUCCESS_MESSAGE
                    )
                }
            }
        } else {
            handlingToastAction(ERROR_LABEL, DEFAULT_ERROR_MESSAGE)
        }
    }

    const handlingReport = async (reportData: ReportForm) => {
        let data: string | boolean = ''
        if (reportData.type === 'vulns') {
            if (
                row.original.vulnerabilityResult &&
                row.original.vulnerabilityResult?.libs.length > 0
            ) {
                data = await generateReport(
                    row.original.vulnerabilityResult,
                    reportData,
                    'vulns'
                )
            }
        } else {
            if (
                row.original.scanningResult &&
                row.original.scanningResult?.libraries.length > 0
            ) {
                data = await generateReport(
                    row.original.scanningResult,
                    reportData,
                    'sbom'
                )
            }
        }
        if (typeof data === 'string' || !data) {
            handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
        } else {
            handlingToastAction(SUCCESS_LABEL, GENERATE_REPORT_SUCCESS_MESSAGE)
        }
    }

    const handlingSubmit = async (data: ReportForm) => {
        handlingToastAction(
            'Report Processing',
            'This action can take some minutes'
        )
        await handlingReport(data)
    }

    const handlingDeleteRepository = async () => {
        if (row.original.id && user.githubToken) {
            const data = await removeRepoById(row.original.id, user.githubToken)
            if (!data) {
                handlingToastAction(ERROR_LABEL, DEFAULT_ERROR_MESSAGE)
            } else {
                handlingToastAction(
                    SUCCESS_LABEL,
                    REMOVE_PROJECT_SUCCESS_MESSAGE
                )
            }
        }
    }

    return (
        <Dialog>
            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                    <Button
                        variant="ghost"
                        className="flex h-8 w-8 p-0 data-[state=open]:bg-muted"
                    >
                        <DotsHorizontalIcon className="h-4 w-4" />
                        <span className="sr-only">Open menu</span>
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-[160px]">
                    <DropdownMenuItem onClick={handlingBuild}>
                        Build
                    </DropdownMenuItem>
                    <DialogTrigger asChild>
                        <DropdownMenuItem>Generate Report</DropdownMenuItem>
                    </DialogTrigger>
                    <DropdownMenuSeparator />
                    <DropdownMenuItem onClick={handlingDeleteRepository}>
                        Delete
                        <DropdownMenuShortcut>⌘⌫</DropdownMenuShortcut>
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
            <DialogContent className="sm:max-w-[425px]">
                <ScrollArea className="w-full px-4 mb-4" style={{height: "calc(100vh - 200px)"}}>
                    <Form {...form}>
                        <form
                            onSubmit={form.handleSubmit(handlingSubmit)}
                            className="space-y-8"
                        >
                            <DialogHeader>
                                <DialogTitle>Export scan data</DialogTitle>
                                <DialogDescription>
                                    Please provide project information and format of
                                    data you like to export
                                </DialogDescription>
                            </DialogHeader>
                            <FormField
                                control={form.control}
                                name="projectName"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Project Name</FormLabel>
                                        <FormControl>
                                            <Input
                                                placeholder="Your project name"
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormDescription>
                                            This is the project name that will be
                                            displayed on your report.
                                        </FormDescription>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name="author"
                                render={({ field }) => (
                                    <FormItem className="flex flex-col">
                                        <FormLabel>Author</FormLabel>
                                        <FormControl>
                                            <Input
                                                placeholder="Author name"
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormDescription>
                                            This is the author name that will be
                                            displayed on your report.
                                        </FormDescription>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name="type"
                                render={({ field }) => (
                                    <FormItem className="space-y-3">
                                        <FormLabel>Please choose a type</FormLabel>
                                        <FormControl>
                                            <RadioGroup
                                                onValueChange={field.onChange}
                                                defaultValue={field.value}
                                                className="flex flex-col space-y-1"
                                            >
                                                <FormItem className="flex items-center space-x-3 space-y-0">
                                                    <FormControl>
                                                        <RadioGroupItem value="vulns" />
                                                    </FormControl>
                                                    <FormLabel className="font-normal">
                                                        Vulnerability
                                                    </FormLabel>
                                                </FormItem>
                                                <FormItem className="flex items-center space-x-3 space-y-0">
                                                    <FormControl>
                                                        <RadioGroupItem value="sbom" />
                                                    </FormControl>
                                                    <FormLabel className="font-normal">
                                                        SBOM
                                                    </FormLabel>
                                                </FormItem>
                                            </RadioGroup>
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name="format"
                                render={({ field }) => (
                                    <FormItem className="space-y-3">
                                        <FormLabel>
                                            Please choose a format
                                        </FormLabel>
                                        <FormControl>
                                            <RadioGroup
                                                onValueChange={field.onChange}
                                                defaultValue={field.value}
                                                className="flex flex-col space-y-1"
                                            >
                                                <FormItem className="flex items-center space-x-3 space-y-0">
                                                    <FormControl>
                                                        <RadioGroupItem value="pdf" />
                                                    </FormControl>
                                                    <FormLabel className="font-normal">
                                                        PDF
                                                    </FormLabel>
                                                </FormItem>
                                                <FormItem className="flex items-center space-x-3 space-y-0">
                                                    <FormControl>
                                                        <RadioGroupItem value="excel" />
                                                    </FormControl>
                                                    <FormLabel className="font-normal">
                                                        EXCEL
                                                    </FormLabel>
                                                </FormItem>
                                                <FormItem className="flex items-center space-x-3 space-y-0">
                                                    <FormControl>
                                                        <RadioGroupItem value="json" />
                                                    </FormControl>
                                                    <FormLabel className="font-normal">
                                                        JSON
                                                    </FormLabel>
                                                </FormItem>
                                            </RadioGroup>
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <DialogFooter>
                                <DialogClose asChild>
                                    <Button type="button" variant="secondary">
                                        Cancel
                                    </Button>
                                </DialogClose>
                                <Button type="submit">Export</Button>
                            </DialogFooter>
                        </form>
                    </Form>
                </ScrollArea>
            </DialogContent>
        </Dialog>
    )
}
