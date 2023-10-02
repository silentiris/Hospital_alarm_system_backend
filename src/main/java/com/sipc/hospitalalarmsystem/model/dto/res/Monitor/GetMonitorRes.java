package com.sipc.hospitalalarmsystem.model.dto.res.Monitor;

import lombok.Data;

import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-01 21:02
 */

@Data
public class GetMonitorRes {

    @Data
    public static class MonitorBorder{
        private Integer leftX;
        private Integer leftY;
        private Integer rightX;
        private Integer rightY;
    }

    @Data
    public static class MonitorAbilities {

        public MonitorAbilities(Boolean dangerArea,Boolean fall,Boolean flame,Boolean smoke){
            this.abilities.add(new MonitorAbility(1,dangerArea));
            this.abilities.add(new MonitorAbility(2,flame));
            this.abilities.add(new MonitorAbility(3,fall));
            this.abilities.add(new MonitorAbility(4,smoke));
        }

        public static class MonitorAbility {
            public MonitorAbility(Integer caseType,Boolean checked){
                this.name = caseType == 1?"进入危险区域":caseType == 2?"烟雾":caseType == 3?"摔倒":caseType==4?"烟雾":"吸烟";
                this.value = caseType;
                this.checked = checked;
            }
            private String name;
            private Integer value;
            private Boolean checked;
        }



        private List<MonitorAbility> abilities;
    }



    private Integer id;
    private String name;
    private String deal;
    private String department;
    private String leader;
    private Boolean running;
    private List<MonitorBorder> border;
    private String video;
    private String img;
    private List<MonitorAbilities> ability;



}
