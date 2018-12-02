package com.kicker.api.service

import com.kicker.api.config.property.PlayerSettingsProperties
import com.kicker.api.domain.PageRequest
import com.kicker.api.domain.model.game.GameRegistrationRequest
import com.kicker.api.model.Game
import com.kicker.api.repository.GameRepository
import com.kicker.api.utils.DateUtils
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate.now
import java.util.*

/**
 * @author Yauheni Efimenko
 */
@Service
@Transactional(readOnly = true)
class DefaultGameService(
        private val repository: GameRepository,
        private val playerService: PlayerService,
        private val eventPublisher: ApplicationEventPublisher,
        private val playerSettingsProperties: PlayerSettingsProperties
) : DefaultBaseService<Game, GameRepository>(repository), GameService {

    @Cacheable("games")
    override fun getAll(pageRequest: PageRequest): Page<Game> {
        return super.getAll(pageRequest)
    }

    /*
        * Current week is number 0, so 10 week is number 9
        * */
    override fun countPerWeekDuring10WeeksByPlayer(playerId: Long): List<Long> {
        val player = playerService.get(playerId)
        val dashboard = mutableListOf<Long>()
        for (weeksAgo in 9 downTo 0) {
            val dates = DateUtils.getIntervalDatesOfWeek(weeksAgo.toLong())
            val count = repository.countByPlayerAndIntervalDates(player, dates.first, dates.second)
            dashboard.add(count)
        }
        return dashboard
    }

    /*
    * Current week is number 0, so 10 week is number 9
    * */
    override fun countDuring10WeeksByPlayer(playerId: Long): Long {
        val player = playerService.get(playerId)
        return repository.countByPlayerAndIntervalDates(player,
                DateUtils.getStartDateOfWeek(playerSettingsProperties.countWeeks!! - 1), now())
    }

    override fun countPerDayDuringLast7Days(): List<Long> {
        val countGamesPerDay = repository.countPerDayByIntervalDates(now().minusWeeks(1), now())
        val countGamesPerDayDuringLast7Days = mutableListOf<Long>()

        /**
         * plusDays(1) is needed because current day also is needed be in last week
         */
        var startDate = now().minusWeeks(1).plusDays(1)
        val endDate = now()
        while (!startDate.isAfter(endDate)) {
            val dto = countGamesPerDay.firstOrNull { it.date == startDate }

            if (null != dto) {
                countGamesPerDayDuringLast7Days.add(dto.count)
            } else {
                countGamesPerDayDuringLast7Days.add(0)
            }

            startDate = startDate.plusDays(1)
        }

        return countGamesPerDayDuringLast7Days
    }

    @CacheEvict(value = ["games", "relations", "playersDashboard", "statsPlayers", "statsActivePlayers", "playerGames",
        "deltaPerWeekDuring10Weeks"], allEntries = true)
    @Transactional
    override fun gameRegistration(playerId: Long, request: GameRegistrationRequest): Game {
        val reporter = playerService.get(playerId)

        val winner1 = playerService.get(request.winner1Id!!)
        val winner2 = playerService.get(request.winner2Id!!)
        val loser1 = playerService.get(request.loser1Id!!)
        val loser2 = playerService.get(request.loser2Id!!)


        val game = if (Objects.nonNull(request.date)) {
            Game(request.losersGoals!!, winner1, winner2, loser1, loser2, reporter, request.date!!.toLocalDateTime())
        } else {
            Game(request.losersGoals!!, winner1, winner2, loser1, loser2, reporter)
        }
        val persistGame = repository.save(game)

        eventPublisher.publishEvent(persistGame)

        return persistGame
    }

}