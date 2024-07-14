package in.ashokit.service;

import in.ashokit.model.User;
import in.ashokit.model.WatiParameters;
import in.ashokit.model.WatiRequest;
import in.ashokit.model.WatiResponse;
import in.ashokit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    @Value("${wati.otp.template.name}")
    private String watiOtpTemplateName;

    @Value("${wati.send.template.msg.url}")
    private String watiTemplateMsgApiUrl;

    @Value("${wati.token}")
    private String token;

    @Autowired
    private UserRepository userRepo;

    public boolean saveUser(User user){
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpStatus("NOT-VERIFIED");
        userRepo.save(user);

        WatiResponse watiResponse = sendOtp(user.getPhone(),otp);
        System.out.println(watiResponse);

        return true;
    }

    public WatiResponse sendOtp(String phno,String otp){
        RestTemplate rt = new RestTemplate();
        String apiUrl = watiTemplateMsgApiUrl + "?whatsappNumber=" + phno;

        WatiParameters params = new WatiParameters();
        params.setName("otp");
        params.setValue(otp);

        WatiRequest requestDTO = new WatiRequest();
        requestDTO.setTemplate_name(watiOtpTemplateName);
        requestDTO.setBroadcast_name(watiOtpTemplateName);
        requestDTO.setParameters(Arrays.asList(params));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        HttpEntity<WatiRequest> requestEntity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<WatiResponse> response = rt.postForEntity(apiUrl, requestEntity, WatiResponse.class);
        response.getBody().setOtp(otp);
        return response.getBody();
    }

    private String generateOTP() {
        // Length of the OTP
        int length = 4;
        // Generate random digits
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return otp.toString();
    }

    public User validateOtp(String email, String otp){
        User user = userRepo.findByEmailAndOtp(email, otp);
        if(user!=null){
            user.setOtpStatus("VERIFIED");
            return userRepo.save(user);
        }
        return null;
    }
}
