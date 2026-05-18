package com.example.registrodeocupacion.domain.ocupacion

import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import com.example.registrodeocupacion.domain.ocupacion.usecase.GetOcupacionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetOcupacionUseCaseTest {

    private lateinit var useCase: GetOcupacionUseCase
    private lateinit var repository: OcupacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetOcupacionUseCase(repository)
    }

    @Test
    fun `invoke_llamaAlRepositorioYRetornaLaOcupacionCorrespondienteAlId`() = runTest {
        val ocupacionId = 1
        val ocupacionEsperada =
            Ocupacion(ocupacioneId = ocupacionId, descricion = "Medico", sueldo = 80000.0)

        coEvery { repository.getOcupacion(ocupacionId) } returns ocupacionEsperada

        val result = useCase(ocupacionId)

        assertEquals(ocupacionEsperada, result)
        coVerify(exactly = 1) { repository.getOcupacion(ocupacionId) }
    }

    @Test
    fun `invoke_retornaNullSiElIdNoExisteEnElRepositorio`() = runTest {
        val idInexistente = 99

        coEvery { repository.getOcupacion(idInexistente) } returns null

        val result = useCase(idInexistente)

        assertNull(result)
        coVerify(exactly = 1) { repository.getOcupacion(idInexistente) }
    }
}