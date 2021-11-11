package com.funny.combo.encrypt.web;

import com.funny.combo.encrypt.web.domain.DecryptOut;
import com.funny.combo.encrypt.web.domain.EncryptOut;
import com.funny.combo.encrypt.web.service.ICryptoService;
import com.funny.combo.encrypt.web.service.config.SecurityKey;
import com.funny.combo.encrypt.web.service.impl.MobileService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class MobileServiceTest {
    private ICryptoService cryptoService;
    private MobileService mobileService;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        mobileService = new MobileService();

        cryptoService = mock(ICryptoService.class);
        Field cryptoServiceField = mobileService.getClass().getDeclaredField("cryptoService");
        cryptoServiceField.setAccessible(true);
        cryptoServiceField.set(mobileService, cryptoService);
    }

    @Test
    public void encryptTest_with_error_mobile_expect_status_is_102() throws Exception {
        List<EncryptOut> list = mobileService.encrypt(null, "");

        assertEquals(Integer.valueOf(102), list.get(0).getStatus());
    }

    @Test
    public void encryptTest_with_null_key_expect_status_is_500() throws Exception {
        String mobile = "13683092276";
        when(cryptoService.encryptOne(any(SecurityKey.class), any(String.class)))
                .thenThrow(new NullPointerException());

        List<EncryptOut> list = mobileService.encrypt(null, mobile);

        assertEquals(Integer.valueOf(500), list.get(0).getStatus());
    }

    @Test
    public void encryptTest_with_success_return_is_expect_base64() throws Exception {
        SecurityKey key = new SecurityKey();
        String mobile = "13683092276";

        List<EncryptOut> list = mobileService.encrypt(key, mobile);

        assertEquals(Integer.valueOf(0), list.get(0).getStatus());
    }

    @Test
    public void decryptTest_with_decryptOne_exception_expect_status_is_500() throws Exception {
        String base64="QqydzTB/gWgzE07Xro2JofkinR5fV/JdU6iQ++HVNCIAdgAAAAE=";
        when(cryptoService.decryptOne(any(String.class), any(String.class))).thenThrow(new NullPointerException());

        List<DecryptOut> list = mobileService.decrypt("appId", base64);

        assertEquals(Integer.valueOf(500), list.get(0).getStatus());
    }

    @Test
    public void decryptTest_with_success_expect_status_is_0() throws Exception {
        String base64="tpnyjxbRnm/qGdWmcbc4ewB2AAAAAQ==";

        List<DecryptOut> list = mobileService.decrypt("appId", base64);

        assertEquals(Integer.valueOf(0), list.get(0).getStatus());
    }

}
