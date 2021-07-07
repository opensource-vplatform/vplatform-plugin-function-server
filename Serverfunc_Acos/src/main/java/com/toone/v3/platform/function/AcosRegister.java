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
 * 反余弦函数注册器
 *
 * @Author xugang
 * @Date 2021/5/27 11:42
 */
public class AcosRegister implements IRegisterPlugin {

    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Component_Code = "Serverfunc_Acos";
    private static final String Component_Version = "3.10.0";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "Acos";
    private static final String Plugin_Name = "反余弦";
    private static final String Plugin_Desc = "反余弦，返回余弦值为指定值的角度。";

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
        plugins.add(getACos());

        return plugins;
    }

    /**
     * 反余弦函数
     *
     * @return 反余弦函数描述器
     */
    private IFunctionProfileVo getACos() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo = pluginBuilder.newInput()
                .setDesc("余弦值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("角度")
                .setType(VariableType.Char)
                .setDesignType(VariableType.Number)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(AcosFunc.class)
                .setExample("代码示例:Acos(1)返回值为0。\n" +
                        "参数1--指定的角度(小数类型)，取值范围:[-1,1]；\n" +
                        "返回值类型：数字类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo);

        return pluginBuilder.build();
    }
}
