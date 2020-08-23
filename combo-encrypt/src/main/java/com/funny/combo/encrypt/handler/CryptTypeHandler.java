package com.funny.combo.encrypt.handler;


import com.funny.combo.encrypt.exception.EncryptException;
import com.funny.combo.encrypt.utils.EncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 解密Handler
 * Created by funnystack on 20/5/8.
 */
public class CryptTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (StringUtils.isNotEmpty(parameter)) {
            try {
				ps.setObject(i, EncryptUtils.getInstance().encryption(parameter).getEncrypted());
			} catch (EncryptException e) {
				throw new SQLException("敏感字段加解密出错", e);
			}
        }else {
            ps.setNull(i, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        if (StringUtils.isNotEmpty(rs.getString(columnName))) {
            try {
				return EncryptUtils.getInstance().decryption(rs.getString(columnName)).getOrigin();
			} catch (EncryptException e) {
				throw new SQLException("敏感字段加解密出错", e);
			}
        }
        return null;
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        if (StringUtils.isNotEmpty(rs.getString(columnIndex))) {
            try {
				return EncryptUtils.getInstance().decryption(rs.getString(columnIndex)).getOrigin();
			} catch (EncryptException e) {
				throw new SQLException("敏感字段加解密出错", e);
			}
        }
        return null;
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (StringUtils.isNotEmpty(cs.getString(columnIndex))) {
            try {
				return EncryptUtils.getInstance().decryption(cs.getString(columnIndex)).getOrigin();
			} catch (EncryptException e) {
				throw new SQLException("敏感字段加解密出错", e);
			}
        }
        return null;
    }


}