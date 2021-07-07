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
 * @Date 2021/5/31 21:58
 */
public class GetSerialNumberRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetSerialNumberFunc";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "GetSerialNumberFunc";
    private static final String Plugin_Name = "流水号函数";
    private static final String Plugin_Desc = "根据前缀，取出表里的最大流水号加1后，补齐位数返回字符串。";
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
     * GetSerialNumberFunc函数
     *
     * @return GetSerialNumberFunc函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("表名")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("字段名")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("前缀字符串")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo4 = pluginBuilder.newInput()
                .setDesc("流水号长度")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo5 = pluginBuilder.newInput()
                .setDesc("补位符")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo6 = pluginBuilder.newInput()
                .setDesc("查询语句的like值")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo7 = pluginBuilder.newInput()
                .setDesc("截取流水号的起始位置")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo8 = pluginBuilder.newInput()
                .setDesc("是否从左边截取")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo9 = pluginBuilder.newInput()
                .setDesc("是否重用流水号")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("流水号")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(GetSerialNumberFunc.class)
                .setDeprecated(true)
                .setExample("代码示例:GetSerialNumberFunc(\"TableName\",\"ColumnName\",\"20151103--\",\"11\",\"0\",\"0\",\"3\",\"true\",\"true\") 返回值为00000000001。\n" +
                        "参数1--表名(字符串类型)；\n" +
                        "参数2--字段名(字符串类型)；\n" +
                        "参数3--前缀字符串(字符串类型，只用于查询流水号时使用，不包含在返回结果中)；\n" +
                        "参数4--流水号长度(字符串类型) \n" +
                        "参数5--补位符(字符串类型，并且长度必须为1)；\n" +
                        "参数6--查询语句的like值(字符串类型)\n" +
                        "参数7--截取流水号的起始位置，下标从0开始(字符串类型)\n" +
                        "参数8--是否从左边截取(字符串类型)\n" +
                        "参数9--是否重用流水号(字符串类型)\n" +
                        "返回值为字符串类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3)
                .addInputParam(inputVo4)
                .addInputParam(inputVo5)
                .addInputParam(inputVo6)
                .addInputParam(inputVo7)
                .addInputParam(inputVo8)
                .addInputParam(inputVo9);

        return pluginBuilder.build();
    }
}
