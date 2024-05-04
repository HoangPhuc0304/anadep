'use client'
import { useToast } from '../component/ui/use-toast'
import { getAccessToken, getAuthUser, saveUser } from '../api/apiCall'
import * as React from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { ToastAction } from '../component/ui/toast'
import {
    ERROR_LABEL,
    FAILED_AUTHORIZATION,
    SUCCESS_AUTHORIZATION,
    SUCCESS_LABEL,
} from '../common/common'
import { User } from '../model/library'
import { useDispatch } from 'react-redux'
import { update } from '../redux/slice/userSlice'

export function AuthCallbackPage() {
    const [searchParams, setSearchParams] = useSearchParams()
    const code = searchParams.get('code')
    const { toast } = useToast()
    const dispatch = useDispatch()
    const navigate = useNavigate()

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const fetchData = async (code: string) => {
        const tokenResponse = await getAccessToken(code)
        if (typeof tokenResponse === 'string') {
            handlingToastAction(
                ERROR_LABEL,
                tokenResponse || FAILED_AUTHORIZATION
            )
        } else {
            const authUser = await getAuthUser(tokenResponse.accessToken)
            if (typeof authUser === 'string') {
                handlingToastAction(
                    ERROR_LABEL,
                    authUser || FAILED_AUTHORIZATION
                )
                return
            }
            const userRequest: User = {
                githubUserId: authUser.id,
                login: authUser.login,
                name: authUser.name,
                avatarUrl: authUser.avatar_url,
                githubUrl: authUser.html_url,
                email: authUser.email,
            }
            const savedUser = await saveUser(
                userRequest,
                tokenResponse.accessToken,
                tokenResponse.refreshToken
            )
            if (typeof savedUser === 'string') {
                handlingToastAction(
                    ERROR_LABEL,
                    tokenResponse || FAILED_AUTHORIZATION
                )
                return
            }
            const user: User = savedUser
            user.githubToken = tokenResponse.accessToken
            dispatch(update(user))
            handlingToastAction(SUCCESS_LABEL, SUCCESS_AUTHORIZATION)
            navigate('/project')
        }
    }

    React.useEffect(() => {
        code && fetchData(code)
    }, [code])

    return <div>Authorizing ...</div>
}
