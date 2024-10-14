package ai.youmatch.critter.service

import ai.youmatch.critter.entity.Critter
import ai.youmatch.critter.entity.Size
import ai.youmatch.critter.repository.CritterRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class CritterService(
        private val critterRepository: CritterRepository
) {
    fun getAll(): List<Critter> {
        return critterRepository.findAll()
    }

    fun getById(id: UUID): Critter? {
        return critterRepository.findById(id).orElseThrow {
            NoSuchElementException("Critter with ID $id not found")
        }
    }

    fun getByName(name: String): Critter? {
        return critterRepository.findByName(name).orElseThrow {
            NoSuchElementException("Critter with name $name not found")
        }
    }

    @Transactional
    fun add(name: String, size: Size, age: Int): UUID {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }
        if (age < 1 || age > 100) {
            throw IllegalArgumentException("Age must be between 1 and 100")
        }
        val existingCritter = critterRepository.findByName(name)
        return if (existingCritter.isPresent) {
            existingCritter.get().id
        } else {
            val newCritter = Critter(name = name, size = size, age = age)
            val savedCritter = critterRepository.save(newCritter)
            savedCritter.id
        }
    }

    @Transactional
    fun updateById(id: UUID, name: String, size: Size, age: Int): Critter {
        val existingCritter = critterRepository.findById(id).orElseThrow {
            NoSuchElementException("Critter with ID $id not found")
        }
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }
        if (age < 1 || age > 100) {
            throw IllegalArgumentException("Age must be between 1 and 100")
        }
        val updatedCritter = existingCritter.copy(
                name = name,
                size = size,
                age = age,
                mood = Critter.calculateMood(existingCritter.powerLevel, age)
        )
        return critterRepository.save(updatedCritter)
    }

    @Transactional
    fun updateByName(name: String, size: Size, age: Int): Critter {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }
        val existingCritter = critterRepository.findByName(name).orElseThrow {
            NoSuchElementException("Critter with name $name not found")
        }
        if (age < 1 || age > 100) {
            throw IllegalArgumentException("Age must be between 1 and 100")
        }
        val updatedCritter = existingCritter.copy(
                size = size,
                age = age,
                mood = Critter.calculateMood(existingCritter.powerLevel, age)
        )
        return critterRepository.save(updatedCritter)
    }

    @Transactional
    fun deleteById(id: UUID): Result<String> {
        if (critterRepository.findById(id).isEmpty) {
            return Result.success("Such user does not exist")
        } else {
            critterRepository.deleteById(id)
            return Result.success("User successfully deleted")
        }
    }

    @Transactional
    fun deleteByName(name: String): Result<String> {
        if (critterRepository.findByName(name).isEmpty) {
            return Result.success("Such user does not exist")
        } else {
            critterRepository.deleteByName(name)
            return Result.success("User successfully deleted")
        }
    }
}