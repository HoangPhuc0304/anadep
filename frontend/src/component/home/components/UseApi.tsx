// import { GiftIcon, MapIcon, MedalIcon, PlaneIcon } from './Icons';
// import { Card, CardContent, CardHeader, CardTitle } from '../../ui/card';

import { Link } from 'react-router-dom'
import { Button } from '../../../component/ui/button'
import { CopyIcon } from '@radix-ui/react-icons'

const UseApi: React.FC = () => {
    // const data = fs.readFileSync('../../data/analyze-response.json', { encoding: 'utf-8', flag: 'r' });
    // const analysisResultList: AnalysisResult = JSON.parse(data);

    return (
        <section id="api" className="container text-center py-24 sm:py-32">
            <h2 className="text-3xl md:text-4xl font-bold ">Use the API</h2>
            <p className="md:w-3/4 mx-auto mt-4 mb-8 text-xl text-muted-foreground">
                An easy-to-use API is available to query for all known
                vulnerabilities by either a commit hash, or a package version.
            </p>

            <div className="grid grid-cols-1 md:grid-cols-1 lg:grid-cols-2 gap-8">
                <div className="grid gap-4">
                    <div className="rounded-md bg-black p-6 relative">
                        <pre>
                            <code className="grid gap-1 text-sm text-muted-foreground [&_span]:h-4 text-left">
                                <span>
                                    <span className="text-sky-300">import</span>{' '}
                                    os
                                </span>
                                <span>
                                    <span className="text-sky-300">import</span>{' '}
                                    openai
                                </span>
                                <span />
                                <span>
                                    openai.api_key = os.getenv(
                                    <span className="text-green-300">
                                        &quot;OPENAI_API_KEY&quot;
                                    </span>
                                    )
                                </span>
                                <span />
                                <span>
                                    response = openai.Completion.create(
                                </span>
                                <span>
                                    {' '}
                                    model=
                                    <span className="text-green-300">
                                        &quot;davinci&quot;
                                    </span>
                                    ,
                                </span>
                                <span>
                                    {' '}
                                    prompt=
                                    <span className="text-amber-300">
                                        &quot;&quot;
                                    </span>
                                    ,
                                </span>
                                <span>
                                    {' '}
                                    temperature=
                                    <span className="text-amber-300">0.9</span>,
                                </span>
                                <span>
                                    {' '}
                                    max_tokens=
                                    <span className="text-amber-300">5</span>,
                                </span>
                                <span>
                                    {' '}
                                    top_p=
                                    <span className="text-amber-300">1</span>,
                                </span>
                                <span>
                                    {' '}
                                    frequency_penalty=
                                    <span className="text-amber-300">0</span>,
                                </span>
                                <span>
                                    {' '}
                                    presence_penalty=
                                    <span className="text-green-300">0</span>,
                                </span>
                                <span>)</span>
                            </code>
                        </pre>
                        <Button
                            type="submit"
                            size="sm"
                            className="px-3 absolute top-2 right-2"
                        >
                            <span className="sr-only">Copy</span>
                            <CopyIcon className="h-4 w-4" />
                        </Button>
                    </div>
                </div>
                <div className="grid gap-4">
                    <div className="rounded-md bg-black p-6 relative">
                        <pre>
                            <code className="grid gap-1 text-sm text-muted-foreground [&_span]:h-4 text-left">
                                <span>
                                    <span className="text-sky-300">import</span>{' '}
                                    os
                                </span>
                                <span>
                                    <span className="text-sky-300">import</span>{' '}
                                    openai
                                </span>
                                <span />
                                <span>
                                    openai.api_key = os.getenv(
                                    <span className="text-green-300">
                                        &quot;OPENAI_API_KEY&quot;
                                    </span>
                                    )
                                </span>
                                <span />
                                <span>
                                    response = openai.Completion.create(
                                </span>
                                <span>
                                    {' '}
                                    model=
                                    <span className="text-green-300">
                                        &quot;davinci&quot;
                                    </span>
                                    ,
                                </span>
                                <span>
                                    {' '}
                                    prompt=
                                    <span className="text-amber-300">
                                        &quot;&quot;
                                    </span>
                                    ,
                                </span>
                                <span>
                                    {' '}
                                    temperature=
                                    <span className="text-amber-300">0.9</span>,
                                </span>
                                <span>
                                    {' '}
                                    max_tokens=
                                    <span className="text-amber-300">5</span>,
                                </span>
                                <span>
                                    {' '}
                                    top_p=
                                    <span className="text-amber-300">1</span>,
                                </span>
                                <span>
                                    {' '}
                                    frequency_penalty=
                                    <span className="text-amber-300">0</span>,
                                </span>
                                <span>
                                    {' '}
                                    presence_penalty=
                                    <span className="text-green-300">0</span>,
                                </span>
                                <span>)</span>
                            </code>
                        </pre>
                        <Button
                            type="submit"
                            size="sm"
                            className="px-3 absolute top-2 right-2"
                        >
                            <span className="sr-only">Copy</span>
                            <CopyIcon className="h-4 w-4" />
                        </Button>
                    </div>
                </div>
            </div>
            <Link to="#" target="_blank">
                <Button className="m-8">API Document</Button>
            </Link>
        </section>
    )
}

export default UseApi
