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
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 21:41
 */
public class GenerateSequenceNumberRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GenerateSequenceNumber";
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
     * GenerateSequenceNumber函数
     *
     * @return GenerateSequenceNumber函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("流水号种子")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("生成模式")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("需要使用的废弃流水号")
                .setType(VariableType.Integer)
                .setRequired(false)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("流水号")
                .setType(VariableType.Integer)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.GenerateSequenceNumber.Function_Code())
                .setDesc(ServerFuncCommonUtils.GenerateSequenceNumber.Function_Desc())
                .setName(ServerFuncCommonUtils.GenerateSequenceNumber.Function_Name())
                .setEntry(GenerateSequenceNumberFunc.class)
                .setExample("代码示例：GenerateSequenceNumber (\"123456\", \"INC\") 返回值是：\"1\" 。\n" +
                        "参数1：流水号种子，字符串类型。根据该种子生成流水号，最长128位字符串，建议64位或者更短。\n" +
                        "参数2：生成模式，字符串类型。\n" +
                        "支持模式：\n" +
                        "INC：最大号+1；\n" +
                        "REUSE：重用废弃的流水号，当没有废号可用时，同INC；\n" +
                        "ASSIGNERR：用给定的值作为流水号，如果该值已被占用，那么生成失败；\n" +
                        "ASSIGNINC：用给定的值作为流水号，如果该值已被占用，那么同INC；\n" +
                        "ASSIGNREUSE：用给定的值作为流水号，如果该值已被占用，那么同REUSE；\n" +
                        "参数3：需要使用的废弃流水号，整数类型。当且仅当参数2为ASSIGNERR、ASSIGNINC、ASSIGNREUSE才有效。\n" +
                        "返回值：流水号，整数类型 ")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3);

        return pluginBuilder.build();
    }
}
