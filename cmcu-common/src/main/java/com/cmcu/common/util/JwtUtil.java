package com.cmcu.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2018/9/3 14:09
 * To change this template use File | Settings | File Templates.
 */
public class JwtUtil {


    private static final String JWT_KEY="CO_JWT";

    /**
     *  auth0 生成token
     * @param data
     * @param expireTime
     * @return 失败返回null
     */
    public static String encodeJwtToken(Object data, Date expireTime){
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withSubject(JsonMapper.obj2String(data))
                    .withExpiresAt(expireTime)
                    .sign(algorithm);
            return token;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * auth0解密
     * @param token
     * @return
     */
    public static String decodeJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (Exception e) {
            return null;
        }
    }




}
