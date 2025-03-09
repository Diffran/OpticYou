package com.example.opticyou.ui

import androidx.lifecycle.viewModelScope
import com.example.opticyou.communications.ServerRequests
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.LoginUiState
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
class LoginViewModel : IOViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Connexió interna amb el servidor
    private val SERVER_NAME = "192.168.1.100"  // IP o URL fixa
    private val SERVER_PORT = 8080             // Port fix

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
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                ServerRequests.login(SERVER_NAME, SERVER_PORT, username, password)
            } catch (e: Exception) {
                null
            }
            // Actualitzem loginTried (si fos necessari, també podríem fer-ho al Main)
            _uiState.update { it.copy(loginTried = true) }

            // Ara canvia al fil principal per fer la navegació i altres actualitzacions de la UI
            withContext(Dispatchers.Main) {
                if (response?.success == true) {
                    onSuccess(response)
                    setGoodResult(true)
                } else {
                    setGoodResult(false)
                }
            }
        }
    }
}
