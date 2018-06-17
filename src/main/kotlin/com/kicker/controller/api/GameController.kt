package com.kicker.controller.api

import com.kicker.annotation.CurrentPlayer
import com.kicker.domain.PageRequest
import com.kicker.domain.model.game.GameDto
import com.kicker.domain.model.game.GamePageResponse
import com.kicker.domain.model.game.GameRegistrationRequest
import com.kicker.domain.model.player.PlayerDto
import com.kicker.model.Player
import com.kicker.service.GameService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @author Yauheni Efimenko
 */
@RestController
@RequestMapping("/api/games")
class GameController(
        private val service: GameService
) {

    @GetMapping("/{gameId}")
    fun get(@PathVariable gameId: Long): GameDto = GameDto(service.get(gameId))

    @GetMapping
    fun getAll(@Valid pageRequest: PageRequest): GamePageResponse {
        val games = service.getAll(pageRequest)
        val players = mutableSetOf<Player>()
        games.forEach {
            players.addAll(setOf(it.redPlayer1, it.redPlayer2, it.yellowPlayer1, it.yellowPlayer2, it.reportedBy))
        }
        return GamePageResponse(games.map { GameDto(it) }, players.map { PlayerDto(it) })
    }

    @PostMapping
    fun gameRegistration(@CurrentPlayer currentPlayer: Player,
                         @Valid @RequestBody request: GameRegistrationRequest): GameDto =
            GameDto(service.gameRegistration(currentPlayer, request))

}