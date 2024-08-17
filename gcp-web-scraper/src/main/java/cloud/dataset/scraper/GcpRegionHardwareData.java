package cloud.dataset.scraper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GcpRegionHardwareData {
    private String cloudZone;
    private String cloudRegion;
    private String location;
    private String machineTypes;
    private String cpus;
    private String resource;
    private String sourceUrl;
}
