package cloud.dataset.scraper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AzureSustainabilityData {
    private String regionText; // Iowa (Central US)
    private String pueText; // 1.16
    private String cfeText; // 100
    private String dateText; // March 2023
    private String cloudRegion; // Central US
    private Integer year; // 2023
    private String sourceUrl; // https://datacenters.microsoft.com/globe/pdfs/sustainability/factsheets/Iowa%20(Central%20US).pdf
    private String sourceDescription;

    private static Float textToFloat(String text) {
        if (text == null) {
            return null;
        }
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Float getPue() {
        return textToFloat(pueText);
    }

    public Float getCfe() {
        Float percentageCfe = textToFloat(cfeText);
        if (percentageCfe == null) {
            return null;
        } else {
            return percentageCfe / 100;
        }
    }
}
