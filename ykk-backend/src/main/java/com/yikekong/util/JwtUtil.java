package com.yikekong.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Slf4j
public class JwtUtil {
    //设置秘钥明文
    public static final String JWT_KEY = "yykkey";

    /**
     * 生成jwt token
     * @return
     */
    public static String createJWT(Integer adminId) {

        //指定签名中使用的加密算法hs256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //获取当前时间毫秒数
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //如果传入的超时时间参数为空, 使用默认1小时作为超时时间

        ZoneId zoneId = ZoneId.systemDefault();
        //使用当前时间+ 超时时间=具体在哪个时间点超时
        long expMillis = LocalDateTime.now().plusDays(7).atZone(zoneId).toInstant().toEpochMilli();
        //在这个时间超时
        Date expDate = new Date(expMillis);
        //将传入的id与userName转换为json
        HashMap<String, Object> map = new HashMap<>();
        map.put("adminId",adminId);
        String s = null;
        try {
            s = JsonUtil.serialize(map);
        } catch (JsonProcessingException e) {
            log.error("json序列化失败",e);
        }

        //指定加密的钥匙是什么
        SecretKey secretKey = generalKey();

        JwtBuilder builder = Jwts.builder()
                .setSubject(s)//设置json:id和userName
                .setIssuedAt(now)//签发时间
                .signWith(signatureAlgorithm,secretKey)//使用hs256加密对称算法签名,第二个是秘钥
                .setExpiration(expDate);//设置过期时间
        return builder.compact();

    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}
