package com.fleetmate.stat.modules.order.service


import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.order.data.dao.WorkTypeDao
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class WorkService(di: DI) : KodeinService(di) {
    fun getAllTypes() = transaction {
        WorkTypeDao.all().map(WorkTypeDao::toOutputDto)
    }
}