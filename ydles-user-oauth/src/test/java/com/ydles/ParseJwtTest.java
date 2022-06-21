package com.ydles;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public class ParseJwtTest {
    @Test
    public void parseJwt(){
        //基于公钥去解析jwt
        String jwt ="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZGRyZXNzIjoidGFpeXVhbiIsImNvbnBhbnkiOiJpdGxpbHMifQ.fzivB4T_N7VajFDf6prFzceA2vOYM27JHWCXQwAQkf1GSMdAR1OkhD1JnChAns5Yb8C53SAu1dxDKBG9c0W0-L0I0-kl4J0OsCsc01qu5VrLW2Gm4jCv8RQG8g2eqNHtbJhj3XdzGOoRBhfWqLpbAhleWyrBSVBB8sR_N5Yiia-38N0BlVeXpciNhGxQBWeRBvTH3WYkQKjJvnfnnBtzh5NbeBpQSjuv_qdnDBo2nKeJ51fmnQDDgoKVFVdCCQ56ai7gOm5lQjE47Pw6QllT3Uec3-loufr4m3DyC1jyOAqawyNpL4S1couHhbIwf36NzgTD8B1qMJR3U9_vH_JGJw";
        String publicKey ="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1e+vuZYMGIGQnTWCfHlkzOXKmEpe4Mj4rK2sl2ykt0aDq2SVrBtUawfCUzSfyuC73OBdja7IevLcivgs0Vy4cd+T2jjaB0aOr9EwnJUB94TFplwoqMdGHHYrF2JTD4QnjdQDHvR8ugGNw7lqRRhttX/L1GoSBmXl6WwQAurfcl5dUqHekVYmCpOjQPS+29LaTNsdSYWUjzg/grDUD/FjjQDCZkQ6sTt1DxAlbFra/Td41ewbEF6xfK0oRFbuZKFeznCVp08dfSO5fL9JbtCeCxnCJsYwLO1142sVe5PpA9e+qML7i/UX6wiFixwRfcvHtRgLa2n6q9Sw38fnLKxPuwIDAQAB-----END PUBLIC KEY-----";
        //解析令牌
        Jwt token = JwtHelper.decodeAndVerify(jwt, new RsaVerifier(publicKey));
        //获取负载
        String claims = token.getClaims();
        System.out.println(claims);

        String ydlershe = BCrypt.hashpw("itlils666", BCrypt.gensalt());
        System.out.println(ydlershe);
    }
}
