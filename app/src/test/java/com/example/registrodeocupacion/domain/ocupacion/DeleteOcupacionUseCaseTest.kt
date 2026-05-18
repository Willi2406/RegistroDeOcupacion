package com.example.registrodeocupacion.domain.ocupacion

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
class DeleteOcupacionUseCaseTest {

    private lateinit var useCase: DeleteOcupacionUseCase
    private lateinit var repository: OcupacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteOcupacionUseCase(repository)
    }

    @Test
    fun `invoke_llamaAlRepositorioParaEliminarLaOcupacionConElIdProporcionado`() = runTest {
        val ocupacionId = 5
        coEvery { repository.delete(any()) } just Runs

        useCase(ocupacionId)

        coVerify(exactly = 1) { repository.delete(ocupacionId) }
    }
}