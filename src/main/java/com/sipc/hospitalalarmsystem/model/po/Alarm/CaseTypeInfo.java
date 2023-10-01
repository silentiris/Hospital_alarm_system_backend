package com.sipc.hospitalalarmsystem.model.po.Alarm;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-25 22:22
 */
@Data
@TableName(value = "case_type_info")
public class CaseTypeInfo
{
    private Integer id;
    private String caseTypeName;
}
