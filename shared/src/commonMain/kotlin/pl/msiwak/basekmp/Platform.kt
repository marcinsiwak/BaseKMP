package pl.msiwak.basekmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform