package com.example.opticyou.ui

import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.LoginUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Holds the method to log in to the server and fills the operation result, which can be retrieved from the screen.
 * Also, it fills if the operation has started to aid screen representation.
 */
class LoginViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : IOViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Connexió interna amb el servidor
    private val SERVER_NAME = "192.168.1.100"  // IP o URL fixa
    private val SERVER_PORT = 8083             // Port fix

    fun setLoginTried(tried: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                loginTried = tried
            )
        }
    }

    /**
     * Logs in the server
     */
    fun doLogin(username: String, password: String, onSuccess: (LoginResponse) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val response = try {
                ServerRequests.login(SERVER_NAME, SERVER_PORT, username, password)
            } catch (e: Exception) {
                null
            }
            _uiState.update { it.copy(loginTried = true) }
            withContext(mainDispatcher) {
                // Si response és null, creem un LoginResponse fallit per defecte.
                val finalResponse = response ?: LoginResponse(success = false, role = "")
                // Cridem el callback sempre.
                onSuccess(finalResponse)
                setGoodResult(finalResponse.success)
            }
        }
    }
}
