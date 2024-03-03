import { GiftIcon, MapIcon, MedalIcon, PlaneIcon } from './Icons'
import { Card, CardContent, CardHeader, CardTitle } from '../../ui/card'

interface ArchitectureProps {
    icon: JSX.Element
    title: string
    description: string
}

const architectures: ArchitectureProps[] = [
    {
        icon: (
            <svg
                xmlns="http://www.w3.org/2000/svg"
                enable-background="new 0 0 24 24"
                viewBox="0 0 24 24"
                className="w-14 fill-primary"
                id="download"
            >
                <path d="M8.1624146,13.6552734c-0.1972046,0.1932983-0.2003784,0.5098267-0.0071411,0.7070312l3.4912109,3.4912109C11.7401123,17.9474487,11.8673706,18.0001831,12,18c0.1326294,0.0001221,0.2598267-0.0525513,0.3535156-0.1464844l3.4912109-3.4912109c0.1904907-0.194397,0.1904907-0.5054321,0-0.6998291c-0.1932983-0.1972046-0.5098267-0.2004395-0.7070312-0.0072021L12.5,16.2929688V2.5C12.5,2.223877,12.276123,2,12,2s-0.5,0.223877-0.5,0.5v13.7929688l-2.6377563-2.6376953C8.6679077,13.4647827,8.3568115,13.4647827,8.1624146,13.6552734z M18,9h-1.5C16.223877,9,16,9.223877,16,9.5s0.223877,0.5,0.5,0.5H18c1.1040039,0.0014038,1.9985962,0.8959961,2,2v7c-0.0014038,1.1040039-0.8959961,1.9985962-2,2H6c-1.1040039-0.0014038-1.9985962-0.8959961-2-2v-7c0.0014038-1.1040039,0.8959961-1.9985962,2-2h2.5C8.776123,10,9,9.776123,9,9.5S8.776123,9,8.5,9H6c-1.6561279,0.0018311-2.9981689,1.3438721-3,3v7c0.0018311,1.6561279,1.3438721,2.9981689,3,3h12c1.6561279-0.0018311,2.9981689-1.3438721,3-3v-7C20.9981689,10.3438721,19.6561279,9.0018311,18,9z" />
            </svg>
        ),
        title: 'Downloader',
        description:
            'Access project source code via user uploads or GitHub repositories',
    },
    {
        icon: (
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 256 256"
                className="w-14 fill-primary"
                id="barcode"
            >
                <rect width="256" height="256" fill="none" />
                <polyline
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="12"
                    points="184 48 224 48 224 88"
                />
                <polyline
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="12"
                    points="72 208 32 208 32 168"
                />
                <polyline
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="12"
                    points="224 168 224 208 184 208"
                />
                <polyline
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="12"
                    points="32 88 32 48 72 48"
                />
                <line
                    x1="80"
                    x2="80"
                    y1="88"
                    y2="168"
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="12"
                />
                <line
                    x1="176"
                    x2="176"
                    y1="88"
                    y2="168"
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="12"
                />
                <line
                    x1="144"
                    x2="144"
                    y1="88"
                    y2="168"
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="12"
                />
                <line
                    x1="112"
                    x2="112"
                    y1="88"
                    y2="168"
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="12"
                />
            </svg>
        ),
        title: 'Scanner',
        description:
            'Identify all direct and transitive dependencies using various package managers, supporting over a dozen different ecosystems',
    },
    {
        icon: (
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 52 52"
                className="w-14 fill-primary"
                id="analytics"
            >
                <g
                    fill="none"
                    stroke="#000"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-miterlimit="10"
                    stroke-width="2"
                >
                    <circle cx="6.738" cy="41.943" r="4.738" />
                    <circle cx="20.633" cy="21.521" r="4.738" />
                    <circle cx="28.955" cy="37.205" r="4.738" />
                    <circle cx="45.262" cy="10.057" r="4.738" />
                    <path d="m17.973 25.444-8.57 12.58M42.828 14.112 31.395 33.149M26.734 33.014l-3.875-7.3" />
                </g>
            </svg>
        ),
        title: 'Analyzer',
        description:
            'Retrieve project vulnerabilities from diverse database providers organized by OSV',
    },
    {
        icon: <MedalIcon />,
        title: 'Evaluator',
        description:
            'Assess vulnerability severity and provide CVSS scores for informed decision-making',
    },
    {
        icon: (
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 32 32"
                className="w-14 fill-primary"
                id="report"
            >
                <path d="m16.09 12.33 8.74 2.147a.499.499 0 0 0 .239-.97l-8.74-2.147a.502.502 0 0 0-.604.366.497.497 0 0 0 .365.604zm10.204 4.567L13.67 13.796a.499.499 0 1 0-.238.97l12.625 3.102a.499.499 0 0 0 .237-.971zm-.715 2.914-12.625-3.102a.499.499 0 1 0-.238.97l12.625 3.102a.499.499 0 0 0 .238-.97zm-.716 2.913-8.209-2.017a.499.499 0 1 0-.238.97l8.209 2.017a.499.499 0 0 0 .238-.97zm-.716 2.913-2.375-.583a.5.5 0 0 0-.238.97l2.375.583a.499.499 0 0 0 .238-.97zM17.776 9.655l5.827 1.432a.499.499 0 0 0 .239-.97L18.5 8.804l.239-.972 5.341 1.313a.499.499 0 1 0 .238-.97l-5.827-1.432a.5.5 0 0 0-.604.366l-.477 1.942a.496.496 0 0 0 .366.604z" />
                <path d="M15.637 28.806c.226-.017.438-.087.63-.197l1.205 1.04a1.494 1.494 0 0 0 1.09.359c.4-.029.764-.213 1.025-.516l.611-.708 4.937 1.213a.499.499 0 0 0 .605-.366l4.295-17.479c.005-.022.001-.044.003-.066a.493.493 0 0 0-.06-.312l-2.93-4.839a.489.489 0 0 0-.25-.199c-.02-.008-.037-.022-.058-.027l-2.99-.734-3.841-3.841a.5.5 0 0 0-.286-.133c-.023-.003-.044-.013-.068-.013h-13a.5.5 0 0 0-.5.5V13.78l-3.481-.883a.5.5 0 0 0-.584.677l1.721 4.128.008.01a.492.492 0 0 0 .127.176l2.21 1.907v4.692a.5.5 0 0 0 .5.5h1.791l-.087.351a.499.499 0 0 0 .366.604l5.411 1.33c-.002.049-.01.098-.007.148.029.399.212.764.516 1.025a1.49 1.49 0 0 0 1.091.361zm2.851.206a.482.482 0 0 1-.363-.12l-1.135-.98 1.96-2.27 1.135.98a.496.496 0 0 1 .052.704l-.496.574-.811.94a.502.502 0 0 1-.342.172zM6.837 15.186l3.463 2.99 6.377 5.504-.653.757-9.841-8.494.654-.757zm6.177-9.199h-.959v-1h1.205l-.246 1zm-2.678 10.897-1.039-.897h1.26l-.221.897zm14.552 12.024-3.958-.973c.008-.01.01-.022.017-.032.106-.139.189-.29.24-.453.003-.009.003-.018.006-.028a1.488 1.488 0 0 0-.455-1.56l-1.2-1.036a1.504 1.504 0 0 0-.418-1.682 1.504 1.504 0 0 0-1.724-.167l-6.211-5.361 3.174-12.92L26.013 7.56l-.835 3.398a.499.499 0 0 0 .366.604l3.399.836-4.055 16.51zm3.119-18.456.489.808-2.227-.548.547-2.226 1.191 1.966zm-6.132-4.938-1.819-.447V3.694l1.819 1.82zM7.055 2.987h12v1.834l-4.942-1.214a.501.501 0 0 0-.604.366l-.003.014h-1.95a.5.5 0 0 0-.5.5v2a.5.5 0 0 0 .5.5h1.213l-.491 2h-1.722a.5.5 0 0 0 0 1h1.476l-.491 2H8.555a.5.5 0 0 0 0 1h2.739l-.491 2H8.555a.493.493 0 0 0-.294.106l-1.149-.992a.465.465 0 0 0-.057-.042V2.987zM4.322 16.569l-1.025-2.458 2.581.655-1.181 1.368-.375.435zm1.208.131 9.841 8.494-.653.757-4.835-4.173-3.001-2.591-.002-.001-2.002-1.728.652-.758zm1.525 3.958 1.943 1.677-.406 1.652H7.055v-3.329zm2.792 2.41 3.473 2.998-3.97-.975.497-2.023zm5.181 4.279a.499.499 0 0 1 .048-.238c.005-.01.017-.015.021-.025.016-.035.035-.077.047-.094.002-.002.002-.005.004-.006l1.305-1.512.002-.001.001-.002 1.305-1.512a.47.47 0 0 1 .166-.123c.022-.01.045-.012.068-.019a.472.472 0 0 1 .128-.027.536.536 0 0 1 .113.013c.022.004.044.004.065.011a.5.5 0 0 1 .216.798l-2.614 3.027a.495.495 0 0 1-.706.052.511.511 0 0 1-.169-.342z" />
            </svg>
        ),
        title: 'Reporter',
        description:
            'Generate visual reports in multiple formats (e.g., PDF, CSV, JSON) for comprehensive analysis and communication',
    },
]

const Architecture: React.FC = () => {
    // const data = fs.readFileSync('../../data/analyze-response.json', { encoding: 'utf-8', flag: 'r' });
    // const analysisResultList: AnalysisResult = JSON.parse(data);

    return (
        <section
            id="architecture"
            className="container text-center py-24 sm:py-32"
        >
            <h2 className="text-3xl md:text-4xl font-bold ">How it works</h2>
            <p className="md:w-3/4 mx-auto mt-4 mb-8 text-xl text-muted-foreground">
                Organizes separate components, each supplying a mission.
                Together, they form a unified block, streamlining operations for
                successful mission delivery
            </p>

            <div className="grid grid-cols-5 gap-4">
                {architectures.map(
                    ({ icon, title, description }: ArchitectureProps) => (
                        <Card key={title} className="bg-muted/50">
                            <CardHeader>
                                <CardTitle className="grid gap-4 place-items-center">
                                    {icon}
                                    {title}
                                </CardTitle>
                            </CardHeader>
                            <CardContent>{description}</CardContent>
                        </Card>
                    )
                )}
            </div>
        </section>
    )
}

export default Architecture
