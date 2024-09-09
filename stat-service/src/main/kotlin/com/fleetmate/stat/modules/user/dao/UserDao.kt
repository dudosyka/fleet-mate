package com.fleetmate.stat.modules.user.dao


import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserLicenceModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.user.model.UserRoleModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.user.dto.UserDto
import com.fleetmate.stat.modules.user.dto.UserSimpleDto
import com.fleetmate.stat.modules.user.dto.output.DriverDto
import com.fleetmate.stat.modules.user.dto.output.StaffDto
import com.fleetmate.stat.modules.user.model.UserPhotoModel
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder

class UserDao(id: EntityID<Int>) : BaseIntEntity<UserDto>(id, UserModel) {
    companion object : BaseIntEntityClass<UserDto, UserDao>(UserModel)

    val login by UserModel.login
    val email by UserModel.email
    val phoneNumber by UserModel.phoneNumber
    val hash by UserModel.hash
    val fullName by UserModel.fullName
    val birthday by UserModel.birthday
    val positionId by UserModel.position
    val position by PositionDao referencedOn UserModel.position
    val departmentId by UserModel.department
    val department by DepartmentDao referencedOn UserModel.department
    val insuranceNumber by UserModel.insuranceNumber
    val sectorBossId by UserModel.sectorBossId
    val sectorBoss by UserDao optionalReferencedOn UserModel.sectorBossId
    val licenceNumber by UserModel.licenceNumber


    val lastTrip: TripDao? get() =
        TripDao.find {
            (TripModel.driver eq idValue)
        }.orderBy(TripModel.keyReturn to SortOrder.DESC).firstOrNull()

    val licenceTypes: List<String> get() =
        UserLicenceModel.innerJoin(LicenceTypeModel).select(LicenceTypeModel.name).where { UserLicenceModel.user eq idValue }.map {
            it[LicenceTypeModel.name]
        }

    override fun toOutputDto(): UserDto =
        UserDto(
            idValue, login, email, phoneNumber,
            fullName, birthday, licenceTypes,
            positionId.value, departmentId.value
        )

    val roles: List<Int> get() =
        UserRoleModel.select(UserRoleModel.role).where {
            UserRoleModel.user eq idValue
        }.map { it[UserRoleModel.role] }

    val simpleDto: UserSimpleDto get() =
        UserSimpleDto(idValue, fullName)

    val staffDto: StaffDto get() =
        if (roles.contains(AppConf.roles.washer) || roles.contains(AppConf.roles.mechanic) || roles.contains(AppConf.roles.juniorMechanic) || roles.contains(AppConf.roles.admin))
            StaffDto(idValue, fullName, phoneNumber, department.name, position.name, photos)
        else
            throw NotFoundException("")

    val driverDto: DriverDto get() =
        if (roles.contains(AppConf.roles.driver))
            DriverDto(idValue, fullName, department.name, insuranceNumber, phoneNumber,
                licenceTypes, sectorBoss?.staffDto, position.name, photos,
                licenceNumber ?: throw InternalServerException("a driver's license cannot be missing from the driver"))
        else
            throw NotFoundException("")

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