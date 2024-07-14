package in.ashokit.model;

import lombok.Data;

import java.util.List;

@Data
public class WatiRequest {

    private String template_name;
    private String broadcast_name;
    private List<WatiParameters> parameters;

}
