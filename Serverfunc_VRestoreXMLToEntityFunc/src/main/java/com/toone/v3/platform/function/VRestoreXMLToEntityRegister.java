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
 * @Date 2021/6/17 10:00
 */
public class VRestoreXMLToEntityRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_VRestoreXMLToEntityFunc";
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
     * VRestoreXMLToEntityFunc函数
     *
     * @return VRestoreXMLToEntityFunc函数描述器
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
                .setType(VariableType.Char)
                .setRequired(false)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.VRestoreXMLToEntityFunc.Function_Code())
                .setDesc(ServerFuncCommonUtils.VRestoreXMLToEntityFunc.Function_Desc())
                .setName(ServerFuncCommonUtils.VRestoreXMLToEntityFunc.Function_Name())
                .setEntry(VRestoreXMLToEntityFunc.class)
                .setExample("代码示例：VRestoreXMLToEntityFunc(\"xml内容\",\"BR_IN_PARENT.a1:BR_IN_PARENT.a2,BR_IN_PARENT.b1:BR_OUT_PARENT.b2\")，表示将XML还原后的方法输入a1实体还原到方法输入a2实体，方法输入b1实体还原到方法输出b2实体。\n" +
                        "参数1：由VConvertEntityToXML/VConvertEntityToXMLFunc函数生成的XML信息（字符串类型）；\n" +
                        "参数2：还原数据的来源与目标实体映射关系（字符串类型，选填，整个参数不填时则按原编码完全匹配），映射格式：源1:目标1,源2:目标2（即多个映射采用逗号分隔），方法输入实体（BR_IN_PARENT.entityCode）、方法变量实体（BR_VAR_PARENT.entityCode）、方法输出实体（BR_OUT_PARENT.entityCode）；\n" +
                        "备注：目标实体结构（字段和类型）必须和源实体一致，才能还原成功。\n" +
                        "无返回值。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
