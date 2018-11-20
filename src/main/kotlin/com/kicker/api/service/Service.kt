package com.kicker.api.service

import com.kicker.api.domain.PageRequest
import com.kicker.api.domain.model.game.GameRegistrationRequest
import com.kicker.api.domain.model.player.CreatePlayerRequest
import com.kicker.api.domain.model.player.UpdatePlayerPasswordRequest
import com.kicker.api.domain.model.player.UpdatePlayerUsernameRequest
import com.kicker.api.domain.model.playerStats.PlayerStatsDto
import com.kicker.api.domain.repository.PlayerDeltaDto
import com.kicker.api.model.Game
import com.kicker.api.model.Player
import com.kicker.api.model.PlayerRelations
import com.kicker.api.model.PlayerStats
import com.kicker.api.model.base.BaseModel
import org.springframework.data.domain.Page
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.multipart.MultipartFile

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

    fun updateIcon(playerId: Long, icon: MultipartFile): Player

    fun updateWinAndLossStreak(playerId: Long, won: Boolean): Player

}

interface GameService : BaseService<Game> {

    fun gameRegistration(playerId: Long, request: GameRegistrationRequest): Game

    fun getAllByPlayer(playerId: Long, pageRequest: PageRequest): Page<Game>

    fun countByPlayer(playerId: Long): Long

    fun countPerWeekDuring10WeeksByPlayer(playerId: Long): List<Long>

    fun countByPlayerAndWeeksAgo(playerId: Long, weeksAgo: Long): Long

    fun countDuring10WeeksByPlayer(playerId: Long): Long

    fun countPerDayDuringLast7Days(): List<Long>

}

interface PlayerStatsService : BaseService<PlayerStats> {

    fun getStatsByPlayer(playerId: Long): PlayerStatsDto

    fun getGamesStatsByPlayer(playerId: Long, pageRequest: PageRequest): Page<PlayerStats>

    fun getDeltaPerWeekDuring10WeeksByPlayer(playerId: Long): List<Double>

    fun getDeltaByPlayerAndWeeksAgo(playerId: Long, weeksAgo: Long): Double

    fun getDeltaPlayersDuringLastWeek(): List<PlayerDeltaDto>

    fun getActualRatingByPlayer(playerId: Long): Double

    fun countLossesByPlayer(playerId: Long): Long

    fun countWinsByPlayer(playerId: Long): Long

    fun countGoalsAgainstByPlayer(playerId: Long): Long

    fun countGoalsForByPlayer(playerId: Long): Long

}

interface PlayerRelationsService {

    fun getAllByPlayer(playerId: Long, pageRequest: PageRequest): Page<PlayerRelations>

    fun getByPlayerAndPartner(playerId: Long, partnerId: Long): PlayerRelations

    fun create(playerRelations: PlayerRelations): PlayerRelations

}