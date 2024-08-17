package cloud.dataset.scraper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GcpMachineData {
    private String machine;
    private String workloadTypes;
    private String cpuTypeText;
    private List<String> cpuTypes;
    private String architecture;
    private String vCpuRange;
    private String vCpuDefinition;
    private String memoryRange;
    private String maxLocalSsd;
    private String networkRange;
    private String vCpuCoremarkScore;
    private String sourceUrl;

    public GcpMachineData(String machineName, String sourceUrl) {
        this.machine = machineName;
        this.sourceUrl = sourceUrl;
    }

    public void setCpuTypeText(String cpuTypeText) {
        this.cpuTypeText = cpuTypeText;
        if (cpuTypeText != null) {
            List<String> list = new ArrayList<>();
            for (String s : cpuTypeText.split(", ")) {
                if (s.contains("and ")) {
                    for (String str : s.split("and ")) {
                        if (!str.isBlank()) {
                            list.add(str);
                        }
                    }
                } else {
                    list.add(s);
                }
            }
            cpuTypes = list.stream()
                    .map(s -> s.replaceAll("Intel", "")
                            .replaceAll("AMD", "")
                            .replaceAll("intel", "")
                            .trim())
                    .toList();
        }
    }

    public Integer getVCpuRangeStart() {
        return extractRangeStart(vCpuRange);
    }

    public Integer getVCpuRangeEnd() {
        return extractRangeEnd(vCpuRange);
    }

    public Integer getMemoryRangeStart() {
        return extractRangeStart(memoryRange);
    }

    public Integer getMemoryRangeEnd() {
        return extractRangeEnd(memoryRange);
    }

    public Integer getNetworkRangeStart() {
        return extractRangeStart(networkRange);
    }

    public Integer getNetworkRangeEnd() {
        return extractRangeEnd(networkRange);
    }

    public Integer getMaxLocalSsd() {
        return extractRangeEnd(maxLocalSsd);
    }

    public Float getVCpuCoremarkScore() {
        if (vCpuCoremarkScore == null) {
            return null;
        }
        try {
            return Float.parseFloat(vCpuCoremarkScore);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer extractRangeStart(String rangeValue) {
        if (rangeValue == null) {
            return null;
        }
        try {
            return (int) Float.parseFloat(rangeValue.split(" ")[0]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer extractRangeEnd(String rangeValue) {
        if (rangeValue == null) {
            return null;
        }
        try {
            return (int) Float.parseFloat(rangeValue.split(" ")[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return extractRangeStart(rangeValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
