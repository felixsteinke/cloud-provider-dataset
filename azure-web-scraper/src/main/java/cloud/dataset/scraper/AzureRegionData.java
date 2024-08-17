package cloud.dataset.scraper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AzureRegionData {
    private String geography;
    private String regionName;
    private String physicalLocation;
    private String sourceUrl;
}
