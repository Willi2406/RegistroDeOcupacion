package com.example.registrodeocupacion.domain.empleado

import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import com.example.registrodeocupacion.domain.empleado.usecase.UpsertEmpleadoUseCase
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
import java.time.LocalDate

@ExperimentalCoroutinesApi
class UpsertEmpleadoUseCaseTest {
    private lateinit var useCase: UpsertEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup(){
        repository = mockk()
        useCase = UpsertEmpleadoUseCase(repository)
    }

    @Test
    fun `invoke guarda empleado con datos validos`() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = LocalDate.now(),
            nombres = "Juan Perez",
            sexo = "Masculino",
            sueldo = 25000.0
        )
        coEvery { repository.observeEmpleados() } returns flowOf(emptyList())
        coEvery { repository.upsert(empleado) } returns 1

        val result = useCase(empleado)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify { repository.upsert(empleado) }
    }

    @Test
    fun `invoke falla con nombres vacios`() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = LocalDate.now(),
            nombres = "",
            sexo = "Masculino",
            sueldo = 25000.0
        )
        coEvery { repository.observeEmpleados() } returns flowOf(emptyList())

        val result = useCase(empleado)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con nombres muy cortos`() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = LocalDate.now(),
            nombres = "J",
            sexo = "Masculino",
            sueldo = 25000.0
        )
        coEvery { repository.observeEmpleados() } returns flowOf(emptyList())

        val result = useCase(empleado)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con sueldo invalido`() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = LocalDate.now(),
            nombres = "Maria Lopez",
            sexo = "Femenino",
            sueldo = -5000.0
        )
        coEvery { repository.observeEmpleados() } returns flowOf(emptyList())

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con sexo vacio`() = runTest {
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now(), nombres = "Alfredo", "", 30000.0)

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con fecha posterior a la actual`() = runTest {
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now().plusYears(1), nombres = "Alfredo", "Masculino", 30000.0)

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
}