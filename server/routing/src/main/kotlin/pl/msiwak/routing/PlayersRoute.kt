package pl.msiwak.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import pl.msiwak.domain.player.GetPlayersUseCase

fun Route.addPlayersRoute() {
    val getPlayersUseCase: GetPlayersUseCase by inject()

    get("/players") {
        with(call) {
            val players = getPlayersUseCase()
            respond(status = HttpStatusCode.OK, message = players)
        }
    }
}
