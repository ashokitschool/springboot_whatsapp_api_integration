package in.ashokit.model;

import lombok.Data;

import java.util.List;

@Data
public class WatiResponse {

    private String result;
    private String phone_number;
    private List<WatiParameters> parameters;
    private boolean validWhatsAppNumber;
    private String otp;
}
