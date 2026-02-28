package ru.komsomolsk.discountapp

/**
 * Хранит текущую роль пользователя после входа.
 */
object SessionManager {
    var currentRole: UserRole = UserRole.GUEST
}
