package com.example.opticyou.communications

import com.example.opticyou.data.LoginResponse
import com.example.opticyou.data.User
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Object with functions for calling the server
 */

object ServerRequests {

    // A mutex is used to ensure that reads and writes are thread-safe.
    private val accessMutex = Mutex()

    //Validació d'usuari i contrasenya sense servidor
//    suspend fun login(serverName: String, port: Int, username: String, password: String): LoginResponse? = accessMutex.withLock {
//        // Simulem que si la contrasenya és "1234", el login és exitós.
//        // Si el nom d'usuari és "admin", retornem el rol "admin", en cas contrari "user".
//        return if (password == "1234") {
//            val role = if (username == "admin") "admin" else "user"
//            LoginResponse(success = true, role = role)
//        } else {
//            // Si la contrasenya no és "1234", el login falla.
//            LoginResponse(success = false, role = "")
//        }
//    }

    //Validació login amb servidor
    suspend fun login(serverName: String, port: Int, username: String, password: String): LoginResponse? = accessMutex.withLock {
        // Configura la connexió amb el servidor
        CommController.setServerName(serverName)
        CommController.setPort(port)

        // Crida la funció de login i obté un codi de retorn
        val result = CommController.doLogin(username, password)

        return if (result == CommController.OK_RETURN_CODE) {
            // Obtenim el rol de l'usuari a partir del seu nom, segons la teva lògica de negoci
            val role = CommController.getUserRole(username)
            LoginResponse(success = true, role = role)
        } else {
            // Si el login falla, retorna un LoginResponse amb success = false;
            // aquí pots optar per assignar un rol per defecte, o simplement deixar-lo buit.
            LoginResponse(success = false, role = "")
        }
    }

    suspend fun logout():Boolean = accessMutex.withLock {
        return (CommController.doLogout()==CommController.OK_RETURN_CODE)
    }

    suspend fun isLogged():Boolean = accessMutex.withLock {
        return CommController.isLogged
    }

    suspend fun queryUser(username:String): User? = accessMutex.withLock {
          return CommController.doQueryUser(username)
    }

    suspend fun listUsers():Array<User>? = accessMutex.withLock {
        return CommController.doListUsers()
    }

    suspend fun addUser(user:User):Boolean = accessMutex.withLock {
        return (CommController.doAddUser(user)==CommController.OK_RETURN_CODE)

    }
}

