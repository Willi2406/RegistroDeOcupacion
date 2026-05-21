package com.example.registrodeocupacion.domain.empleado

import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import com.example.registrodeocupacion.domain.empleado.usecase.GetEmpleadoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class GetEmpleadoUseCaseTest {
        private lateinit var useCase: GetEmpleadoUseCase
        private lateinit var repository: EmpleadoRepository

        @Before
        fun setup() {
            repository = mockk()
            useCase = GetEmpleadoUseCase(repository)
        }

        @Test
        fun `invoke_llamaAlRepositorioYRetornaElEmpleadoCorrespondienteAlId`() = runTest {
            val empleadoId = 1
            val empleadoEsperado =
                Empleado(
                    empleadoId = 0,
                    fechaIngreso = LocalDate.now(),
                    nombres = "Juli",
                    sexo = "Femenino",
                    sueldo = 1000000.0
                )

            coEvery { repository.getEmpleado(empleadoId) } returns empleadoEsperado

            val result = useCase(empleadoId)

            assertEquals(empleadoEsperado, result)
            coVerify(exactly = 1) { repository.getEmpleado(empleadoId) }
        }

        @Test
        fun `invoke_retornaNullSiElIdNoExisteEnElRepositorio`() = runTest {
            val idInexistente = 99

            coEvery { repository.getEmpleado(idInexistente) } returns null

            val result = useCase(idInexistente)

            assertNull(result)
            coVerify(exactly = 1) { repository.getEmpleado(idInexistente) }
        }

}