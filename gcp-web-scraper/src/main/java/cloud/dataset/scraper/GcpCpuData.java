package cloud.dataset.scraper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GcpCpuData {
    private String cpuType;
    private String skuCpu;
    private String machines;
    private String sourceUrl;
}
