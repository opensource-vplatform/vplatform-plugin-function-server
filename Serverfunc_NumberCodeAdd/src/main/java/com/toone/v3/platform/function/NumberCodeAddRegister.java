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
 * @Date 2021/6/17 9:43
 */
public class NumberCodeAddRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_NumberCodeAdd";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "NumberCodeAdd";
    private static final String Plugin_Name = "编码字符串累加";
    private static final String Plugin_Desc = "对数值型的字符串进行数值加减操作，并依照原格式返回字符串。";
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
     * NumberCodeAdd函数
     *
     * @return NumberCodeAdd函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("参数1")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("参数2")
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
                .setEntry(NumberCodeAddFunc.class)
                .setExample("代码示例:NumberCodeAdd(\"0010203\",-3)返回值为\"0010200\"。\n" +
                        "参数1--能转换成数值的编码字符串(字符串类型, 仅支持正整数)；\n" +
                        "参数2--累加的数值(整型)；\n" +
                        "返回值为字符串类型（返回值长度有参数1决定，补前导零，相加值小于零，结果将为零）。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
