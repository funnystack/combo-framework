package com.funny.combo.extension.test.customer.app.extension;

import com.funny.combo.core.common.ApplicationContextHelper;
import com.funny.combo.extension.test.customer.client.AddCustomerCmd;
import com.funny.combo.extension.test.customer.client.CustomerDTO;
import com.funny.combo.extension.test.customer.domain.CustomerEntity;
import org.springframework.stereotype.Component;

/**
 * CustomerConvertor
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:08 AM
 */
@Component
public class CustomerConvertor{

    public CustomerEntity clientToEntity(Object clientObject){
        AddCustomerCmd addCustomerCmd = (AddCustomerCmd)clientObject;
        CustomerDTO customerDTO =addCustomerCmd.getCustomerDTO();
        CustomerEntity customerEntity = (CustomerEntity) ApplicationContextHelper.getBean(CustomerEntity.class);
        customerEntity.setCompanyName(customerDTO.getCompanyName());
        customerEntity.setCustomerType(customerDTO.getCustomerType());
        customerEntity.setBizScenario(addCustomerCmd.getBizScenario());
        return customerEntity;
    }
}
