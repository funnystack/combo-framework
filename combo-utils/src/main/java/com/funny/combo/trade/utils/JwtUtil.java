package com.funny.combo.trade.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.UUID;

/**
 * @author: funnystack
 * @create: 2019-06-05 14:08
 **/
public class JwtUtil {

	private static final String USER_NAME = "username";
	// Token过期时间30分钟（用户登录过期时间是此时间的两倍，以token在reids缓存时间为准）
	public static final long EXPIRE_TIME = 30 * 60 * 1000;
	private static final String secret = "secret";

	/**
	 * 校验token是否正确
	 *
	 * @param token    令牌
	 * @param username 用户名
	 * @return 是否正确
	 */
	public static boolean verify(String token, String username) {
		try {
			// 根据密码生成JWT效验器
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withClaim(USER_NAME, username).build();
			// 效验TOKEN
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的用户名
	 */
	public static String getUsername(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			// 已过期
			if (jwt.getExpiresAt() != null && jwt.getExpiresAt().before(new Date())) {
				return null;
			}
			// 未生效
			if (jwt.getIssuedAt() != null && jwt.getIssuedAt().after(new Date())) {
				return null;
			}
			return jwt.getClaim(USER_NAME).asString();
		} catch (JWTDecodeException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 生成签名,xxmin后过期
	 *
	 * @param username 用户名
	 * @return 加密的token
	 */
	public static String sign(String username) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(secret);
		// 附带username信息
		return JWT.create().withClaim(USER_NAME, username)
				.withJWTId(UUID.randomUUID().toString())//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.withExpiresAt(date) //设置过期时间
				.withIssuedAt(new Date())//iat: jwt的签发时间
				.withSubject(username)
				.sign(algorithm);

	}

}
