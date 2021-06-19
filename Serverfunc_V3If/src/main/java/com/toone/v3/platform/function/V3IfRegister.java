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
import java.util.Arrays;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/6/17 9:59
 */
public class V3IfRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_V3If";
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
     * V3If函数
     *
     * @return V3If函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("条件")
                .setType(VariableType.Boolean)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("结果1")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Entity, VariableType.Integer, VariableType.Char, VariableType.Number, VariableType.Date, VariableType.LongDate, VariableType.Boolean, VariableType.Text))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("结果2")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Entity, VariableType.Integer, VariableType.Char, VariableType.Number, VariableType.Date, VariableType.LongDate, VariableType.Boolean, VariableType.Text))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Entity, VariableType.Integer, VariableType.Char, VariableType.Number, VariableType.Date, VariableType.LongDate, VariableType.Boolean, VariableType.Text))
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.V3If.Function_Code())
                .setDesc(ServerFuncCommonUtils.V3If.Function_Desc())
                .setName(ServerFuncCommonUtils.V3If.Function_Name())
                .setEntry(V3IfFunc.class)
                .setExample("代码示例:V3If(1==1,20,30) 返回值为 20。\n" +
                        "参数1--条件表达式（布尔类型）；\n" +
                        "参数2--条件表达式为true时返回值（各种类型）；\n" +
                        "参数3--条件表达式为false时返回值（各种类型）；\n" +
                        "返回值类型不定，由参数2，或者参数3决定。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3);

        return pluginBuilder.build();
    }
}
