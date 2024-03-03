'use client'

import {
    CommandGroup,
    CommandItem,
    CommandList,
    CommandInput,
} from '../../ui/command'
import { Command as CommandPrimitive } from 'cmdk'
import {
    useState,
    useRef,
    useCallback,
    type KeyboardEvent,
    useEffect,
} from 'react'

import { Skeleton } from '../../ui/skeleton'
import { cn } from '../../../lib/utils'
import { Check } from 'lucide-react'

type AutoCompleteProps = {
    setNameValue: React.Dispatch<React.SetStateAction<string>>
    options: string[]
    placeholder?: string
    emptyMessage?: string
    disabled?: boolean
}

export const AutoComplete = ({
    setNameValue,
    options,
    placeholder,
    emptyMessage,
    disabled,
}: AutoCompleteProps) => {
    const inputRef = useRef<HTMLInputElement>(null)

    const [isLoading, setLoading] = useState(false)
    const [isOpen, setOpen] = useState(false)
    const [selected, setSelected] = useState<string>('')
    const [inputValue, setInputValue] = useState<string>('')

    const handleKeyDown = useCallback(
        (event: KeyboardEvent<HTMLDivElement>) => {
            const input = inputRef.current
            if (!input) {
                return
            }

            // Keep the options displayed when the user is typing
            if (!isOpen) {
                setOpen(true)
            }

            // This is not a default behaviour of the <input /> field
            if (event.key === 'Enter' && input.value !== '') {
                const optionToSelect = options.find(
                    (option) => option === input.value
                )
                if (optionToSelect) {
                    setSelected(optionToSelect)
                    setInputValue(optionToSelect)
                }
            }

            if (event.key === 'Escape') {
                input.blur()
            }
        },
        [isOpen, options, setInputValue]
    )

    const handleBlur = () => {
        setOpen(false)
    }

    const handleSelectOption = (selectedOption: string) => {
        setInputValue(selectedOption)
        setSelected(selectedOption)
        setTimeout(() => {
            inputRef?.current?.blur()
        }, 0)
    }

    useEffect(() => {
        setNameValue(inputValue)
    }, [inputValue])

    return (
        <CommandPrimitive
            onKeyDown={handleKeyDown}
            className="h-10 rounded-md border border-input bg-background text-sm"
        >
            <div className="border-none">
                <CommandInput
                    ref={inputRef}
                    value={inputValue}
                    onValueChange={setInputValue}
                    onBlur={handleBlur}
                    onFocus={() => setOpen(true)}
                    placeholder={placeholder || ''}
                    className="text-sm border-none py-0 h-10"
                    disabled={disabled}
                />
            </div>
            <div className="mt-1 relative">
                {isOpen ? (
                    <div className="absolute top-0 z-10 w-full rounded-xl bg-stone-50 outline-none animate-in fade-in-0 zoom-in-95">
                        <CommandList className="rounded-md border border-input bg-background text-sm shadow-lg">
                            {isLoading ? (
                                <CommandPrimitive.Loading>
                                    <div className="p-1">
                                        <Skeleton className="h-8 w-full" />
                                    </div>
                                </CommandPrimitive.Loading>
                            ) : null}
                            {options.length > 0 && !isLoading ? (
                                <CommandGroup>
                                    {options.map((option) => {
                                        const isSelected = selected === option
                                        return (
                                            <CommandItem
                                                key={option}
                                                value={option}
                                                onMouseDown={(event) => {
                                                    event.preventDefault()
                                                    event.stopPropagation()
                                                }}
                                                onSelect={() =>
                                                    handleSelectOption(option)
                                                }
                                                className={cn(
                                                    'flex items-center gap-2 w-full',
                                                    !isSelected ? 'pl-8' : null
                                                )}
                                            >
                                                {isSelected ? (
                                                    <Check className="w-4" />
                                                ) : null}
                                                {option}
                                            </CommandItem>
                                        )
                                    })}
                                </CommandGroup>
                            ) : (
                                <CommandPrimitive.Empty className="select-none rounded-sm px-2 py-3 text-sm text-center">
                                    {emptyMessage}
                                </CommandPrimitive.Empty>
                            )}
                        </CommandList>
                    </div>
                ) : null}
            </div>
        </CommandPrimitive>
    )
}
