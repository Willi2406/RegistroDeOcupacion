package com.example.registrodeocupacion.data.ocupacion

import com.example.registrodeocupacion.data.ocupacion.local.OcupacionDao
import com.example.registrodeocupacion.data.ocupacion.local.OcupacioneEntity
import com.example.registrodeocupacion.data.ocupacion.repository.OcupacionRepositoryImpl
import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OcupacionRepositoryImplTest {
    private lateinit var dao: OcupacionDao
    private lateinit var repository: OcupacionRepositoryImpl

    @Before
    fun setUp(){
        dao = mockk(relaxed = true)
        repository = OcupacionRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda ocupacion correctamente`() = runTest {
        val ocupacion = Ocupacion(
            ocupacioneId = 0,
            descricion = "Nueva ocupacion",
            sueldo = 30.0
        )

        val ocupacionSlot = slot<OcupacioneEntity>()
        coEvery { dao.upsert(capture(ocupacionSlot)) } just Runs

        val result = repository.upsert(ocupacion)

        TestCase.assertEquals(0, result)
        coVerify { dao.upsert(any()) }
        TestCase.assertEquals("Nueva ocupacion", ocupacionSlot.captured.descricion)
        TestCase.assertEquals(30.0, ocupacionSlot.captured.sueldo)
    }
    @Test
    fun `upsert actualiza guarda ocupacion correctamente`() = runTest {
        val ocupacion =
            Ocupacion(ocupacioneId = 1, descricion = "Ocupacion actualizada", sueldo = 45.0)
        coEvery { dao.upsert(any()) } just Runs

        val result = repository.upsert(ocupacion)

        TestCase.assertEquals(1, result)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun `delete elimina ocupacion correctamente`() = runTest {
        val ocupacionId = 1
        coEvery { dao.deleteById(ocupacionId) } just Runs

        repository.delete(ocupacionId)

        coVerify { dao.deleteById(ocupacionId) }
    }

    @Test
    fun `observeOcupaciones retorna flow de ocupaciones`() = runTest {
        val entities = listOf(
            OcupacioneEntity(1, "Ocupacion 1", 30.0),
            OcupacioneEntity(2, "Ocupacion 2", 45.0)
        )
        every { dao.observeAll() } returns flowOf(entities)

        val result = repository.observeOcupaciones().first()

        TestCase.assertEquals(2, result.size)
        TestCase.assertEquals("Ocupacion 1", result[0].descricion)
        TestCase.assertEquals("Ocupacion 2", result[1].descricion)
    }

    @Test
    fun `buscar retorna ocupacion correctamente`() = runTest {
        val entity = OcupacioneEntity(1, "Ocupacion Test", 30.0)
        coEvery { dao.getById(1) } returns entity

        val result = repository.getOcupacion(1)

        TestCase.assertNotNull(result)
        TestCase.assertEquals("Ocupacion Test", result?.descricion)
        TestCase.assertEquals(30.0, result?.sueldo)
    }
}