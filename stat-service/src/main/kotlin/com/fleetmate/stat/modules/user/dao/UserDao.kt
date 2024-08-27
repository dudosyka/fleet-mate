package com.fleetmate.stat.modules.user.dao


import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.FieldFilterWrapper
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.order.data.dao.OrderDao
import com.fleetmate.stat.modules.order.data.dao.WashDao
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.user.dto.UserDto
import com.fleetmate.stat.modules.user.dto.UserSimpleDto
import com.fleetmate.stat.modules.user.dto.output.DriverDto
import com.fleetmate.stat.modules.user.dto.output.DriverOutputDto
import com.fleetmate.stat.modules.user.dto.output.StaffDto
import com.fleetmate.stat.modules.user.dto.output.StaffOutputDto
import com.fleetmate.stat.modules.user.model.UserHoursModel
import com.fleetmate.stat.modules.user.model.UserPhotoModel
import com.fleetmate.stat.modules.violation.dao.ViolationDao
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or

class UserDao(id: EntityID<Int>) : BaseIntEntity<UserDto>(id, UserModel) {
    companion object : BaseIntEntityClass<UserDto, UserDao>(UserModel) {
        fun mechanicOrders(
            idValue: Int,
            dateRange: FieldFilterWrapper<Long>?,
        ): SizedIterable<OrderDao> =
            OrderDao.find {
                (rangeCond(dateRange, OrderModel.id neq 0, OrderModel.startedAt, Long.MIN_VALUE, Long.MAX_VALUE) or
                nullableRangeCond(dateRange, OrderModel.id neq 0, OrderModel.closedAt, Long.MIN_VALUE, Long.MAX_VALUE)) and
                (OrderModel.mechanic eq idValue)
            }

        fun washerOrders(
            idValue: Int,
            filterWrapper: FieldFilterWrapper<Long>?
        ): SizedIterable<WashDao> =
            WashDao.find {
                timeCond(Pair(filterWrapper?.bottomBound, filterWrapper?.topBound), WashModel.createdAt) and
                (WashModel.author eq idValue)
            }

        fun hoursCompleted(
            idValue: Int,
            filterWrapper: FieldFilterWrapper<Long>?,
        ): Double =
            UserHoursModel.select(UserHoursModel.hours).where {
                (UserHoursModel.user eq idValue) and
                timeCond(Pair(filterWrapper?.bottomBound, filterWrapper?.topBound), WashModel.createdAt)
            }.sumOf { it[UserHoursModel.hours] }

        fun driverViolations(
            idValue: Int,
            filterWrapper: FieldFilterWrapper<Long>?
        ): SizedIterable<ViolationDao> =
            ViolationDao.find {
                timeCond(Pair(filterWrapper?.bottomBound, filterWrapper?.topBound), ViolationModel.createdAt) and
                (ViolationModel.driver eq idValue)
            }

    }

    val login by UserModel.login
    val email by UserModel.email
    val phoneNumber by UserModel.phoneNumber
    val hash by UserModel.hash
    val fullName by UserModel.fullName
    val birthday by UserModel.birthday
    val licenceTypeId by UserModel.licenceType
    val licenceType by LicenceTypeDao referencedOn UserModel.licenceType
    val positionId by UserModel.position
    val position by PositionDao referencedOn UserModel.position
    val departmentId by UserModel.department
    val department by DepartmentDao referencedOn UserModel.department
    val insuranceNumber by UserModel.insuranceNumber
    val sectorBossId by UserModel.sectorBossId
    val sectorBoss by UserDao optionalReferencedOn UserModel.sectorBossId

    val lastTrip: TripDao? get() =
        TripDao.find {
            (TripModel.driver eq idValue)
        }.orderBy(TripModel.keyReturn to SortOrder.DESC).firstOrNull()


    override fun toOutputDto(): UserDto =
        UserDto(
            idValue, login, email, phoneNumber,
            fullName, birthday, licenceTypeId.value,
            positionId.value, departmentId.value
        )

    val simpleDto: UserSimpleDto get() =
        UserSimpleDto(idValue, fullName)

    val driverOutput: DriverOutputDto get() =
        DriverOutputDto(
            idValue, fullName, lastTrip?.simpleDto, licenceType.name, photo = photos
        )

    fun toStaffOutput(orderInProgress: Long, orderCompleted: Long, hoursCompleted: Double): StaffOutputDto =
        StaffOutputDto(
            idValue, fullName, position.name,
            orderInProgress, orderCompleted,
            hoursCompleted, photos
        )
    val staffDto: StaffDto get() =
        StaffDto(idValue, fullName, phoneNumber, department.name, position.name, photos)

    val driverDto: DriverDto get() =
        DriverDto(idValue, fullName, department.name, insuranceNumber, phoneNumber,
            licenceType.name, sectorBoss?.staffDto, position.name, photos)

    val photos: List<String> get() {
        return (UserPhotoModel innerJoin PhotoModel)
            .select(PhotoModel.link)
            .where {
                UserPhotoModel.user eq idValue
            }
            .map {
                it[PhotoModel.link]
            }
    }
}