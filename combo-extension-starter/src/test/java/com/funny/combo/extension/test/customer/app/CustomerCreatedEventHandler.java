package com.funny.combo.extension.test.customer.app;

import com.funny.combo.core.result.Response;
import com.funny.combo.extension.test.customer.client.CustomerCreatedEvent;

/**
 * CustomerCreatedEventHandler
 *
 * @author Frank Zhang
 * @date 2020-06-22 7:00 PM
 */
public class CustomerCreatedEventHandler {

    public Response execute(CustomerCreatedEvent customerCreatedEvent) {
        System.out.println("customerCreatedEvent processed");
        return null;
    }
}
