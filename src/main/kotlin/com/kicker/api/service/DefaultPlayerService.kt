package com.kicker.api.service

import com.kicker.api.component.IconManager
import com.kicker.api.domain.PageRequest
import com.kicker.api.domain.model.player.CreatePlayerRequest
import com.kicker.api.domain.model.player.UpdatePlayerPasswordRequest
import com.kicker.api.domain.model.player.UpdatePlayerUsernameRequest
import com.kicker.api.exception.service.DuplicateUsernameException
import com.kicker.api.exception.service.NotFoundPlayerException
import com.kicker.api.exception.service.PasswordIncorrectException
import com.kicker.api.model.Player
import com.kicker.api.repository.PlayerRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * @author Yauheni Efimenko
 */
@Service
@Transactional(readOnly = true)
class DefaultPlayerService(
        private val repository: PlayerRepository,
        private val passwordEncoder: PasswordEncoder,
        private val iconManager: IconManager
) : DefaultBaseService<Player, PlayerRepository>(repository), PlayerService {

    override fun get(id: Long): Player {
        return try {
            super.get(id)
        } catch (e: NoSuchElementException) {
            throw NotFoundPlayerException("Player with such id: $id not found")
        }
    }

    override fun getByUsername(username: String): Player? = repository.findByUsername(username)

    @Cacheable("players")
    override fun getAll(): List<Player> = super.getAll()

    @Cacheable("players")
    override fun getAll(pageRequest: PageRequest): Page<Player> = super.getAll(pageRequest)

    @Cacheable("activePlayers")
    override fun getAllActive(pageRequest: PageRequest): Page<Player> = repository.findAllByActiveTrue(pageRequest)

    override fun loadUserByUsername(username: String): UserDetails = getByUsername(username)
            ?: throw UsernameNotFoundException("User with username $username not found")

    @CacheEvict("players", allEntries = true)
    @Transactional
    override fun create(request: CreatePlayerRequest): Player {
        if (isExist(request.username!!)) {
            throw DuplicateUsernameException("The player with such username already exist")
        }

        request.password = passwordEncoder.encode(request.password)

        return super.save(Player(request.username!!, request.password!!))
    }

    @Transactional
    override fun updateUsername(playerId: Long, request: UpdatePlayerUsernameRequest): Player {
        if (isExist(request.username!!)) {
            throw DuplicateUsernameException("The player with such username already exist")
        }

        val player = get(playerId)
        player.username = request.username!!

        return super.save(player)
    }

    @Transactional
    override fun updatePassword(playerId: Long, request: UpdatePlayerPasswordRequest): Player {
        val player = get(playerId)

        if (!passwordEncoder.matches(request.currentPassword, player.password)) {
            throw PasswordIncorrectException("Password is incorrect")
        }

        player.password = passwordEncoder.encode(request.newPassword)

        return super.save(player)
    }

    @Transactional
    override fun updateRating(playerId: Long, newRating: Double): Player {
        val player = get(playerId)
        player.rating = newRating

        return super.save(player)
    }

    @CacheEvict("activePlayers", allEntries = true)
    @Transactional
    override fun updateActivity(playerId: Long, active: Boolean): Player {
        val player = get(playerId)
        player.active = active

        return super.save(player)
    }

    @Transactional
    override fun updateIcon(playerId: Long, icon: MultipartFile): Player {
        val player = get(playerId)
        val iconName = UUID.randomUUID().toString() + icon.originalFilename!!

        player.iconName?.let { iconManager.remove(it) }
        iconManager.save(iconName, icon.bytes)

        player.iconName = iconName
        return super.save(player)
    }

    @Transactional
    override fun updateWinAndLossStreak(playerId: Long, won: Boolean): Player {
        val player = get(playerId)

        player.changeWinAndLossStreak(won)

        return super.save(player)
    }

    private fun isExist(username: String): Boolean {
        return getByUsername(username)?.let { true } ?: false
    }

}