package com.javaclub;

import dev.paseto.jpaseto.Paseto;
import dev.paseto.jpaseto.PasetoParser;
import dev.paseto.jpaseto.Pasetos;
import dev.paseto.jpaseto.Version;
import dev.paseto.jpaseto.lang.Keys;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PublicTokenDemo {
    public static void main(String[] args) {
        KeyPair keyPair = Keys.keyPairFor(Version.V2); // you need to be careful to choose the right version, used different algorithms
        Instant now = Instant.now();

        String v2PublicToken = Pasetos.V2.PUBLIC.builder()
                .setPrivateKey(keyPair.getPrivate())
                .setIssuedAt(now)
                .setExpiration(now.plus(1, ChronoUnit.HOURS))
                .setAudience("javaclub")
//                .setSubject("demo")
//                .footerClaim("kid", "java-club-event-287")
                .claim("topic", "PASETO intro")
                .claim("javaVersion", 17)
                .claim("demo", new String(new byte[10000]))
//                .claim("permissions", List.of("participate in community", "be a speaker", "talk not about JS"))
                .compact();


        logInfo(v2PublicToken);
//
        PasetoParser pasetoParser = Pasetos.parserBuilder()
                .setPublicKey(keyPair.getPublic())
//                .setSharedSecret(secretKey)
//                .requireAudience("jsClub")
//                .require("javaVersion", value -> (long) value >=17) // must be same data as in build token
                .build();
////
        Paseto parsedObject = pasetoParser.parse(v2PublicToken);
//
        logInfo("Claims => " + parsedObject.getClaims());
        logInfo("Footer => " + parsedObject.getFooter());
        logInfo("Purpose => " + parsedObject.getPurpose());
        logInfo("Version => " + parsedObject.getVersion());
        logInfo("DEMO => " + parsedObject.getClaims().get("demo", String.class));
    }

    private static void logInfo(String msg) {
        System.out.println(msg);
    }
}