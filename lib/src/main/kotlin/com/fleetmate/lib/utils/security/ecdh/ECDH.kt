package com.fleetmate.lib.utils.security.ecdh

import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction

object ECDH : BaseIntIdTable("ecdh_keys") {
    val gentime = timestamp("gentime")
    val public_x = text("public_x")
    val public_y = text("public_y")
    val private_key = text("private_key")

    fun retrievePublics(): PointDto = transaction {
        val point = PointDto()
        exec("select x, y from get_ecdh_public();") {
            while (it.next()) {
                point.x = it.getString("x")
                point.y = it.getString("y")
            }
        }
        point
    }

    fun getSymmetricalKey(publics: PointDto): PointDto = transaction {
        val point = PointDto()
        //TODO: Protect from SQL injection
        exec("select x, y from get_symmetrical_key('${publics.x}', '${publics.y}');")  {
            while (it.next()) {
                point.x = it.getString("x")
                point.y = it.getString("y")
            }
        }
        point
    }
}