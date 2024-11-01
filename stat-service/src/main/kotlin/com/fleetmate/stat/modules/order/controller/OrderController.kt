package com.fleetmate.stat.modules.order.controller


import com.fleetmate.lib.shared.dto.IdInputDto
import com.fleetmate.lib.utils.kodein.KodeinController
import com.fleetmate.stat.modules.order.data.dto.order.CreateOrderDto
import com.fleetmate.stat.modules.order.data.dto.order.OrderFilterDto
import com.fleetmate.stat.modules.order.data.dto.work.CreateWorkDto
import com.fleetmate.stat.modules.order.service.OrderService
import com.fleetmate.stat.modules.order.service.WorkService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class OrderController(override val di: DI) : KodeinController() {
    private val orderService: OrderService by instance()
    private val workService: WorkService by instance()

    override fun Route.registerRoutes() {
        authenticate("admin", "mechanic") {
            route("work") {
                get("types") {
                    call.respond(workService.getAllTypes())
                }
            }
            route("order") {
                get("status") {
                    call.respond(orderService.getAllStatuses())
                }
                post("all") {
                    val orderFilterDto = call.receive<OrderFilterDto>()
                    orderFilterDto.parseRanges()

                    call.respond(orderService.getAllFiltered(orderFilterDto))
                }
                post {
                    val orderId = call.receive<IdInputDto>().id

                    call.respond(orderService.getOne(orderId))
                }
                post("create") {
                    val createOrderDto = call.receive<CreateOrderDto>()

                    call.respond(orderService.create(createOrderDto))
                }
                route("work") {
                    post {
                        val orderId = call.receive<IdInputDto>().id

                        call.respond(orderService.getOrderWorkList(orderId))
                    }
                    post("actor") {
                        val juniorMechanicId = call.receive<IdInputDto>().id

                        call.respond(orderService.getWorkListByJuniorMechanic(juniorMechanicId))
                    }
                    patch {
                        val createWorkDto = call.receive<CreateWorkDto>()

                        call.respond(orderService.appendWork(createWorkDto))
                    }
                }
                patch("close") {
                    val orderId = call.receive<IdInputDto>().id

                    call.respond(orderService.close(orderId))
                }
            }
        }
        authenticate("washer") {
            route("wash") {
                post("washer") {
                    val washerId = call.receive<IdInputDto>().id

                    call.respond(orderService.getWashListByWasher(washerId))
                }
            }
        }
    }

}