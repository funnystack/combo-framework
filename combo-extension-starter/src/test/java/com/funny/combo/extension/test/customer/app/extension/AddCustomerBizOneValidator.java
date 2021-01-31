package com.funny.combo.extension.test.customer.app.extension;

import com.funny.combo.core.exception.BizException;
import com.funny.combo.extension.Extension;
import com.funny.combo.extension.test.customer.app.extensionpoint.AddCustomerValidatorExtPt;
import com.funny.combo.extension.test.customer.client.AddCustomerCmd;
import com.funny.combo.extension.test.customer.client.Constants;
import com.funny.combo.extension.test.customer.domain.CustomerType;

/**
 * AddCustomerBizOneValidator
 *
 * @author Frank Zhang
 * @date 2018-01-07 1:31 AM
 */
@Extension(bizId = Constants.BIZ_1)
public class AddCustomerBizOneValidator implements AddCustomerValidatorExtPt {

    public void validate(AddCustomerCmd addCustomerCmd) {
        //For BIZ TWO CustomerTYpe could not be VIP
        if(CustomerType.VIP == addCustomerCmd.getCustomerDTO().getCustomerType())
            throw new BizException("Customer Type could not be VIP for Biz One");
    }
}
