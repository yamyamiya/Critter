package ai.youmatch.critter.repository

import ai.youmatch.critter.entity.Critter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CritterRepository: JpaRepository<Critter, UUID> {
    fun findByName(name: String): Optional<Critter>
    fun deleteByName(name: String)
}