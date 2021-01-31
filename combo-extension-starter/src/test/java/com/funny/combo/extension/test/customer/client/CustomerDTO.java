package com.funny.combo.extension.test.customer.client;

import com.funny.combo.core.dto.AbstractDTO;
import com.funny.combo.extension.test.customer.domain.CustomerType;


/**
 * CustomerDTO
 *
 * @author Frank Zhang 2018-01-06 7:30 PM
 */
public class CustomerDTO extends AbstractDTO {

    private String companyName;
    private String source;  //advertisement, p4p, RFQ, ATM
    private CustomerType customerType; //potential, intentional, important, vip

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
}
