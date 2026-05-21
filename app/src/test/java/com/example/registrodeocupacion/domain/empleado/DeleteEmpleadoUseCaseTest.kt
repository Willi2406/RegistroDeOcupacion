package com.example.registrodeocupacion.domain.empleado

import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import com.example.registrodeocupacion.domain.empleado.usecase.DeleteEmpleadoUseCase
import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import com.example.registrodeocupacion.domain.ocupacion.usecase.DeleteOcupacionUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteEmpleadoUseCaseTest {
    private lateinit var useCase: DeleteEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteEmpleadoUseCase(repository)
    }

    @Test
    fun `invoke_llama Al Repositorio Para Eliminar el empleado Con ElId Proporcionado`() = runTest {
        val empleadoId = 5
        coEvery { repository.delete(any()) } just Runs

        useCase(empleadoId)

        coVerify(exactly = 1) { repository.delete(empleadoId) }
    }
}