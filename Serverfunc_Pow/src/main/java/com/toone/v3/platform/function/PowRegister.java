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
 * @Date 2021/6/1 16:05
 */
public class PowRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_Pow";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "Pow";
    private static final String Plugin_Name = "指定数字的幂";
    private static final String Plugin_Desc = "返回指定数字的指定次幂。";
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
     * Pow函数
     *
     * @return Pow函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("参数1")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Integer, VariableType.Number))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("参数2")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Integer, VariableType.Number))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("结果")
                .setType(VariableType.Char)
                .setDesignType(VariableType.Number)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(PowFunc.class)
                .setExample("代码示例：Pow(3,2)返回值为9。\n" +
                        "参数1--指数（数字类型）；\n" +
                        "参数2--幂（数字类型）；\n" +
                        "返回值类型：数字类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
