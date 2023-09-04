package com.javaclub;

import dev.paseto.jpaseto.Paseto;
import dev.paseto.jpaseto.PasetoParser;
import dev.paseto.jpaseto.Pasetos;
import dev.paseto.jpaseto.lang.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LocalTokenDemo {
    public static void main(String[] args) {
        SecretKey secretKey = Keys.secretKey();
        Instant now = Instant.now();

        String v2LocalToken = Pasetos.V2.LOCAL.builder()
                .setSharedSecret(secretKey)
                .setIssuedAt(now)
                .setExpiration(now.plus(1, ChronoUnit.HOURS))
                .setAudience("javaclub")
                .setSubject("demo")
                .footerClaim("kid", "java-club-event-287")
                .claim("topic", "PASETO intro")
                .claim("javaVersion", 17)
                .claim("permissions", List.of("participate in community", "be a speaker", "talk not about JS"))
                .compact();

        logInfo(v2LocalToken);

        PasetoParser pasetoParser = Pasetos.parserBuilder()
                .setSharedSecret(secretKey)
//                .requireAudience("jsClub")
//                .require("javaVersion", 17L) // must be same data as in build token
                .require("javaVersion", value -> (long) value >=17) // must be same data as in build token
                .build();
//
        Paseto parsedObject = pasetoParser.parse(v2LocalToken);
//
        logInfo("Claims => " + parsedObject.getClaims());
        logInfo("Footer => " + parsedObject.getFooter());
        logInfo("Purpose => " + parsedObject.getPurpose());
        logInfo("Version => " + parsedObject.getVersion());


        logInfo("topic => " + parsedObject.getClaims().get("topic", Integer.class));
//        logInfo("javaVersion => " + parsedObject.getClaims().get("javaVersion",String.class));


    }

    private static void logInfo(String msg) {
        System.out.println(msg);
    }
}