package com.funny.combo.encrypt.web.controller;

import com.funny.combo.core.result.SingleResponse;
import com.funny.combo.encrypt.web.domain.DecryptOut;
import com.funny.combo.encrypt.web.domain.EncryptOut;
import com.funny.combo.encrypt.web.service.IMobileService;
import com.funny.combo.encrypt.web.service.config.KeyConfig;
import com.funny.combo.encrypt.web.service.config.SecurityKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 手机号加密解密接口
 */
@Controller
@RequestMapping(value = "/mobile")
public class MobileController {
    private static final int MAX_REQUEST_MOBILE_SIZE = 1000;

    @Resource
    private KeyConfig keyConfig;

    @Resource
    private IMobileService mobileService;

    @ResponseBody
    @RequestMapping(value = "/encrypt")
    public SingleResponse encrypt(String _appId, String consumerId, String mobiles) {
        if (StringUtils.isBlank(_appId)) {
            return SingleResponse.fail("_appId is null");
        }
        if (StringUtils.isBlank(mobiles)) {
            return SingleResponse.fail("mobiles is null");
        }
        String[] array = mobiles.split(",");
        return encrypt(_appId, consumerId, array);
    }

    @ResponseBody
    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public SingleResponse encrypt(@RequestParam String _appId, @RequestParam String consumerId, @RequestBody String... mobiles) {
        SingleResponse valid = validEncryptParams(_appId, consumerId, mobiles);
        if (!valid.isSuccess()) {
            return valid;
        }

        SecurityKey key = keyConfig.getSecurityKey(consumerId);
        List<EncryptOut> result = mobileService.encrypt(key, mobiles);
        return SingleResponse.succ(result);
    }

    @ResponseBody
    @RequestMapping(value = "/hash")
    public SingleResponse hash(String _appId, String consumerId, String mobiles) {
        if (StringUtils.isBlank(_appId)) {
            return SingleResponse.fail("_appId is null");
        }
        if (StringUtils.isBlank(mobiles)) {
            return SingleResponse.fail("mobiles is null");
        }
        String[] array = mobiles.split(",");
        return hash(_appId, consumerId, array);
    }

    @ResponseBody
    @RequestMapping(value = "/hash", method = RequestMethod.POST)
    public SingleResponse hash(@RequestParam String _appId, @RequestParam String consumerId, @RequestBody String... mobiles) {
        SingleResponse singleResponse = validEncryptParams(_appId, consumerId, mobiles);
        if (!singleResponse.isSuccess()) {
            return singleResponse;
        }

        List<EncryptOut> result = mobileService.hash(consumerId, mobiles);
        return SingleResponse.succ(result);
    }

    private SingleResponse validEncryptParams(String _appId, String consumerId, String[] mobiles) {
        if (StringUtils.isBlank(_appId)) {
            return SingleResponse.fail("_appId is null");
        }
        if (StringUtils.isBlank(consumerId)) {
            return SingleResponse.fail("consumerId is null");
        }
        if (!keyConfig.isExistsConsumerId(consumerId)) {
            return SingleResponse.fail("consumerId not exists");
        }
        if (mobiles != null && mobiles.length > 0 && mobiles.length <= MAX_REQUEST_MOBILE_SIZE) {
            return SingleResponse.fail("mobiles 参数不能为空且数组长度小于：" + MAX_REQUEST_MOBILE_SIZE);
        }
        return SingleResponse.succ();
    }

    @ResponseBody
    @RequestMapping(value = "/decrypt")
    public SingleResponse decrypt(String _appId, String consumerId, String ciphers) {
        if (StringUtils.isBlank(_appId)) {
            return SingleResponse.fail("_appId is null");
        }
        if (StringUtils.isBlank(ciphers)) {
            return SingleResponse.fail("ciphers is null");
        }
        String[] array = ciphers.split(",");
        return decrypt(_appId, consumerId, array);
    }

    @ResponseBody
    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    public SingleResponse decrypt(@RequestParam String _appId, @RequestParam String consumerId, @RequestBody String... ciphers) {
        if (StringUtils.isBlank(_appId)) {
            return SingleResponse.fail("_appId is null");
        }
        if (StringUtils.isBlank(consumerId)) {
            return SingleResponse.fail("consumerId is null");
        }
        if (keyConfig.isExistsConsumerId(consumerId)) {
            return SingleResponse.fail("consumerId=" + consumerId + " 在 security_key.xml 中不存在对应的 SecurityKey 配置节点");
        }
        if (ciphers != null && ciphers.length > 0 && ciphers.length <= MAX_REQUEST_MOBILE_SIZE) {
            return SingleResponse.fail("ciphers 参数不能为空且数组长度小于：" + MAX_REQUEST_MOBILE_SIZE);
        }


        List<DecryptOut> result = mobileService.decrypt(consumerId, ciphers);
        return SingleResponse.succ(result);
    }

}
