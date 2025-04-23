package com.duyhai.identityservice.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.duyhai.identityservice.dto.request.AuthenticationRequest;
import com.duyhai.identityservice.dto.request.IntrospectRequest;
import com.duyhai.identityservice.dto.request.LogouttRequest;
import com.duyhai.identityservice.dto.request.RefreshTokenRequest;
import com.duyhai.identityservice.dto.response.AuthenticationResponse;
import com.duyhai.identityservice.dto.response.IntrospectResponse;
import com.duyhai.identityservice.entity.InvalidToken;
import com.duyhai.identityservice.entity.User;
import com.duyhai.identityservice.exception.AppException;
import com.duyhai.identityservice.exception.ErrorCode;
import com.duyhai.identityservice.repository.InvalidTokenRepository;
import com.duyhai.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidTokenRepository invalidTokenRepository;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    // validate token
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        // verify token
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public void logout(LogouttRequest req) throws ParseException, JOSEException {
        var signToken = verifyToken(req.getToken(), true);
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiredTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidToken invalidToken =
                InvalidToken.builder().id(jit).expiredTime(expiredTime).build();
        invalidTokenRepository.save(invalidToken);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest req) throws ParseException, JOSEException {
        // kiem tra hieu luc cua token
        var signJWT = verifyToken(req.getToken(), true);
        // thuc hien refresh
        // b1: invalid cai token cu di vi no sap het hieu luc
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        Date expiredTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidToken invalidToken =
                InvalidToken.builder().id(jit).expiredTime(expiredTime).build();
        invalidTokenRepository.save(invalidToken);
        // b2 : create mot token moi
        var username = signJWT.getJWTClaimsSet().getSubject();
        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("leduyhai.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                // them mot claim nua : token_id : jti
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    // them role va permisssion vao claimset
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });

        return stringJoiner.toString();
    }

    // verify token
    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {

        // verify token
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        // check xem neu verify thi chi getExpirationTime con refresh thi cho time dai hon
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier); // check xem chu ky bi sua doi
        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // check xem token do co trong db invalidToken khong
        if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }
}
