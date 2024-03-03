// import {
//   ArrowDownIcon,
//   ArrowRightIcon,
//   ArrowUpIcon,
//   CheckCircledIcon,
//   CircleIcon,
//   CrossCircledIcon,
//   QuestionMarkCircledIcon,
//   StopwatchIcon,
// } from "@radix-ui/react-icons"

export const labels = [
    {
        value: 'bug',
        label: 'Bug',
    },
    {
        value: 'feature',
        label: 'Feature',
    },
    {
        value: 'documentation',
        label: 'Documentation',
    },
]

// export const statuses = [
//   {
//     value: "backlog",
//     label: "Backlog",
//     icon: QuestionMarkCircledIcon,
//   },
//   {
//     value: "todo",
//     label: "Todo",
//     icon: CircleIcon,
//   },
//   {
//     value: "in progress",
//     label: "In Progress",
//     icon: StopwatchIcon,
//   },
//   {
//     value: "done",
//     label: "Done",
//     icon: CheckCircledIcon,
//   },
//   {
//     value: "canceled",
//     label: "Canceled",
//     icon: CrossCircledIcon,
//   },
// ]

// export const priorities = [
//   {
//     label: "Low",
//     value: "low",
//     icon: ArrowDownIcon,
//   },
//   {
//     label: "Medium",
//     value: "medium",
//     icon: ArrowRightIcon,
//   },
//   {
//     label: "High",
//     value: "high",
//     icon: ArrowUpIcon,
//   },
// ]

export interface FilterOption {
    label: string
    color?: string
    value: string
}

export const severities: FilterOption[] = [
    {
        label: 'Low',
        color: '#fcd34d',
        value: 'Low',
    },
    {
        label: 'Medium',
        color: '#f59e0b',
        value: 'Medium',
    },
    {
        label: 'High',
        color: '#e93838',
        value: 'High',
    },
    {
        label: 'Critical',
        color: '#9f1420',
        value: 'Critical',
    },
]

export const ecosystems: FilterOption[] = [
    {
        label: 'Maven',
        value: 'Maven',
    },
    {
        label: 'Npm',
        value: 'Npm',
    },
]
