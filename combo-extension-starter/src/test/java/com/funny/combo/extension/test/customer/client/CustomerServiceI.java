package com.funny.combo.extension.test.customer.client;


import com.funny.combo.core.result.Response;
import com.funny.combo.core.result.SingleResponse;

/**
 * CustomerServiceI
 *
 * @author Frank Zhang 2018-01-06 7:24 PM
 */
public interface CustomerServiceI {
     Response addCustomer(AddCustomerCmd addCustomerCmd);
     SingleResponse<CustomerDTO> getCustomer(GetOneCustomerQry getOneCustomerQry);
}
