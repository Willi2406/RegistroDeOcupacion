package com.example.registrodeocupacion.domain.empleado

import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import com.example.registrodeocupacion.domain.empleado.usecase.ObserveEmpleadoUseCase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class ListarEmpleadoUseCaseTest {
        private lateinit var repository: EmpleadoRepository
        private lateinit var useCase: ObserveEmpleadoUseCase

        @Before
        fun setup(){
            repository = mockk()
                useCase = ObserveEmpleadoUseCase(repository)
        }

        @Test
        fun `invoke llama al repositorio y retorna un flujo`() = runTest {
            val  listarEsperada = listOf(
                Empleado(
                    empleadoId = 0,
                    fechaIngreso = LocalDate.now(),
                    nombres = "Juli",
                    sexo = "Femenino",
                    sueldo = 1000000.0
                ),
                Empleado(
                    empleadoId = 1,
                    fechaIngreso = LocalDate.now(),
                    nombres = "Carlos",
                    sexo = "Masculino",
                    sueldo = 2000000.0
                )
            )
            coEvery { repository.observeEmpleados()} returns flowOf(listarEsperada)

            val result = useCase().first()

            assertEquals(listarEsperada, result)
            coVerify (exactly = 1) { repository.observeEmpleados()}
        }
        @Test
        fun `invoke retorna un flujo vacio cuando no hay registros`() = runTest {
            val listaVacia = emptyList<Empleado>()
            coEvery { repository.observeEmpleados()} returns flowOf(listaVacia)

            val result = useCase().first()

            assertEquals(listaVacia, result)
            coVerify(exactly = 1) { repository.observeEmpleados()}
        }

}