package com.example.registrodeocupacion.domain.ocupacion

import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import com.example.registrodeocupacion.domain.ocupacion.usecase.UpsertOcupacionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpsertOcupacionUseCaseTest {
    private lateinit var useCase: UpsertOcupacionUseCase
    private lateinit var repository: OcupacionRepository

    @Before
    fun setup(){
        repository = mockk()
        useCase = UpsertOcupacionUseCase(repository)
    }

    @Test
    fun `invoke guarda ocupacion con datos validos`() = runTest {
        val ocupacion = Ocupacion(ocupacioneId = 0, descricion = "Ingeniero", sueldo = 25000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())
        coEvery { repository.upsert(ocupacion) } returns 1

        val result = useCase(ocupacion)

        assertTrue(result.isSuccess)
        assertEquals(1,result.getOrNull() )
        coVerify { repository.upsert(ocupacion) }
    }

    @Test
    fun `invoke falla con descripcion vacia`() = runTest {
        val ocupacion = Ocupacion(ocupacioneId = 0, descricion = "", sueldo = 25000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con descripcion muy corta`() = runTest {
        val ocupacion = Ocupacion(ocupacioneId = 0, descricion = "ab", sueldo = 25000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)

    }
    @Test
    fun `invoke falla cuando la ocupacion ya esta registrada`() = runTest {
        val ocupacionesExistentes = listOf(Ocupacion(ocupacioneId = 1, descricion = "Ingeniero", sueldo = 25000.0))
        coEvery { repository.observeOcupaciones() } returns flowOf(ocupacionesExistentes)

        val ocupacion = Ocupacion(ocupacioneId = 0, descricion = "Ingeniero", sueldo = 30000.0)

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con sueldo invalido`() = runTest {
        val ocupacion = Ocupacion(ocupacioneId = 0, descricion = "Test Ocupacion", sueldo = -5000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

}