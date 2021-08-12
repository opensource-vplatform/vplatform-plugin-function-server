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
 * 加法运算函数注册器
 *
 * @Author xugang
 * @Date 2021/5/28 8:59
 */
public class ServerAddRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_ServerAdd";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "ServerAdd";
    private static final String Plugin_Name = "加法";
    private static final String Plugin_Desc = "1. 对数值做加法运算\r\n2. 最后一位参数为小数保留位\r\n3. 结果按照保留小数位数进行四舍五入";
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
        plugins.add(getServerAdd());

        return plugins;
    }

    private IPluginProfileVo getServerAdd() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("参数1")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("参数2")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("参数3")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer))
                .setRequired(true)
                .setStartIndex(2)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("运算和")
                .setType(VariableType.Char)
                .setDesignType(VariableType.Number)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(ServerAddFunc.class)
                .setExample("代码示例:ServerAdd(arg1,arg2,...,argN,argN+1) 返回值为数字类型。\n" +
                        "参数1--被加数(数字类型)；\n" +
                        "参数2--加数(数字类型)；\n" +
                        "参数N--加数(数字类型)；\n" +
                        "参数N+1--小数保留位数(数字类型)；\n" +
                        "返回值为数字类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3);

        return pluginBuilder.build();
    }
}
