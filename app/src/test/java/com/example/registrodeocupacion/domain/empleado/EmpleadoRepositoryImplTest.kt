package com.example.registrodeocupacion.domain.empleado

import androidx.compose.ui.platform.LocalContext
import com.example.registrodeocupacion.data.empleado.local.EmpleadoDao
import com.example.registrodeocupacion.data.empleado.local.EmpleadoEntity
import com.example.registrodeocupacion.data.empleado.repository.EmpleadoRepositoryImpl
import com.example.registrodeocupacion.domain.empleado.model.Empleado
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import java.time.LocalDate
import org.junit.Before
import org.junit.Test

class EmpleadoRepositoryImplTest {
    private lateinit var dao: EmpleadoDao
    private lateinit var repository: EmpleadoRepositoryImpl

    @Before
    fun setUp(){
        dao = mockk(relaxed = true)
        repository = EmpleadoRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda empleado correctamente`() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = LocalDate.now(),
            nombres = "Juli",
            sexo = "Femenino",
            sueldo = 1000000.0
        )

        val empleadoSlot = slot<EmpleadoEntity>()
        coEvery { dao.upsert(capture(empleadoSlot)) } just Runs

        val result = repository.upsert(empleado)

        TestCase.assertEquals(0, result)
        coVerify { dao.upsert(any()) }
        TestCase.assertEquals(LocalDate.now(), empleadoSlot.captured.fechaIngreso)
        TestCase.assertEquals("Juli", empleadoSlot.captured.sexo)
        TestCase.assertEquals(1000000.0, empleadoSlot.captured.sueldo)

    }

    @Test
    fun `upsert actualiza guarda ocupacion correctamente`() = runTest {
        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = LocalDate.now().minusYears(1),
            nombres = "Mateo",
            sexo = "Masculino",
            sueldo = 6500.0
        )
        coEvery { dao.upsert(any()) } just Runs

        val result = repository.upsert(empleado)

        TestCase.assertEquals(1, result)
        coEvery { dao.upsert(any()) }
    }
    @Test
    fun `delete elimina ocupacion correctamente`() = runTest {
        val empleadoId = 1
        coEvery { dao.deleteById((empleadoId)) } just Runs
        repository.delete(empleadoId)
        coVerify { dao.deleteById(empleadoId) }
    }
    @Test
    fun `observeEmpleados retorna flow de ocupaciones`() = runTest {
        val entities = listOf(
            EmpleadoEntity(1, LocalDate.now().minusYears(1), "Jose", "Masculino",10000.0),
            EmpleadoEntity(2, LocalDate.now().minusYears(2), "Josefa", "Femenino", 8000.0),
        )
        every { dao.observeAll() } returns flowOf(entities)

        val result = repository.observeEmpleados().first()

        TestCase.assertEquals(2, result.size)
        TestCase.assertEquals("Jose", result[0].nombres)
        TestCase.assertEquals("Josefa", result[1].nombres)
    }

    @Test
    fun `buscar retorna ocupacion correctamente`() = runTest {
        val entity = EmpleadoEntity(1, LocalDate.now(), "Juan", "Masculino", 10000.0)
        coEvery { dao.getById(1) } returns entity

        val result = repository.getEmpleado(1)

        TestCase.assertNotNull(result)
        TestCase.assertEquals(LocalDate.now(), result?.fechaIngreso)
        TestCase.assertEquals("Jose", result?.nombres)
        TestCase.assertEquals("Masculino", result?.sexo)
        TestCase.assertEquals(10000.0, result?.sueldo)
    }



}