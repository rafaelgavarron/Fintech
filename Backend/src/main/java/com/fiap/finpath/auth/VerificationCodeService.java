package com.fiap.finpath.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public VerificationCode createVerificationCode(String userEmail) {
        VerificationCode code = new VerificationCode(userEmail);
        System.out.println("VerificationCodeService: Creating verification code for " + userEmail + ". Code: " + code.getCodeText());
        return verificationCodeRepository.save(code);
    }

    public boolean verifyCode(String userEmail, String codeText) {
        System.out.println("VerificationCodeService: Verifying code for " + userEmail);
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByUserEmailAndCodeText(userEmail, codeText);

        if (optionalCode.isPresent()) {
            VerificationCode code = optionalCode.get();
            if (!code.isUsed() && code.getExpireAt() > System.currentTimeMillis() / 1000) {
                code.setUsed(true);
                verificationCodeRepository.save(code);
                return true;
            }
        }
        return false;
    }

    public void invalidateCode(String codeId) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findById(codeId);
        if (optionalCode.isPresent()) {
            VerificationCode code = optionalCode.get();
            code.setUsed(true);
            verificationCodeRepository.save(code);
        }
    }

    public void cleanupExpiredCodes() {
        long currentTime = System.currentTimeMillis() / 1000;
        List<VerificationCode> expiredCodes = verificationCodeRepository.findExpiredCodes(currentTime);
        verificationCodeRepository.deleteAll(expiredCodes);
        System.out.println("VerificationCodeService: Cleaned up " + expiredCodes.size() + " expired codes");
    }
}
