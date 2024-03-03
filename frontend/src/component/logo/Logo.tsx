import './Logo.css'

const Logo = () => {
    return (
        <div id="logo">
            <img
                className="logo-img"
                src={require('../../resource/assets/logo.png')}
                alt="logo"
                style={{ height: '100%' }}
            />
        </div>
    )
}

export default Logo
