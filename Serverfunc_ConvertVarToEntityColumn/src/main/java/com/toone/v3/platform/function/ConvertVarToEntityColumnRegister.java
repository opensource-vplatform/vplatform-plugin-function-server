package com.toone.v3.platform.function;

import com.yindangu.v3.plugin.vds.reg.api.IRegisterPlugin;
import com.yindangu.v3.plugin.vds.reg.api.builder.IFunctionBuilder;
import com.yindangu.v3.plugin.vds.reg.api.model.IComponentProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IFunctionProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IPluginProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.VariableType;
import com.yindangu.v3.plugin.vds.reg.common.RegVds;

import java.util.ArrayList;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 13:39
 */
public class ConvertVarToEntityColumnRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_ConvertVarToEntityColumn";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "ConvertVarToEntityColumn";
    private static final String Plugin_Name = "将字符串的值添加到实体中指定的字段";
    private static final String Plugin_Desc = "将字符串的值添加到实体中指定的字段。";
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
     * ConvertVarToEntityColumn函数
     *
     * @return ConvertVarToEntityColumn函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("来源字符串")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("实体编码")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("字段编码")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo4 = pluginBuilder.newInput()
                .setDesc("分隔符")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setType(VariableType.Char)
                .setDesc("返回值")
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(ConvertVarToEntityColumnFunc.class)
                .setExample("代码示例: ConvertVarToEntityColumn(\"今天;明天;后天\",\"BR_IN_PARENT.entityCode\",\"fieldCode\",\";\")。\n" +
                        "参数1：来源字符串（字符串类型，必填）；\n" +
                        "参数2：实体编码（字符串类型，必填，必须带前缀，实体可以是方法输入(BR_IN_PARENT.entityCode)、方法输出(BR_OUT_PARENT.entityCode)、方法变量(BR_VAR_PARENT.entityCode)）；\n" +
                        "参数3：字段编码（字符串类型，必填）；\n" +
                        "参数4：参数1中多个数据之间的分隔符（字符串类型，必填）；\n" +
                        "返回值类型：无返回值。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3)
                .addInputParam(inputVo4);

        return pluginBuilder.build();
    }
}
