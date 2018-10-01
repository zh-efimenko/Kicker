package com.kicker.service

import com.kicker.domain.PageRequest
import com.kicker.domain.model.game.GameRegistrationRequest
import com.kicker.domain.model.player.CreatePlayerRequest
import com.kicker.domain.model.player.UpdatePlayerPasswordRequest
import com.kicker.domain.model.player.UpdatePlayerUsernameRequest
import com.kicker.model.Award
import com.kicker.model.Game
import com.kicker.model.Player
import com.kicker.model.PlayerStats
import com.kicker.model.base.BaseModel
import org.springframework.data.domain.Page
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * @author Yauheni Efimenko
 */
interface BaseService<T : BaseModel> {

    fun get(id: Long): T

    fun getAll(): List<T>

    fun getAll(pageRequest: PageRequest): Page<T>

    fun save(entity: T): T

}

interface PlayerService : BaseService<Player>, UserDetailsService {

    fun getByUsername(username: String): Player?

    fun getAllActive(pageRequest: PageRequest): Page<Player>

    fun create(request: CreatePlayerRequest): Player

    fun updateUsername(playerId: Long, request: UpdatePlayerUsernameRequest): Player

    fun updatePassword(playerId: Long, request: UpdatePlayerPasswordRequest): Player

    fun updateRating(playerId: Long, newRating: Double): Player

    fun updateActivity(playerId: Long, active: Boolean): Player

}

interface GameService : BaseService<Game> {

    fun gameRegistration(playerId: Long, request: GameRegistrationRequest): Game

    fun getAllBelongGames(playerId: Long, pageRequest: PageRequest): Page<Game>

    fun countGamesByPlayerAndWeek(playerId: Long, weekAgo: Int): Int

}

interface PlayerStatsService : BaseService<PlayerStats> {

    fun getByPlayer(playerId: Long, pageRequest: PageRequest): Page<PlayerStats>

    fun getDeltaByPlayerAndWeek(playerId: Long, weekAgo: Int): Double

}

interface AwardService : BaseService<Award> {

    fun getAllByPlayer(playerId: Long): List<Award>

    fun doAwardMaxRatingForWeek()

    fun doAwardMaxDeltaRatingForWeek()

}