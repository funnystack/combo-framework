package com.funny.combo.encrypt.web;


import com.funny.combo.encrypt.web.service.config.KeyConfig;
import com.funny.combo.encrypt.web.service.config.SecurityKey;
import com.funny.combo.encrypt.web.service.impl.CryptoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CryptoServiceTest {
    private KeyConfig keyConfig;

    private CryptoService cryptoService;

    public static SecurityKey buildSecurityKey() {
        SecurityKey key = new SecurityKey();
        key.setDepartment("dealer");
        key.setGroup("ics");
        key.setBusiness("order");
        key.setPrefix('v');
        key.setSequence(1);
        key.setKey("0123456789012345");
        return key;
    }

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        cryptoService = new CryptoService();

        keyConfig = mock(KeyConfig.class);
        Field keyConfigField = cryptoService.getClass().getDeclaredField("keyConfig");
        keyConfigField.setAccessible(true);
        keyConfigField.set(cryptoService, keyConfig);
    }

    @Test
    public void encryptOneTest_with_a_key_return_is_expect_encrypted() throws Exception {
        SecurityKey key = buildSecurityKey();
        String mobile = "13683092276";
        String encrypted = cryptoService.encryptOne(key, mobile);

        assertEquals("tpnyjxbRnm/qGdWmcbc4ewB2AAAAAQ==", encrypted);
    }

    @Test
    public void hashMobileTest_with_a_sale_return_is_expect_hash() throws Exception {
        when(keyConfig.getSalt()).thenReturn("salt");
        String mobile = "13683092276";
        String hash = cryptoService.hashMobile(mobile);

        assertEquals("11666ab123fd207e3f42efa4240ad94a130c2fdaeaaca762e611e697b7b44c65", hash);
    }

    @Test
    public void decryptOneTest_with_a_key_return_is_expect_mobile() throws Exception {
        String base64 = "tpnyjxbRnm/qGdWmcbc4ewB2AAAAAQ==";
        SecurityKey key = buildSecurityKey();
        when(keyConfig.getSecurityKey(any(String.class), any(Integer.class))).thenReturn(key);

        String mobile = cryptoService.decryptOne(key.getConsumerId(), base64);

        assertEquals("13683092276", mobile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decryptOneTest_with_a_null_Key_return_is_expect_IllegalArgumentException() throws Exception {
        String base64 = "tpnyjxbRnm/qGdWmcbc4ewB2AAAAAQ==";
        SecurityKey key = buildSecurityKey();
        when(keyConfig.getSecurityKey(any(String.class), any(Integer.class))).thenReturn(null);
        cryptoService.decryptOne(key.getConsumerId(), base64);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decryptOneTest_with_a_error_base64_return_is_expect_IllegalArgumentException() throws Exception {
        String base64 = "tpnyjxbRnm/qGdWmcbc4ewB2AAAAAQ==sd";
        SecurityKey key = buildSecurityKey();
        when(keyConfig.getSecurityKey(any(String.class), any(Integer.class))).thenReturn(null);
        cryptoService.decryptOne(key.getConsumerId(), base64);
    }

    @Test(expected = RuntimeException.class)
    public void decryptOneTest_with_a_otherKey_return_is_expect_runtimeException() throws Exception {
        String base64 = "tpnyjxbRnm/qGdWmcbc4ewB2AAAAAQ==";
        SecurityKey key = buildSecurityKey();
        key.setKey("01234567890123sd");
        when(keyConfig.getSecurityKey(any(String.class), any(Integer.class))).thenReturn(key);
        cryptoService.decryptOne(key.getConsumerId(), base64);
    }
}
