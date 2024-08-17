package cloud.dataset.scraper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AzureMachineData {
    private String machine;
    private String family;
    private String workloadTypes;
    private String cpu;
    private Integer vCpus;
    private Integer nodes;
    private Float memoryGB;
    private Float benchmarkScore;
    private String sourceUrl;
}
