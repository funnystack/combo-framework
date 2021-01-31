package com.funny.combo.extension.test.customer.client;


import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.extension.BizScenario;
import lombok.Data;

/**
 * AddCustomerCmd
 *
 * @author Frank Zhang 2018-01-06 7:28 PM
 */
@Data
public class AddCustomerCmd extends AbstractCommand {

    private CustomerDTO customerDTO;

    private String biz;

    private BizScenario bizScenario;
}
