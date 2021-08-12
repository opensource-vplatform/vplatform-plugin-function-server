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
 * @Date 2021/5/31 10:59
 */
public class CheckChineseRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_CheckChinese";
    private static final String Component_Version = "3.10.0";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "CheckChinese";
    private static final String Plugin_Name = "中文检查";
    private static final String Plugin_Desc = "检查指定字符串是否包含中文字符。";

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
     * CheckChinese函数
     *
     * @return CheckChinese函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo = pluginBuilder.newInput()
                .setDesc("字符串")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("是否包含中文")
                .setType(VariableType.Boolean)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(CheckChineseFunc.class)
                .setExample("代码示例:CheckChinese(\"a你好bc\") 返回值为True。\n" +
                        "参数1--待检查字符串(字符串类型)；\n" +
                        "返回值为布尔类型")
                .setOutput(outputVo)
                .addInputParam(inputVo);

        return pluginBuilder.build();
    }
}
