package ru.komsomolsk.discountapp

import android.content.Context
import android.content.SharedPreferences

/**
 * Хранит назначенные роли для логинов admin и manager.
 * По умолчанию: admin → ADMIN, manager → MANAGER.
 * Только пользователь с ролью ADMIN может менять роли.
 */
object RoleRepository {

    private const val PREFS_NAME = "role_prefs"
    private const val KEY_ROLE_ADMIN = "role_admin"
    private const val KEY_ROLE_MANAGER = "role_manager"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getStoredRole(context: Context, login: String): UserRole {
        val key = when (login) {
            "admin" -> KEY_ROLE_ADMIN
            "manager" -> KEY_ROLE_MANAGER
            else -> return UserRole.GUEST
        }
        val value = prefs(context).getString(key, null) ?: defaultRoleFor(login)
        return UserRole.valueOf(value)
    }

    private fun defaultRoleFor(login: String): String = when (login) {
        "admin" -> UserRole.ADMIN.name
        "manager" -> UserRole.MANAGER.name
        else -> UserRole.GUEST.name
    }

    /** Меняет роль пользователя. Вызывать только при текущей роли ADMIN. */
    fun setRole(context: Context, login: String, role: UserRole) {
        if (login != "admin" && login != "manager") return
        val key = if (login == "admin") KEY_ROLE_ADMIN else KEY_ROLE_MANAGER
        prefs(context).edit().putString(key, role.name).apply()
    }

    /** Поменять роли местами: admin ↔ manager */
    fun swapRoles(context: Context) {
        val p = prefs(context)
        val adminRole = UserRole.valueOf(p.getString(KEY_ROLE_ADMIN, UserRole.ADMIN.name)!!)
        val managerRole = UserRole.valueOf(p.getString(KEY_ROLE_MANAGER, UserRole.MANAGER.name)!!)
        p.edit()
            .putString(KEY_ROLE_ADMIN, managerRole.name)
            .putString(KEY_ROLE_MANAGER, adminRole.name)
            .apply()
    }
}
