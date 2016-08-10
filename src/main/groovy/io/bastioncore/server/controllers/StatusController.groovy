package io.bastioncore.server.controllers

import akka.pattern.Patterns
import akka.util.Timeout
import io.bastioncore.core.BastionContext
import io.bastioncore.core.messages.Messages
import io.bastioncore.core.messages.ResponseMessage
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration

/**
 *
 */
@RestController
class StatusController {

    @RequestMapping('/')
    public def status(){
        def data = ['status':'OK']
        data['processes'] = BastionContext.instance.processPool.collect { it.key }
        return data
    }

    @RequestMapping('/logs')
    public def logs(){
        def res = [:]
        BastionContext.instance.processPool.each {
            FiniteDuration duration = Duration.create('3 seconds')
            Future future = Patterns.ask(it.value, Messages.QUERY_LOGS,Timeout.durationToTimeout(duration))
            ResponseMessage resp = Await.result(future,duration)
            res[it.key] = resp.content
        }
        return res
    }
}
