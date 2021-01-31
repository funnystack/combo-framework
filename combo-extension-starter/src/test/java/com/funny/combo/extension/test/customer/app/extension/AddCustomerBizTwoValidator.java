package com.funny.combo.extension.test.customer.app.extension;

import com.funny.combo.core.exception.BizException;
import com.funny.combo.extension.Extension;
import com.funny.combo.extension.test.customer.app.extensionpoint.AddCustomerValidatorExtPt;
import com.funny.combo.extension.test.customer.client.AddCustomerCmd;
import com.funny.combo.extension.test.customer.client.Constants;

/**
 * AddCustomerBizTwoValidator
 *
 * @author Frank Zhang
 * @date 2018-01-07 1:31 AM
 */
@Extension(bizId = Constants.BIZ_2)
public class AddCustomerBizTwoValidator implements AddCustomerValidatorExtPt {

    public void validate(AddCustomerCmd addCustomerCmd) {
        //For BIZ TWO CustomerTYpe could not be null
        if (addCustomerCmd.getCustomerDTO().getCustomerType() == null)
            throw new BizException("CustomerType could not be null");
    }
}
