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
 * @Date 2021/6/17 9:56
 */
public class SubstringRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_Substring";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "Substring";
    private static final String Plugin_Name = "子链提取";
    private static final String Plugin_Desc = "从字符串指定索引（0基准）位置开始提取指定长度的该字符串的子串,返回提取出的子串。";
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
     * Substring函数
     *
     * @return Substring函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("字符串")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("起始位置")
                .setType(VariableType.Integer)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("长度")
                .setType(VariableType.Integer)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(SubstringFunc.class)
                .setExample("代码示例:Substring(\"abcdefg\",2,3)，返回值为\"cde\"。\n" +
                        "参数1--原字符串(字符串类型)；\n" +
                        "参数2--开始提取子串的索引位置(整型，不能小于0，第1个字符索引为0)；\n" +
                        "参数3--提取子串的长度(整型，大于0)； \n" +
                        "返回值为字符串类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3);

        return pluginBuilder.build();
    }
}
