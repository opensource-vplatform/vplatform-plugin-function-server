package com.toone.v3.platform.function;

import com.yindangu.v3.plugin.vds.reg.api.IRegisterPlugin;
import com.yindangu.v3.plugin.vds.reg.api.builder.IFunctionBuilder;
import com.yindangu.v3.plugin.vds.reg.api.model.IComponentProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IFunctionProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IPluginProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.VariableType;
import com.yindangu.v3.plugin.vds.reg.common.RegVds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 21:45
 */
public class GetConditionColumnValueRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetConditionColumnValue";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "GetConditionColumnValue";
    private static final String Plugin_Name = "按照条件获取对应的实体的字段信息";
    private static final String Plugin_Desc = "1、根据条件查询指定数据源中某个字段的值 2、如果该条件下返回多条记录，则取第一条记录的值。";
    private static final String Component_Version = "3.10.0";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin()
                .getComponentProfile()
                .setGroupId(Group_Id)
                .setCode(Component_Code)
                .setVersion(Component_Version)
                .build();
    }

    @Override
    public List<IPluginProfileVo> getPluginProfile() {
        List<IPluginProfileVo> plugins = new ArrayList<>();
        plugins.add(getFunc());

        return plugins;
    }

    /**
     * GetConditionColumnValue函数
     *
     * @return GetConditionColumnValue函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("方法实体编码")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("字段拜纳姆")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("查询条件")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Char, VariableType.Number, VariableType.Integer, VariableType.Text, VariableType.Date,VariableType.LongDate, VariableType.Entity, VariableType.Boolean))
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(GetConditionColumnValueFunc.class)
                .setExample("代码示例1：GetConditionColumnValue(\"BR_IN_PARENT.entityCode\",\"name\",\"age=1\")。\n" +
                        "代码示例2：GetConditionColumnValue(\"BR_VAR_PARENT.entityCode\",\"name\",\"wb='文本'\")。\n" +
                        "参数1--方法实体编码(字符串类型)，方法输入实体（BR_IN_PARENT.entityCode）、方法变量实体（BR_VAR_PARENT.entityCode）、方法输出实体（BR_OUT_PARENT.entityCode）； \n" +
                        "参数2--方法实体中某个字段的编码(字符串类型)； \n" +
                        "参数3--查询条件(字符串类型，如果值是字符串类型，则需要加单引号，参考示例2)； \n" +
                        "若参数3中的条件值来源变量，则参数3需要用一个变量代替，如：\n" +
                        "GetConditionColumnValue(\"BR_VAR_PARENT.TableName\",\"name\",BR_VAR_PARENT.cs3)\n" +
                        "其中BR_VAR_PARENT.cs3的值=ConcatStr(\"name=\",BR_VAR_PARENT.cs3_z)\n" +
                        "若BR_VAR_PARENT.cs3_z为字符串，拼接参数3时，还需要加上单引号，如：\n" +
                        "ConcatStr(\"name=\",\"'\",BR_VAR_PARENT.cs3_z,\"'\")\n" +
                        "返回值类型：与参数二所选字段的类型一致。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3);

        return pluginBuilder.build();
    }
}
