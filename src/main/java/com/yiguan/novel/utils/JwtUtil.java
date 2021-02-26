package com.yiguan.novel.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    //密钥
    public static final String SECRET = "sdjhakdhajdklsl;o653632";
    //过期时间:天
    public static final int EXPIRE = 10;

    /**
     * 生成Token
     * @param userId
     * @param userName
     * @return
     * @throws Exception
     */
    public static String createToken(Long userId, String userName) throws Exception {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DATE, EXPIRE);
        Date expireDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        String token = JWT.create()
                .withHeader(map)//头
                .withClaim("userId", userId)
                .withClaim("userName", userName)
                .withSubject("novelToken")//主题
                .withIssuedAt(new Date())//签名时间
                .withExpiresAt(expireDate)//过期时间
                .sign(Algorithm.HMAC256(SECRET));//签名
        return token;
    }

    /**
     * 验证Token
     * @param token
     * @return
     * @throws Exception
     */
    public static boolean verifyToken(String token)throws Exception{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
            return false;
        }catch (Exception e){
//            throw new RuntimeException("凭证已过期，请重新登录");
            return true;
        }
    }

    /**
     * 解析Token
     * @param token
     * @return
     */
    public static DecodedJWT parseToken(String token) throws UnsupportedEncodingException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }
}
