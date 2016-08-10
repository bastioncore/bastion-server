package io.bastioncore.server.controllers

import groovy.transform.CompileStatic
import io.bastioncore.core.BastionContext
import io.bastioncore.core.messages.DefaultMessage
import io.bastioncore.core.messages.ResponseMessage
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
/**
 *
 */
@Controller
@CompileStatic
class ApiController {

    @RequestMapping(value = '/ask/{id}')
    @ResponseBody def ask(@PathVariable String id, HttpServletRequest request, HttpServletResponse response){
        ResponseMessage responseMessage = BastionContext.instance.subscriberPoolsCollector.askSubscribers(id,createMessage(request),'10 seconds')
        return responseMessage.content
    }

    @RequestMapping(value = '/tell/{id}')
    @ResponseBody def tell(@PathVariable String id, HttpServletRequest request, HttpServletResponse response){
        BastionContext.instance.subscriberPoolsCollector.tellSubscribers(id,createMessage(request))
        return ['success':true]
    }


    private DefaultMessage createMessage(HttpServletRequest request){
        return populateContext(new DefaultMessage(getBody(request)),request)
    }

    private byte[] getBody(HttpServletRequest request){
        final ServletInputStream inputStream = request.getInputStream()
        byte[] data = IOUtils.toByteArray(inputStream)
        inputStream.close()
        return data
    }

    private DefaultMessage populateContext(DefaultMessage defaultMessage,HttpServletRequest request){
        defaultMessage.context.headers = [:]
        Enumeration<String> names = request.headerNames
        while(names.hasMoreElements()) {
            String it = names.nextElement()
            defaultMessage.context.headers[it] = request.getHeader(it)
        }
        return defaultMessage
    }
}
