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
 * @Date 2021/6/16 10:26
 */
public class IsNullRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_IsNull";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "IsNull";
    private static final String Plugin_Name = "空值处理";
    private static final String Plugin_Desc = "检查输入的参数是否为空值,不为空返回原值，为空返回默认值。";
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
     * IsNull函数
     *
     * @return IsNull函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("参数1")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer, VariableType.Boolean, VariableType.Text, VariableType.Char, VariableType.Date, VariableType.LongDate, VariableType.Entity))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("参数2")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer, VariableType.Boolean, VariableType.Text, VariableType.Char, VariableType.Date, VariableType.LongDate, VariableType.Entity))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer, VariableType.Boolean, VariableType.Text, VariableType.Char, VariableType.Date, VariableType.LongDate, VariableType.Entity))
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(IsNullFunc.class)
                .setExample("代码示例:IsNull(arg,defaultVal)第一个参数不为空直接返回第一个参数,为空时返回默认值。\n" +
                        "参数1--被检查的值；\n" +
                        "参数2--为空时的缺省值；\n" +
                        "返回值为返回参数的数据类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
