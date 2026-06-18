package br.com.lucolimac.xuxubank.ui.viewmodel

import br.com.lucolimac.xuxubank.data.local.entity.ClientEntity
import br.com.lucolimac.xuxubank.domain.repository.ClientRepository
import br.com.lucolimac.xuxubank.domain.usecase.ManageClientUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ClientViewModel following the 'testing-setup' skill.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ClientViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeClientRepository = object : ClientRepository {
        val clientsFlow = MutableStateFlow<List<ClientEntity>>(emptyList())
        override fun getAllClients(): Flow<List<ClientEntity>> = clientsFlow
        override suspend fun getClientById(id: Long): ClientEntity? = clientsFlow.value.find { it.id == id }
        override suspend fun getClientByIdentifier(identifier: String): ClientEntity? {
            return clientsFlow.value.find { it.email == identifier || it.phone == identifier }
        }
        override suspend fun saveClient(client: ClientEntity): Long {
            clientsFlow.value += client
            return client.id
        }
        override suspend fun updateClient(client: ClientEntity) {
            clientsFlow.value = clientsFlow.value.map { if (it.id == client.id) client else it }
        }
        override suspend fun deleteClient(client: ClientEntity) {
            clientsFlow.value -= client
        }
    }

    private val manageClientUseCase = ManageClientUseCase(fakeClientRepository)
    private lateinit var viewModel: ClientViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ClientViewModel(manageClientUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `allClients should reflect data from repository`() = runTest {
        // Given
        val client = ClientEntity(id = 1, name = "John Doe", email = "john@doe.com", phone = "11999999999")
        
        // When
        fakeClientRepository.clientsFlow.value = listOf(client)

        // Then - collection should happen immediately with UnconfinedTestDispatcher
        val actual = viewModel.allClients.first()
        assertEquals(1, actual.size)
        assertEquals("John Doe", actual[0].name)
    }
}
