package com.github.xfslove.autoconfigure.shiro.jwt;

import com.github.xfslove.autoconfigure.shiro.model.Jwt;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.github.xfslove.autoconfigure.shiro.exception.OAuth2AuthenticationException;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by hanwen on 28/12/2017.
 */
public class JwtUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

  public static String generate(Jwt jwt, String clientSecret) {
    try {

      byte[] dest = new byte[32];
      byte[] src = clientSecret.getBytes();
      System.arraycopy(src, 0, dest, 0, Math.min(src.length, 32));

      JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
          .subject(jwt.getSubject())
          .expirationTime(jwt.getExpires());

      for (Map.Entry<String, Object> entry : jwt.getClaims().entrySet()) {
        builder.claim(entry.getKey(), entry.getValue());
      }

      JWTClaimsSet claimsSet = builder.build();

      // jwt
      SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
      signedJwt.sign(new MACSigner(dest));

      // jwe
//      JWEObject jweObject = new JWEObject(
//          new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
//              // required to signal nested JWT
//              .contentType("JWT")
//              .build(),
//          new Payload(signedJwt));

//      jweObject.encrypt(new DirectEncrypter(dest));

      return signedJwt.serialize();
    } catch (JOSEException e) {
      LOGGER.error(ExceptionUtils.getStackTrace(e));
    }

    return null;
  }

  public static Jwt parse(String jwtString, String clientSecret, String... claimFields) {
    try {

      byte[] dest = new byte[32];
      byte[] src = clientSecret.getBytes();
      System.arraycopy(src, 0, dest, 0, Math.min(clientSecret.getBytes().length, 32));

//      JWEObject jweObject = JWEObject.parse(jwtString);
//      jweObject.decrypt(new DirectDecrypter(dest));
//
//      SignedJWT signedJWT1 = jweObject.getPayload().toSignedJWT();

      SignedJWT signedJwt = SignedJWT.parse(jwtString);
      MACVerifier verifier = new MACVerifier(dest);

      if (!signedJwt.verify(verifier)) {
        throw new OAuth2AuthenticationException("json web token[" +  verifier + "] not verified");
      }

      Jwt jwt = new Jwt();

      JWTClaimsSet jwtClaimsSet = signedJwt.getJWTClaimsSet();
      jwt.setSubject(jwtClaimsSet.getSubject());
      jwt.setExpires(jwtClaimsSet.getExpirationTime());

      if (claimFields.length == 0) {
        return jwt;
      }

      Map<String, Object> claims = jwt.getClaims();
      for (String claim : claimFields) {
        Object claimObj = jwtClaimsSet.getClaim(claim);
        if (claimObj instanceof JSONArray) {
          claims.put(claim, new ArrayList<>((JSONArray) claimObj));
        } else {
          claims.put(claim, claimObj);
        }
      }

      return jwt;
    } catch (Exception e) {
      LOGGER.error(ExceptionUtils.getStackTrace(e));
    }

    return null;
  }
}
