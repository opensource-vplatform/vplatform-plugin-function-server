package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
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
    private static final String Component_Version = "3.10.0";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin()
                .getComponentProfile()
                .setGroupId(ServerFuncCommonUtils.Group_Id)
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
        IFunctionProfileVo.IFunctionInputVo inputVo = pluginBuilder.newInput()
                .setDesc("正弦值")
                .setType(VariableType.Number)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("角度")
                .setType(VariableType.Number)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.GetSerialNumberFunc.Function_Code())
                .setDesc(ServerFuncCommonUtils.GetSerialNumberFunc.Function_Desc())
                .setName(ServerFuncCommonUtils.GetSerialNumberFunc.Function_Name())
                .setEntry(GetSerialNumberFunc.class)
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
                .addInputParam(inputVo);

        return pluginBuilder.build();
    }
}
