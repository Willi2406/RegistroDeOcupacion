package com.example.registrodeocupacion.domain.ocupacion

import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import com.example.registrodeocupacion.domain.ocupacion.usecase.ObserveOcupacionesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.first

@ExperimentalCoroutinesApi
class ListarOcupacionUseCaseTest {
    private lateinit var repository: OcupacionRepository
    private lateinit var useCase: ObserveOcupacionesUseCase

    @Before
    fun setup(){
        repository = mockk()
        useCase = ObserveOcupacionesUseCase(repository)
    }

    @Test
    fun `invoke llama al repositorio y retorna un flujo`() = runTest {
        val  listarEsperada = listOf(
            Ocupacion(ocupacioneId = 1, descricion = "Pelotero", sueldo = 20000.0),
            Ocupacion(ocupacioneId = 2, descricion = "Pelotero", sueldo = 20000.0)
        )
        coEvery { repository.observeOcupaciones() } returns flowOf(listarEsperada)

        val result = useCase().first()

        assertEquals(listarEsperada, result)
        coVerify (exactly = 1) { repository.observeOcupaciones() }
    }
    @Test
    fun `invoke retorna un flujo vacio cuando no hay registros`() = runTest {
        val listaVacia = emptyList<Ocupacion>()
        coEvery { repository.observeOcupaciones() } returns flowOf(listaVacia)

        val result = useCase().first()

        assertEquals(listaVacia, result)
        coVerify(exactly = 1) { repository.observeOcupaciones() }
    }


}