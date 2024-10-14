package ai.youmatch.critter.endpoints

import ai.youmatch.critter.log
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<String> {
        ex.log.error(ex.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<String> {
        ex.log.error(ex.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException): ResponseEntity<String> {
        ex.log.error(ex.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }
}