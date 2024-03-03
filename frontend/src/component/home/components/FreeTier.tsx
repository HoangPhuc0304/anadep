import { Link } from 'react-router-dom'
import { Badge } from '../../ui/badge'
import { Button } from '../../ui/button'
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from '../../ui/card'
import { Check } from 'lucide-react'

enum PopularPlanType {
    NO = 0,
    YES = 1,
}

interface PricingProps {
    title: string
    popular: PopularPlanType
    price: number
    description: string
    buttonText: string
    benefitList: string[]
}

const pricingList: PricingProps[] = [
    {
        title: 'Free',
        popular: 0,
        price: 0,
        description:
            'All features and services offered within this application are completely free of charge.',
        buttonText: 'Get Started',
        benefitList: [
            'Service Bill of Materials',
            'Vulnerability Detection',
            'Report Generation',
            'GitHub Integration',
        ],
    },
]

export const FreeTier = () => {
    return (
        <section id="pricing" className="container py-24 sm:py-32">
            <h2 className="text-3xl md:text-4xl font-bold text-center">
                Get Unlimited Access
            </h2>
            <br />
            <div className="flex items-center justify-center">
                {pricingList.map((pricing: PricingProps) => (
                    <Card
                        key={pricing.title}
                        className={
                            'w-[450px] ' +
                            `${
                                pricing.popular === PopularPlanType.YES
                                    ? 'drop-shadow-xl shadow-black/10 dark:shadow-white/10'
                                    : ''
                            }`
                        }
                    >
                        <CardHeader>
                            <CardTitle className="flex item-center justify-between">
                                {pricing.title}
                                {pricing.popular === PopularPlanType.YES ? (
                                    <Badge
                                        variant="secondary"
                                        className="text-sm text-primary"
                                    >
                                        Most popular
                                    </Badge>
                                ) : null}
                            </CardTitle>
                            <div>
                                <span className="text-3xl font-bold">
                                    ${pricing.price}
                                </span>
                                <span className="text-muted-foreground">
                                    {' '}
                                    /month
                                </span>
                            </div>

                            <CardDescription>
                                {pricing.description}
                            </CardDescription>
                        </CardHeader>

                        <CardContent>
                            <Link to="/namespace" target="_blank">
                                <Button className="w-full">
                                    {pricing.buttonText}
                                </Button>
                            </Link>
                        </CardContent>

                        <hr className="w-4/5 m-auto mb-4" />

                        <CardFooter className="flex">
                            <div className="space-y-4">
                                {pricing.benefitList.map((benefit: string) => (
                                    <span key={benefit} className="flex">
                                        <Check className="text-green-500" />{' '}
                                        <h3 className="ml-2">{benefit}</h3>
                                    </span>
                                ))}
                            </div>
                        </CardFooter>
                    </Card>
                ))}
            </div>
        </section>
    )
}
