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
 * AsciiToUnicode函数注册器
 *
 * @Author xugang
 * @Date 2021/5/27 11:23
 */
public class AsciiToUnicodeRegister implements IRegisterPlugin {

    private final static String Component_Code = "Serverfunc_AsciiToUnicode";
    private static final String Component_Version = "3.10.0";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin().getComponentProfile()
                .setGroupId(ServerFuncCommonUtils.Group_Id)
                .setCode(Component_Code)
                .setVersion(Component_Version)
                .build();
    }

    @Override
    public List<IPluginProfileVo> getPluginProfile() {
        List<IPluginProfileVo> plugins = new ArrayList<>();
        plugins.add(getAsciiToUnicode());

        return plugins;
    }

    /**
     * 绝对值函数
     *
     * @return 绝对值函数描述器
     */
    private IFunctionProfileVo getAsciiToUnicode() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo = pluginBuilder.newInput()
                .setDesc("ascii字符串")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("Unicode字符串")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.AsciiToUnicode.Function_Code())
                .setDesc(ServerFuncCommonUtils.AsciiToUnicode.Function_Desc())
                .setName(ServerFuncCommonUtils.AsciiToUnicode.Function_Name())
                .setEntry(AsciiToUnicodeFunc.class)
                .setExample("代码示例:AsciiToUnicode(\"同望Toone\")返回值为\"&#21516;&#26395;&#84;&#111;&#111;&#110;&#101;\"。\n" +
                        "参数1--字符串(字符串类型)\n" +
                        "返回值为字符串类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo);

        return pluginBuilder.build();
    }
}
