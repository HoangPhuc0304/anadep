import { ReactNode } from 'react'

export default function NamespacePage({ component }: { component: ReactNode }) {
    return (
        <div className="vulnerability-scan-container">
            <section className="hidden md:block">
                <div className="overflow-hidden rounded-lg border bg-background shadow-lg">
                    <div className="hidden flex-col md:flex">{component}</div>
                </div>
            </section>
        </div>
    )
}
