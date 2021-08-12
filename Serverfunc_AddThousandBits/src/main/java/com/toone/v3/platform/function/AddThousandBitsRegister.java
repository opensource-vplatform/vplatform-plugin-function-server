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
 * @Author xugang
 * @Date 2021/5/27 11:23
 */
public class AddThousandBitsRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_AddThousandBits";
    private static final String Component_Version = "3.10.0";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "AddThousandBits";
    private static final String Plugin_Name = "给数字添加千分位";
    private static final String Plugin_Desc = "给数字添加千分位。";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin().getComponentProfile()
                .setGroupId(Group_Id)
                .setCode(Component_Code)
                .setVersion(Component_Version)
                .build();
    }

    @Override
    public List<IPluginProfileVo> getPluginProfile() {
        List<IPluginProfileVo> plugins = new ArrayList<>();
        plugins.add(getAddThousandBits());

        return plugins;
    }

    /**
     * 绝对值函数
     *
     * @return 绝对值函数描述器
     */
    private IFunctionProfileVo getAddThousandBits() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo input = pluginBuilder.newInput()
                .setDesc("数字")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo output = pluginBuilder.newOutput()
                .setDesc("带千分位的字符串")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setName(Plugin_Name)
                .setDesc(Plugin_Desc)
                .setEntry(AddThousandBitsFunc.class)
                .setExample("代码示例：AddThousandBits(1234567.7654321) 返回值：1,234,567.765,432,1。\n" +
                        "参数一：需要添加千分位的数字（数字类型）；\n" +
                        "返回值类型：字符串类型。")
                .setOutput(output)
                .addInputParam(input);

        return pluginBuilder.build();
    }
}
